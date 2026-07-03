# Grupo 3 · Cuentas (Mis cuentas + Detalle + Crear + Catálogo)

**Objetivo:** el inventario de suscripciones. Cuatro pantallas que resuelven cómo el
admin registra sus cuentas de streaming y navega su catálogo.

**Depende de:** Fase 1, Fase 2, Grupos 1 y 2.
**Prepara:** Grupo 4 (Asignar) usa las cuentas y perfiles creados aquí.

## Estado

- Estado: ✅ hecho
- Iniciada: 2026-07-01
- Terminada: 2026-07-01
- Notas de ejecución: commits `cf7e70f`…`9e2a266`. Módulo `:feature:accounts` con
  catálogo de servicios, crear/editar cuenta, mis cuentas agrupado por servicio
  (expandir/colapsar) y detalle de cuenta con perfiles libres/ocupados. Wiring en
  bottom nav/NavHost.

## Criterio de "hecho"

- [ ] Login → tab Cuentas → catálogo muestra los 7 servicios globales.
- [ ] Crear cuenta (ej. Netflix) → aparece en Mis Cuentas agrupada por servicio.
- [ ] Entrar a detalle → ver perfiles libres (sin asignación aún).
- [ ] Búsqueda por alias/servicio funciona.
- [ ] Estados vacío/loading/error usan componentes del design system.

## Prompt para Claude Code

```
Fase de construcción: GRUPO 3 — CUENTAS de Yupana.
Tercer grupo de pantallas reales, sobre la base ya validada por Auth y Clientes.

ANTES DE NADA:
- Lee el CLAUDE.md (producto, backend Supabase, decisiones, sección de pantallas).
- Confirma que Fase 1, Fase 2, Grupo 1 (Auth) y Grupo 2 (Clientes) compilan y el bottom
  nav de 4 pestañas existe.

═══ ALCANCE (4 pantallas) ═══
1. Mis cuentas: lista AGRUPADA POR SERVICIO con expandir/colapsar. Encabezado de grupo:
   logo del servicio, nombre, resumen "N cuentas · X/Y perfiles ocupados", badge con
   el número de cuentas, chevron que rota al expandir. Al expandir: cada cuenta con su
   alias, día de corte, costo mensual, y una barra de ocupación de perfiles con color
   (verde=cupo, ámbar=casi llena, rojo=llena) + texto "4/5". Búsqueda por alias/servicio.
   IMPORTANTE: NO uses la versión plana de "Mis cuentas" que pueda existir en el diseño
   de Claude Design importado — esa fue una iteración temprana. La versión correcta es
   la AGRUPADA descrita arriba (fue la decisión final validada).
2. Detalle de cuenta: una cuenta con la lista de sus perfiles, cada uno mostrando si
   está libre o el cliente que lo ocupa (nombre, precio, vencimiento si está asignado).
   Acceso a "Asignar perfil" desde aquí para los perfiles libres (el flujo real de
   asignación es el Grupo 4; por ahora solo deja el punto de navegación/CTA).
3. Crear / editar cuenta: formulario con selector de servicio (del catálogo: globales +
   propios del admin), alias, costo mensual, número de perfiles, día de corte, notas.
4. Catálogo de servicios: lista de servicios disponibles (los 7 globales pre-cargados +
   los propios del admin si tiene). Botón "+ Agregar servicio" para crear uno propio
   (nombre, color). Esta pantalla se abre normalmente desde el selector de servicio del
   formulario de crear cuenta (como un picker), y también es accesible sola desde Ajustes
   o desde Mis Cuentas.

═══ DISEÑO ═══
Importa/usa el diseño de Claude Design como referencia SOLO para el layout general de
"Mis cuentas" (adaptándolo a la versión agrupada descrita arriba, no a la plana si
difiere). Las otras 3 pantallas de este grupo no tienen mockup: constrúyelas con los
componentes de :core:designsystem manteniendo consistencia visual.
Regla de siempre: construir CON los componentes del design system (YupanaCard,
YupanaChip, YupanaButton, etc.), no reproducir el diseño de forma independiente.
Si hay diferencias de valores entre diseño y design system, prioriza el design system
y avísame.

═══ BACKEND (Supabase, tablas ya existen) ═══
- `streaming_service`: id, owner_id (NULL=global, o del admin), name, color, logo_url,
  is_global. RLS: SELECT ve global+propios; INSERT solo propios (owner_id=auth.uid());
  UPDATE/DELETE solo propios.
- `account`: id, owner_id, service_id, alias, monthly_cost, num_profiles, billing_day, notes.
- `profile`: id, owner_id, account_id, label, pin.
Para "X/Y perfiles ocupados" por cuenta: cuenta perfiles vs. asignaciones activas
ligadas a esos perfiles (join con `assignment` where status='active'; si `assignment`
aún no tiene datos reales de este grupo, maneja el caso de 0 ocupados sin fallar).
Usa postgrest-kt para todo. Agrupar por servicio se hace en el dominio/presentación
(groupBy sobre service_id), NO es una tabla ni una vista nueva.

═══ ARQUITECTURA (mismo patrón de los grupos anteriores) ═══
- Módulo :feature:accounts con: domain/ (interfaces AccountRepository, ProfileRepository,
  ServiceRepository + modelos Account, Profile, StreamingService + use cases:
  GetAccountsGroupedByService, GetAccountDetail, CreateAccount, UpdateAccount,
  GetAvailableServices, CreateCustomService), data/ (Default*Repository con
  supabase-kt), ui/ (una Screen/Route/ViewModel/Contract por cada una de las 4
  pantallas), di/ (módulo Koin).
- MVI en cada pantalla: UiState/UiIntent/UiEvent, ViewModel extiende MviViewModel.
  Errores con Result + fold.
- Navegación: Mis cuentas (tab) → tap en cuenta → Detalle de cuenta. FAB en Mis
  cuentas → Crear cuenta. Selector de servicio en el formulario → Catálogo de
  servicios (modo picker) → vuelve con el servicio elegido.
- Estados: loading, vacío ("aún no tienes cuentas" + CTA crear), error — con los
  componentes del design system.

═══ SIN TESTS ═══
No escribas tests en esta fase ni en ninguna. Prioriza que el flujo funcione end-to-end.

═══ DISCIPLINA ═══
- Al terminar, Android COMPILA y se puede: login → tab Cuentas → ver catálogo de
  servicios → crear una cuenta (ej. Netflix) → verla agrupada en Mis Cuentas → entrar
  a su detalle → ver sus perfiles libres. Contra tu Supabase real (los 7 servicios
  globales ya deben aparecer).
- Commits atómicos en GitHub: "feat(accounts): domain + usecases",
  "feat(accounts): supabase repositories", "feat(accounts): catálogo de servicios ui",
  "feat(accounts): crear/editar cuenta ui", "feat(accounts): mis cuentas agrupado ui",
  "feat(accounts): detalle de cuenta ui".

═══ AL FINALIZAR ═══
Muéstrame: (a) cómo probar el flujo completo en Android, (b) estructura final de
:feature:accounts, (c) diferencias diseño↔design system si las hubo en Mis Cuentas.
Detente y espera mi OK antes del Grupo 4 (Asignar perfil).
```

## Referencias

- CLAUDE.md · sección 4 (tablas `streaming_service`, `account`, `profile`),
  sección 6 (pantallas 8, 9, 10, 13).
