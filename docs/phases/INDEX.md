# Yupana · Plan de fases

> Mapa maestro de la construcción de Yupana. Cada fase es un archivo autocontenido en
> esta carpeta con su prompt ejecutable, criterios de "hecho" y estado. Este índice
> refleja el progreso global y sirve como punto de entrada.

**Paquete base:** `com.strawtechberry.yupana`
**Repo:** github.com/marlonpya/YupanaApp
**Contexto permanente del producto:** ver `/CLAUDE.md` en la raíz del proyecto.

---

## Cómo usar estos documentos

- Antes de arrancar cualquier fase, lee su archivo completo y el `CLAUDE.md` de la raíz.
- Ejecuta las fases **en orden**. Cada una asume que las anteriores compilan y funcionan.
- Al terminar una fase: actualiza su bloque `## Estado` en el archivo y este índice.
- Los prompts están escritos para pegar en Claude Code apuntando al repo `YupanaApp`.
- **No tests en ningún prompt** — decisión del proyecto para priorizar velocidad de MVP.

---

## Estado global — Producto completo (19 pantallas + push)

### Bloque A · Cimientos técnicos

| # | Fase | Archivo | Estado |
|---|---|---|---|
| 1 | Esqueleto + librerías | `phase-1-scaffold.md` | ⬜ pendiente |
| 2 | Design System | `phase-2-designsystem.md` | ⬜ pendiente |

### Bloque B · MVP (11 pantallas, usable de punta a punta)

| # | Grupo | Archivo | Pantallas | Estado |
|---|---|---|---|---|
| 3 | Auth | `group-1-auth.md` | Splash, Login, Registro | ⬜ pendiente |
| 4 | Clientes | `group-2-clients.md` | Lista, Crear/Editar | ⬜ pendiente |
| 5 | Cuentas | `group-3-accounts.md` | Mis cuentas, Detalle, Crear/Editar, Catálogo | ⬜ pendiente |
| 6 | Asignar perfil | `group-4-assign.md` | Asignar | ⬜ pendiente |
| 7 | Dashboard | `group-5-dashboard.md` | Dashboard, Detalle asignación | ⬜ pendiente |

### Bloque C · Producto completo (las 8 pantallas restantes + push)

| # | Grupo | Archivo | Pantallas | Estado |
|---|---|---|---|---|
| 8 | Recuperar contraseña | `group-6-reset-password.md` | Recuperar | ⬜ pendiente |
| 9 | Vistas ampliadas | `group-7-detailed-views.md` | Detalle cliente, Todos vencimientos | ⬜ pendiente |
| 10 | Mover integrante | `group-8-move-member.md` | Mover | ⬜ pendiente |
| 11 | Ajustes | `group-9-settings.md` | Ajustes, Preferencias notif., Mi cuenta | ⬜ pendiente |
| 12 | Catálogo avanzado | `group-10-custom-services.md` | Catálogo con propios | ⬜ pendiente |
| 13 | Push y recordatorios | `group-11-push-reminders.md` | (fase técnica, sin pantalla nueva) | ⬜ pendiente |

**Leyenda:** ⬜ pendiente · 🟡 en curso · ✅ hecho · ⚠️ bloqueada

**Total:** 19 pantallas + motor de recordatorios push (13 fases de construcción).

---

## Fuera de este plan (no planificar aún)

Estos bloques son **necesarios para publicar** pero no forman parte del producto en sí.
Se planifican cuando el producto esté funcional de punta a punta.

**Hardening previo a publicar en tiendas:** flavors por ambiente (dev/staging/prod),
R8/minify activo, linters (detekt/ktlint), CI/CD (GitHub Actions con builds automáticos
y firma), revisión de secrets, HTTPS estricto, política de privacidad y términos.

**Activación iOS:** el target ya está estructuralmente listo desde Fase 1 y el push
tiene TODOs de APNs en el Grupo 11. Aquí se resuelve: probar compilación iOS,
Keychain para sesión, APNs completo, engine Darwin, shell del `iosApp`, publicación
en App Store.

---

## Convenciones que se respetan en todos los prompts

- **Sin tests** en ninguna fase.
- **Commits atómicos** con mensajes convencionales (`feat(scope): …`, `chore: …`).
- **Checkpoint tras la matriz de versiones** en la Fase 1 (se detiene antes de escribir builds).
- **Secrets fuera del repo** desde el commit 1 (`secrets.properties` no versionado).
- **Diseño con components del design system**, no reproducciones independientes.
- **Reconciliación diseño ↔ design system**: si algo difiere, gana el design system y se avisa.
- **Todas las escrituras a Supabase respetan RLS** vía sesión autenticada (nunca `service_role`).
- **Actualización del estado** en el archivo de cada fase y en este INDEX al terminar.

---

## Recomendación de orden y ritmo

El **Bloque B (MVP)** te da una app usable en tu operación real. Es recomendable, aunque
no obligatorio, usarla 1-2 semanas antes de arrancar el Bloque C, para que tu experiencia
real informe las prioridades del bloque final. Si tu ritmo es bueno y no quieres cortar,
puedes seguir directo al Bloque C.

El **Grupo 11 (Push)** es la fase más independiente técnicamente pero depende del
Grupo 9 (Preferencias de notificación). El orden dentro del Bloque C se puede reordenar
según tus prioridades reales, con estas dependencias mínimas:
- Grupo 8 (Mover) depende del Grupo 5.
- Grupo 9 (Ajustes) es requisito para el Grupo 11.
- Grupo 10 (Catálogo avanzado) depende del Grupo 3.
- Grupo 6, 7 son autocontenidos.
