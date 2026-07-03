# Grupo 4 · Asignar perfil

**Objetivo:** la acción central del negocio. Conectar un perfil libre con un cliente,
definiendo precio y vencimiento. Sola pantalla, dos puntos de entrada.

**Depende de:** Fase 1, Fase 2, Grupos 1, 2 y 3.
**Prepara:** Grupo 5 (Dashboard) mostrará estas asignaciones.

## Estado

- Estado: ✅ hecho
- Iniciada: 2026-07-01
- Terminada: 2026-07-01
- Notas de ejecución: commits `a8f69a5`…`f7f9f09`. Decisión de módulo: **`:feature:assignment`
  separado** (no dentro de accounts) — ver `settings.gradle.kts`. Incluye domain/usecase,
  repositorio Supabase, UI+MVI, y los dos puntos de entrada (desde detalle de cuenta y
  desde cero).

## Criterio de "hecho"

- [ ] Desde el Detalle de cuenta: tocar un perfil libre → completar formulario → asignar
      → volver y ver el perfil ocupado con ese cliente.
- [ ] Desde entrada general "Nueva asignación": completar todos los campos desde cero.
- [ ] Botón inline "+ Crear nuevo cliente" funciona sin salir del flujo.
- [ ] Si el perfil ya se ocupó entre selección y guardado, mensaje de error claro.
- [ ] Estructura del módulo (dentro de accounts o :feature:assign) documentada.

## Prompt para Claude Code

```
Fase de construcción: GRUPO 4 — ASIGNAR PERFIL de Yupana.
Cuarto grupo de pantallas reales. Es la acción central del negocio: vincular un perfil
libre con un cliente, definiendo precio y vencimiento.

ANTES DE NADA:
- Lee el CLAUDE.md (producto, backend Supabase, decisiones).
- Confirma que Fase 1, Fase 2, Grupo 1 (Auth), Grupo 2 (Clientes) y Grupo 3 (Cuentas)
  compilan. Este grupo depende de que ya existan cuentas con perfiles y clientes creados
  para poder probarse con datos reales.

═══ ALCANCE (1 pantalla, con 2 puntos de entrada) ═══
Asignar perfil: formulario paso a paso —
  1. Servicio/cuenta (selector; si se llega desde el Detalle de cuenta del Grupo 3,
     este campo viene PRE-LLENADO y no editable)
  2. Perfil disponible (selector; solo perfiles LIBRES de la cuenta elegida; si viene
     de un perfil específico tocado en el Detalle de cuenta, también pre-llenado)
  3. Cliente (selector de clientes existentes, con opción "+ Crear nuevo cliente"
     inline sin salir del flujo — reutiliza el use case CreateClient del Grupo 2)
  4. Precio a cobrar (numérico)
  5. Fecha de vencimiento (date picker)
Botón "Asignar perfil" al final, deshabilitado hasta que los campos obligatorios
(perfil, cliente, precio, fecha) estén completos.

Dos puntos de entrada a esta misma pantalla:
  A) Desde el Detalle de cuenta (Grupo 3): tocar un perfil libre → llega con
     cuenta+perfil ya elegidos, salta directo a elegir cliente.
  B) Desde un FAB o acción general "Nueva asignación": llega vacía, el admin elige
     todo desde cero.

═══ DISEÑO ═══
Esta pantalla SÍ está en el diseño de Claude Design importado ("04 · ASIGNAR PERFIL").
Constrúyela CON los componentes de :core:designsystem (YupanaTextField, YupanaButton,
selectores/dropdowns del design system), replicando la estructura visual del mockup:
header con flecha atrás + título, campos apilados con labels, botón "+ Crear nuevo
cliente" en terracota, botón principal "Asignar perfil" al final.
Si el diseño difiere en algún valor del design system, prioriza el design system y avísame.

═══ BACKEND (Supabase, tabla ya existe) ═══
Tabla `assignment`: id, owner_id, profile_id, client_id, price_charged, start_date
(default hoy), due_date, status (default 'active'), reminder_sent (default false).
RESTRICCIÓN CLAVE ya existe en la BD: un perfil solo puede tener UNA asignación activa
a la vez (índice único). Al guardar, si el insert falla por esa restricción (carrera
donde el perfil se ocupó entre que lo viste libre y guardaste), muestra un error claro
("este perfil ya no está disponible") y vuelve a la selección de perfil, no un error genérico.
Usa postgrest-kt: insert en `assignment`.
Para poblar "perfiles disponibles" de una cuenta: perfiles de esa `account` que NO
tengan una asignación con status='active' (reutiliza/extiende el use case de
disponibilidad que ya usaste en el Detalle de cuenta del Grupo 3, no lo dupliques).

═══ ARQUITECTURA (mismo patrón de los grupos anteriores) ═══
- Módulo :feature:assign (o dentro de :feature:accounts si prefieres cohesión, tu
  criterio, pero documenta cuál elegiste) con: domain/ (interfaz AssignmentRepository
  + modelo Assignment + use case AssignProfileToClient), data/ (DefaultAssignmentRepository
  con supabase-kt), ui/ (AssignScreen/Route/ViewModel/Contract), di/ (módulo Koin).
- MVI: UiState con los 5 campos del formulario + estados de carga de cada selector
  (cuentas, perfiles, clientes) + UiIntent por cada selección + UiEvent para navegar
  atrás al completar. Errores con Result + fold.
- Navegación: al asignar con éxito, vuelve al Detalle de cuenta (o a donde se originó)
  mostrando el perfil ya ocupado.
- Reutiliza CreateClient del Grupo 2 para el "+ Crear nuevo cliente" inline; no dupliques
  esa lógica.

═══ SIN TESTS ═══
No escribas tests en esta fase ni en ninguna. Prioriza que el flujo funcione end-to-end.

═══ DISCIPLINA ═══
- Al terminar, Android COMPILA y se puede: login → Detalle de cuenta → tocar un perfil
  libre → completar el formulario (crear un cliente nuevo inline en el camino) →
  asignar → volver y ver el perfil ahora ocupado con ese cliente. Contra tu Supabase real.
- Prueba también el segundo punto de entrada (asignación "desde cero").
- Commits atómicos en GitHub: "feat(assign): domain + usecase",
  "feat(assign): supabase repository", "feat(assign): ui + mvi",
  "feat(assign): entry points (desde detalle de cuenta y desde cero)".

═══ AL FINALIZAR ═══
Muéstrame: (a) cómo probar el flujo completo en Android, incluyendo el caso de error
de perfil ya ocupado, (b) dónde quedó el módulo (accounts o uno nuevo), (c) diferencias
diseño↔design system si las hubo.
Detente y espera mi OK antes del Grupo 5 (Dashboard) — el último del MVP.
```

## Referencias

- CLAUDE.md · sección 4 (tabla `assignment`, restricción de perfil único activo),
  sección 6 (pantalla 11).
