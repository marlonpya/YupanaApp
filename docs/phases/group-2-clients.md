# Grupo 2 · Clientes (Lista + Crear/editar)

**Objetivo:** segundo grupo de pantallas reales. Inventario simple, sin dependencias
cruzadas. Establece el patrón de features que se repetirá en los grupos siguientes.

**Depende de:** Fase 1, Fase 2, Grupo 1 (necesita sesión).
**Prepara:** Grupo 4 (Asignar) usará clientes existentes.

## Estado

- Estado: ⬜ pendiente
- Iniciada: —
- Terminada: —
- Notas de ejecución: —

## Criterio de "hecho"

- [ ] Login → tab Clientes → crear cliente → verlo en la lista → editarlo → cambio reflejado.
- [ ] Bottom nav de 4 pestañas creado (Cuentas y Ajustes pueden ser placeholders).
- [ ] Estructura `:feature:clients` consistente con el patrón del Grupo 1.
- [ ] Estados vacío/loading/error usan componentes del design system.

## Prompt para Claude Code

```
Fase de construcción: GRUPO 2 — CLIENTES de Yupana.
Segundo grupo de pantallas reales, sobre la base ya validada por el Grupo 1 (Auth).

ANTES DE NADA:
- Lee el CLAUDE.md (producto, backend Supabase, decisiones).
- Confirma que Fase 1, Fase 2 (:core:designsystem) y Grupo 1 (:feature:auth) compilan
  y que el login funciona. Este grupo requiere sesión activa (RLS por owner_id).

═══ ALCANCE (solo estas 2 pantallas) ═══
1. Lista de clientes: todos los clientes del admin autenticado, con búsqueda por nombre.
   Cada ítem muestra nombre, contacto, y opcionalmente cuántas asignaciones activas
   tiene (puede quedar en 0 si esa relación se resuelve en un grupo posterior; no
   bloquea esta fase).
2. Crear / editar cliente: formulario con nombre (obligatorio), contacto/WhatsApp
   (opcional), notas (opcional). Un mismo formulario sirve para crear y editar
   (según venga o no un id).

═══ SIN REFERENCIA DE DISEÑO EN CLAUDE DESIGN ═══
Estas 2 pantallas NO están en el diseño de Claude Design (que solo cubre Dashboard,
Login/Registro, Mis Cuentas, Asignar Perfil). Constrúyelas usando directamente los
componentes y tokens de :core:designsystem (YupanaTopBar, YupanaCard, YupanaListItem,
YupanaTextField, YupanaButton, EmptyState si la lista está vacía), manteniendo la
misma identidad visual "oscuro andino" del resto de la app. Estado vacío obligatorio:
"Aún no tienes clientes" + botón para crear el primero.

═══ BACKEND (Supabase, tabla ya existe) ═══
Tabla `client`: id, owner_id (default auth.uid(), RLS ya configurado), name, contact,
notes, created_at. Usa postgrest-kt:
- Listar: from("client").select() ordenado por name.
- Buscar: filtro ilike sobre name.
- Crear: insert.
- Editar: update por id.
No hay borrado lógico definido aún; si agregas eliminar, usa delete real y confírmame
si prefieres soft-delete en el futuro.

═══ ARQUITECTURA (mismo patrón del Grupo 1) ═══
- Módulo :feature:clients con: domain/ (interfaz ClientRepository + modelo Client +
  use cases: GetClients, SearchClients, CreateClient, UpdateClient),
  data/ (DefaultClientRepository con supabase-kt postgrest),
  ui/ (ClientListScreen/Route/ViewModel/Contract, ClientFormScreen/Route/ViewModel/Contract),
  di/ (módulo Koin).
- MVI: UiState/UiIntent/UiEvent por pantalla, ViewModel extiende MviViewModel de
  :core:mvi. Errores con Result + fold, mostrados con los estados del design system.
- Navegación: lista → FAB → formulario (modo crear); lista → item → formulario
  (modo editar, precargado). Volver a la lista tras guardar, con el nuevo/editado
  cliente reflejado (recarga o actualización optimista, tu criterio).
- Validación de formulario: nombre no vacío antes de habilitar "Guardar".

═══ SIN TESTS ═══
No escribas tests en esta fase ni en ninguna. Prioriza que el flujo funcione end-to-end.

═══ DISCIPLINA ═══
- Al terminar, Android COMPILA y se puede: login → ir a Clientes (nueva pestaña o
  navegación temporal si el bottom nav completo aún no existe) → crear un cliente →
  verlo en la lista → editarlo → ver el cambio reflejado. Contra tu Supabase real.
- Si el bottom nav de 4 pestañas (Inicio/Cuentas/Clientes/Ajustes) no existe todavía,
  créalo ahora de forma mínima (aunque Cuentas/Ajustes queden con placeholder), porque
  es la navegación principal de la app y Clientes ya necesita su pestaña real.
- Commits atómicos en GitHub: "feat(clients): domain + usecases",
  "feat(clients): supabase repository", "feat(clients): list ui + mvi",
  "feat(clients): form ui + mvi", "feat(clients): bottom nav".

═══ AL FINALIZAR ═══
Muéstrame: (a) cómo probar el flujo completo en Android, (b) estructura final de
:feature:clients, (c) cómo quedó el bottom nav.
Detente y espera mi OK antes del Grupo 3 (Cuentas).
```

## Referencias

- CLAUDE.md · sección 4 (tabla `client`), sección 6 (pantallas 14 y 16).
