# CLAUDE.md — Yupana

> Documento maestro del proyecto. Léelo completo antes de proponer o escribir código.
> Resume el producto, el stack, la arquitectura objetivo, el backend y las decisiones
> ya tomadas (con su justificación). Si algo aquí entra en conflicto con un patrón que
> encuentres en el código, pregunta antes de asumir.

---

## 1. Qué es Yupana

App móvil (Android + iOS) para **revendedores de suscripciones de streaming**. Un único
admin por cuenta gestiona sus suscripciones (Netflix, Spotify, Disney+, etc.), las asigna
a clientes perfil por perfil, y recibe **recordatorios de vencimiento de cobro**. La app
NO maneja dinero: el cobro ocurre por fuera (Yape/WhatsApp/efectivo); la app organiza y
recuerda.

**El nombre** viene de la *yupana*, el instrumento de cálculo contable andino (compañero
del khipu). Identidad de marca: registro/orden contable, raíz peruana.

**Público y modelo:** multi-tenant. Se publicará en Play Store y App Store, gratis. Cada
revendedor que descarga la app es admin de su propio espacio aislado (no se ven entre
ellos). Importante para tiendas: posicionar como "gestor personal de suscripciones", no
como "panel de reventa".

### Caso de uso central
- El admin tiene **varias cuentas**, incluso varias del **mismo servicio** (ej. 3 Netflix).
- Cada cuenta tiene **perfiles**; cada perfil se asigna a un **cliente** con precio y fecha
  de vencimiento.
- El admin necesita ver de un vistazo **qué vence pronto** y a quién cobrar, y poder
  **mover un cliente de una cuenta a otra**.

---

## 2. Stack y decisiones (ya tomadas — respetar)

| Decisión | Elección | Por qué |
|---|---|---|
| Plataforma | **Kotlin Multiplatform** (Android + iOS) | Una base de lógica, dos apps nativas. |
| UI | **Compose Multiplatform** (compartida) | Se reaprovecha el design system Compose existente en ambas plataformas. |
| Arquitectura | **Clean Architecture** (domain/data/presentation) | Ya es el patrón de la base; dominio aislado de plataforma. |
| Presentación | **MVI** (State/Intent/Event) | Ya existe `:core:mvi` custom probado; se reaprovecha. |
| DI | **Koin** (multiplataforma) | NO Hilt (Hilt es solo-Android). La base ya usa Koin 4.x. |
| Backend client | **SDK supabase-kt** (auth-kt + postgrest-kt + functions-kt) | Maneja auth, sesión y refresh de token automáticamente. Usa Ktor por debajo. |
| Networking base | **Ktor client 3.x** | Ya en la base; el SDK lo usa por debajo. Alinear versión con la que exige supabase-kt 3.x. |
| Serialización | **kotlinx.serialization** | Ya en la base; default del SDK. |
| Async | **Coroutines + Flow** | Ya en la base. |
| Push | **FCM (Android) + APNs (iOS)** vía expect/actual | Desde el inicio. Motor de los recordatorios. |

### Requisitos técnicos del SDK supabase-kt (verificar versiones al instalar)
- Instalar por módulos usando el **BOM**: `auth-kt`, `postgrest-kt`, `functions-kt`.
- supabase-kt 3.x requiere **Ktor 3.0.0-rc-1+**; alinear la versión de Ktor del proyecto.
- Engines Ktor por plataforma: OkHttp/CIO (Android), **Darwin** (iOS).
- **minSdk 26** (requisito del SDK; la base estaba en 24 → subir).
- Usar la **publishable key** (`sb_publishable_xxx`), NO la anon key antigua (deprecada
  fines de 2026). URL + key por variables de entorno, nunca hardcodeadas.
- Cliente: `createSupabaseClient(url, key){ install(Auth); install(Postgrest); install(Functions) }`.
- Las tablas deben estar **expuestas en la Data API** del proyecto Supabase.
- Flujo de auth **PKCE** recomendado.

---

## 3. Estrategia de construcción

**Repo NUEVO con estructura KMP**, al que se **traen y adaptan los módulos buenos** de la
base `ce-android` (que ya es idiomática KMP: Koin + Ktor + Coroutines + kotlinx.serialization
+ Kotlin DSL + version catalog + MVI por capas, sin deuda heredada).

### Qué se reaprovecha de la base (adaptar a `commonMain` de `:shared`)
- `:core:mvi` — contrato MVI + `MviViewModel` base + `Stateful`/`StatefulScope`.
  (Migrar la `ViewModel` a la variante **multiplataforma** de lifecycle.)
- `:core:network` — Ktor client, DTOs `@Serializable`, RemoteDataSources.
  (Selección de engine por plataforma; aquí se integra supabase-kt.)
- `:core:designsystem` — componentes Compose, tema, tokens. (Portar a Compose Multiplatform.)
- `:core:utils` — dispatchers, extensiones.
- `:feature:auth` — **adaptar**, no rehacer. Mantener ViewModel/contrato/UI/tests; cambiar
  solo la tripa del repositorio para usar `supabase-kt` (`auth.signInWith(Email)`).

### Bordes de plataforma (expect/actual en `:shared`)
- **Token/sesión segura:** Android Keystore/EncryptedSharedPreferences ↔ iOS Keychain.
  (En gran parte lo maneja el SDK; aislar lo que quede propio.)
- **DataStore:** creación del store por plataforma.
- **Crashlytics/monitoring:** `AppLogger` ya es interfaz; `actual` por plataforma o wrapper KMP.
- **Engine HTTP:** OkHttp (Android) / Darwin (iOS).
- **Push:** FCM (Android) / APNs (iOS).

### Qué se reemplaza (solo-Android en la base)
- **compose-nav-graph (skydoves) KSP** → navegación de Compose Multiplatform.
- Lo que dependa de androidx-only en UI → equivalente Compose Multiplatform.

### Seguridad — hacerlo bien DE ENTRADA (sin esfuerzo extra por ser repo nuevo)
- `google-services.json` y keys **fuera del repo** (gitignore correcto desde el commit 1).
- Supabase es HTTPS (resuelve el HTTP en claro de la base).
- Resto (flavors por ambiente, R8/minify, linters, CI/CD): fase posterior, no bloquean KMP.

---

## 4. Backend — Supabase (YA EXISTE)

PostgreSQL con **RLS multi-tenant**: cada tabla lleva `owner_id` (default `auth.uid()`) y
políticas que aíslan por usuario. El SDK envía el token del usuario, así que las consultas
respetan RLS automáticamente. **No re-crear el esquema**; ya está aplicado y verificado.

### Tablas
- **streaming_service** — catálogo híbrido. `owner_id IS NULL` = servicio GLOBAL (pre-cargado:
  Netflix, Disney+, Prime Video, Max, Spotify, Apple TV/Music, YouTube Premium). `owner_id`
  con valor = servicio propio del admin. RLS: ve globales + propios; sólo edita/borra propios.
- **account** — cuenta concreta: `service_id`, `alias`, `monthly_cost`, `num_profiles`,
  `billing_day`. Puede haber varias del mismo servicio.
- **profile** — `account_id`, `label`, `pin`.
- **client** — `name`, `contact`.
- **assignment** — `profile_id`, `client_id`, `price_charged`, `start_date`, `due_date`,
  `status` (active/paused/cancelled), `reminder_sent`. Índice único: un perfil sólo puede
  tener una asignación activa a la vez.
- **vista `upcoming_expirations`** — junta assignment+client+profile+account+service y
  calcula `days_left`. Es la fuente del dashboard y de los recordatorios.

### Conceptos que NO necesitan tabla nueva
- **"Grupos" por servicio** en la UI = agrupar `account` por `service_id` en presentación.
  NO hay tabla de grupos ni CRUD de grupos.
- **"Mover un cliente de cuenta"** = update del `profile_id` en su fila de `assignment`
  (conservando precio y vencimiento). No es entidad nueva.

### Recordatorios (fase con functions-kt)
Edge Function + cron diario (pg_cron) que consulta `upcoming_expirations` donde el
vencimiento está próximo y `reminder_sent = false`, envía push vía FCM y marca enviado.

---

## 5. Identidad visual — "oscuro andino"

Material 3, tema oscuro, estética tech premium con raíz andina (motivo khipu sutil en login).

| Token | Valor | Uso |
|---|---|---|
| Fondo | `#15110E` | base, casi-negro terroso (no negro puro) |
| Superficie | `#1E1813` | tarjetas |
| Superficie elevada | `#271F18` | chips, elementos sobre tarjeta |
| **Marca (terracota)** | `#C8643C` | FAB, nav activa, acentos, botón principal |
| **Acento (dorado)** | `#E0A458` | precios, montos, detalles destacados |
| Texto principal | `#F3E9DF` | hueso cálido (no blanco frío) |
| Texto secundario | `#B8A793` | |
| Líneas/bordes | `#322820` | |
| Estados | peligro `#E5594E` · alerta `#E0A458` · ok `#6FBF8E` | urgencia de vencimiento |

Esquinas redondeadas generosas (tarjetas ~18px). Tipografía Roboto/Roboto Flex.

---

## 6. Pantallas (MVP)

1. **Login / Registro** — email + contraseña (supabase-kt Auth). Motivo khipu en el header.
2. **Dashboard "Próximos a vencer"** — métricas (vencen hoy / próximos 7 días / por cobrar)
   + lista de asignaciones con borde de color por urgencia, cliente, servicio·perfil, precio
   y chip de días restantes. Fuente: vista `upcoming_expirations`.
3. **Mis cuentas** — **agrupada por servicio, expandir/colapsar**. Encabezado de grupo con
   resumen ("Netflix · 3 cuentas · 11/15 perfiles"); al expandir, cada cuenta con alias,
   costo, día de corte y barra de ocupación de perfiles. Soporta varias cuentas del mismo
   servicio sin logos repetidos sueltos.
4. **Asignar perfil** — elegir cuenta → perfil libre → cliente (o crear) → precio → fecha
   de vencimiento. Crea una fila en `assignment`.
5. **Mover integrante** — desde el perfil ocupado: menú → "mover a otra cuenta" → elegir
   cuenta destino con perfiles libres. Update de `profile_id` en `assignment`.

---

## 7. Reglas de trabajo para Claude Code

- **Entender antes de escribir.** Ante un módulo de la base, primero léelo y resume cómo
  funciona; luego propón cómo adaptarlo. No reescribas lo que ya funciona.
- **Verificar versiones consultando**, no de memoria (BOM supabase-kt, Ktor compatible,
  plugin KMP, Compose Multiplatform). Las versiones desalineadas rompen el build KMP.
- **Por fases, compilando en cada paso.** Orden sugerido: (1) esqueleto KMP que compile,
  (2) `:shared` con dominio + supabase-kt + Koin, (3) flujo Login→Dashboard end-to-end,
  (4) Mis cuentas agrupado, (5) Asignar, (6) Mover, (7) push/recordatorios.
- **Tests:** preservar el estilo de la base (Given/When/Then, inyección por constructor,
  fakes en vez de MockK para `commonTest`).
- **Secrets fuera del repo** desde el primer commit.
- **Idioma del código:** identificadores (clases, funciones, propiedades, nombres de
  archivo) y comentarios/KDoc en **inglés**. Los **strings de cara al usuario** (mensajes
  de error, textos de validación, copy de pantalla) van en **español** — Yupana es un
  producto para revendedores hispanohablantes en Perú; es una decisión de producto, no
  de estilo de código.
- **No cambiar versiones** en `libs.versions.toml`/`build.gradle.kts` sin aprobación
  explícita — una versión desalineada rompe el build KMP (ver punto de verificar
  versiones más arriba).
- **Commit messages:** Conventional Commits (`feat:`, `fix:`, `refactor:`, `test:`,
  `docs:`, `chore:`), con scope entre paréntesis cuando aplique (`feat(auth): ...`).
- **Compilar/verificar en WSL:** ver `docs/DESARROLLO.md` (setup de JDK/SDK Linux propio,
  comandos de verificación, límite de `git push` en WSL).

### Flujo de ramas

- **`develop` es la rama de integración; `master` queda para releases.** Nada se
  commitea directo a ninguna de las dos. Todo trabajo nuevo va en una rama
  `feature/<slug-descriptivo>`, ramificada desde `develop` actualizado, con PR y
  merge de vuelta a `develop`. `master` se actualiza aparte (merge `develop` →
  `master`) cuando se corte una release; ese proceso no está definido todavía.
- **Gate manual antes de abrir PR** (no hay CI todavía): `./gradlew
  compileCommonMainKotlinMetadata` (o `:app:assembleDebug` si aplica) debe compilar
  limpio.
- **PR + merge:** push de la rama, abrir Pull Request en GitHub, revisar/aprobar uno
  mismo, fusionar con **merge commit (`--no-ff`, no squash, no rebase)** para conservar
  los commits atómicos de la rama. Borrar la rama tras fusionar.
- **Mapeo sugerido fase → rama** (ejemplo, no un nombre obligatorio — el slug lo decide
  quien empieza la fase):

  | Fase (ver arriba) | Rama sugerida |
  |---|---|
  | 4. Mis cuentas agrupado | `feature/cuentas-agrupadas` |
  | 5. Asignar perfil | `feature/asignar-perfil` |
  | 6. Mover integrante | `feature/mover-cliente` |
  | 7. Push/recordatorios | `feature/recordatorios-push` |

- **Fases grandes se pueden partir en varias ramas/PRs** (ej. `feature/cuentas-listado`
  y `feature/cuentas-expandir`) en vez de esperar a que toda la fase esté lista — cada
  una se fusiona cuando compile y funcione, sin bloquear el resto.
- **Recomendado (manual, vía GitHub Settings → Branches):** activar *branch protection*
  en `master` para exigir PR antes de mergear y bloquear push directo, reforzando la
  regla a nivel de plataforma y no solo por disciplina.

## 8. Convenciones de código (Compose Multiplatform / KMP)

- **Compose:**
  - `@Preview` obligatorio en composables **nuevos** de pantalla o componente reutilizable
    (import CMP: `org.jetbrains.compose.ui.tooling.preview.Preview`). Deuda conocida: las
    pantallas de auth (Login/Register/Splash) no lo tienen todavía; no se retrofittea
    como parte de esta regla, solo aplica hacia adelante.
  - Composables **stateless**: reciben estado y lambdas por parámetro, sin lógica de
    negocio dentro del composable.
  - `Modifier` como primer parámetro opcional (`modifier: Modifier = Modifier`),
    propagado al elemento raíz. Ya se cumple en `:core:designsystem`; las pantallas de
    auth aún no lo reciben (deuda conocida, no se toca ahora).
  - Naming para pantallas nuevas: `XxxScreen` (con Scaffold/TopBar si aplica) y `XxxContent`
    (cuerpo sin Scaffold, más fácil de previsualizar/testear) cuando la pantalla lo
    amerite. Componentes reutilizables en `component/`.
- **Strings/colores/dimensiones:** sin valores hardcodeados en código **nuevo**.
  - Strings vía Compose Resources (ya configurado en `:core:designsystem`:
    `compose.components.resources` + `src/commonMain/composeResources/values/strings.xml`).
    Deuda conocida: los strings de Login/Register/Splash siguen hardcodeados; no se migran
    como parte de esta regla.
  - Colores/dimensiones vía `YupanaTheme.colors`/`YupanaTheme.spacing` (patrón ya seguido
    en `:core:designsystem`). Deuda conocida: algunos magic numbers ya existentes
    (`YupanaTextField`, `YupanaButton`, `AuthScreenContainer`).
- **Arquitectura KMP:** límites `commonMain`/`androidMain`/`iosMain` estrictos; `expect/actual`
  solo para lo específico de plataforma. Koin con un módulo por feature (patrón ya seguido,
  ver `feature/auth/di/AuthModule.kt`). El dominio (UseCase/Repository) no fija un
  `Dispatcher`; es responsabilidad de quien lo consume (ViewModel).
- **Estado/errores:** patrón sealed `UiState`/`UiIntent`/`UiEvent` de `:core:mvi` +
  `Result<T>` para operaciones falibles (patrón ya seguido en `feature/auth`).
- **Documentación:** KDoc corto en `UseCase`/`Repository` públicos cuando la lógica no sea
  autoexplicativa.
- **Checklist antes de dar por terminada una tarea de UI/feature:**
  - [ ] `@Preview` agregado si el composable es nuevo
  - [ ] Sin strings/colores/dimensiones hardcodeados en el código nuevo
  - [ ] Sin secrets ni valores de configuración hardcodeados
  - [ ] `./gradlew compileCommonMainKotlinMetadata` compila limpio
