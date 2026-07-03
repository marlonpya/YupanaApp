# Grupo 7 · Vistas ampliadas (Detalle de cliente + Todos los vencimientos)

**Objetivo:** dar profundidad de consulta al MVP. Dos pantallas que amplían lo que
ya se ve resumido en listas: ver "todo lo de un cliente" y "todos los vencimientos
con filtros".

**Depende de:** Fase 1, Fase 2, Grupos 1-5 (MVP completo).
**Prepara:** — (autocontenidas).

## Estado

- Estado: ⬜ pendiente
- Iniciada: —
- Terminada: —
- Notas de ejecución: —

## Criterio de "hecho"

- [ ] Desde Lista de clientes → tap → Detalle de cliente con sus datos + servicios
      ocupados + vencimientos.
- [ ] Desde Dashboard → "Ver todos" → pantalla completa con filtros funcionales
      (hoy / semana / mes / vencidos / todos).
- [ ] Ambas pantallas reutilizan use cases y repositorios existentes.
- [ ] Estados vacío/loading/error consistentes con el resto de la app.

## Prompt para Claude Code

```
Fase de construcción: GRUPO 7 — VISTAS AMPLIADAS (Detalle de cliente + Todos los
vencimientos).

ANTES DE NADA:
- Lee el CLAUDE.md.
- Confirma que los Grupos 1-5 (MVP) compilan y funcionan.

═══ ALCANCE (2 pantallas) ═══
1. Detalle de cliente:
   - Encabezado: nombre, contacto (WhatsApp/tel con acción de abrir chat/llamar),
     notas.
   - Sección "Suscripciones activas": lista de asignaciones donde este cliente es
     titular, mostrando servicio, cuenta, perfil, precio, próxima fecha.
   - Sección "Historial" (opcional, si es fácil con los datos actuales): asignaciones
     pasadas (status='cancelled'). Si no es trivial, omitir y anotar.
   - Botones: Editar cliente (reutiliza ClientFormScreen del Grupo 2 en modo edición).
   - Acceso desde: Lista de clientes → tap en un item.

2. Todos los vencimientos:
   - Encabezado con filtros como chips o tabs: "Hoy", "Próximos 7 días", "Este mes",
     "Vencidos", "Todos".
   - Lista con el mismo componente de tarjeta del Dashboard (YupanaAssignmentCard).
   - Búsqueda opcional por nombre de cliente.
   - Acceso desde: Dashboard → "Ver todos" (link que ya existe en el diseño).

═══ DISEÑO ═══
No hay mockup específico para estas pantallas en Claude Design. Constrúyelas con los
componentes de :core:designsystem, respetando la estética del Dashboard y de la lista
de clientes. Reutiliza al máximo (YupanaAssignmentCard, YupanaChip para los filtros,
YupanaCard para las secciones del detalle).

═══ BACKEND (Supabase, sin cambios de esquema) ═══
- Detalle de cliente: consulta el cliente por id + join con assignment donde
  client_id = ese cliente. Usa la vista upcoming_expirations filtrando por client_id
  para las suscripciones activas (evita reimplementar el join).
- Todos los vencimientos: usa la vista upcoming_expirations con distintos filtros
  sobre days_left:
  - Hoy: days_left = 0
  - Próximos 7 días: days_left BETWEEN 0 AND 7
  - Este mes: days_left BETWEEN 0 AND 30
  - Vencidos: days_left < 0
  - Todos: sin filtro

═══ ARQUITECTURA ═══
- Añade a :feature:clients: use case GetClientDetail (cliente + sus asignaciones),
  ClientDetailScreen/Route/ViewModel/Contract.
- Añade a :feature:dashboard: use case GetAllExpirations(filter), 
  AllExpirationsScreen/Route/ViewModel/Contract.
- Reutiliza repositorios y modelos existentes; no dupliques lógica de datos.
- Navegación: ClientListScreen → ClientDetailScreen (con id). 
  DashboardScreen → AllExpirationsScreen (con filtro inicial opcional).

═══ SIN TESTS ═══
No escribas tests.

═══ DISCIPLINA ═══
- Al terminar, se puede: entrar al detalle de un cliente y ver todo lo que tiene
  contigo; ir a "todos los vencimientos" y filtrar por vencidos o por semana.
- Commits: "feat(clients): client detail", "feat(dashboard): all expirations with filters".

═══ AL FINALIZAR ═══
Muéstrame: (a) cómo probar cada pantalla, (b) qué decidiste sobre la sección de
"Historial" en el Detalle de cliente, (c) si algún filtro requirió lógica especial.
Detente y espera OK antes del Grupo 8.
```

## Referencias

- CLAUDE.md · sección 4 (vista upcoming_expirations, tablas client y assignment).
- Grupos 2 y 5 para reutilización de componentes y use cases.
