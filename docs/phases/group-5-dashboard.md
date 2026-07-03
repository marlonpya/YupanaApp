# Grupo 5 · Dashboard (+ Detalle de asignación)

**Objetivo:** cerrar el MVP. Ver de un vistazo qué vence y gestionar el cobro. Dos
pantallas: Dashboard real (reemplaza el placeholder del Grupo 1) y Detalle de asignación
con sus acciones.

**Depende de:** Fase 1, Fase 2, Grupos 1, 2, 3 y 4.
**Prepara:** — (último grupo del MVP).

## Estado

- Estado: 🟡 en curso
- Iniciada: 2026-07-01
- Terminada: —
- Notas de ejecución: commits `731c6eb`…`2ecd63d` en la rama `feature/dashboard`
  (domain+usecases, repositorio sobre la vista `upcoming_expirations`, UI+MVI del
  Dashboard, UI+MVI+acciones del detalle de asignación). Código de las 2 pantallas
  implementado, pero **la rama aún no está mergeada a `develop`** y no hay confirmación
  de prueba end-to-end contra Supabase real (renovar/editar/liberar). Pendiente: merge
  + validación manual antes de marcar ✅ hecho.

## Criterio de "hecho"

- [ ] Login → Dashboard real con las asignaciones creadas → métricas correctas.
- [ ] Tap en tarjeta → Detalle de asignación.
- [ ] "Marcar como cobrado / renovar" mueve la fecha y funciona contra Supabase real.
- [ ] "Editar precio o vencimiento" funciona.
- [ ] "Liberar perfil" cambia el estado (el perfil vuelve a estar libre en el Detalle
      de cuenta del Grupo 3).
- [ ] "Mover a otra cuenta" queda como placeholder deshabilitado ("Próximamente").
- [ ] Las 11 pantallas del MVP funcionan de punta a punta.

## Prompt para Claude Code

```
Fase de construcción: GRUPO 5 — DASHBOARD de Yupana (último grupo del MVP).
Cierra el ciclo completo: aquí se VE y se GESTIONA el resultado de todo lo construido
en los grupos anteriores (cuentas, clientes, asignaciones).

ANTES DE NADA:
- Lee el CLAUDE.md (producto, backend Supabase, decisiones).
- Confirma que Fase 1, Fase 2, y los Grupos 1-4 (Auth, Clientes, Cuentas, Asignar)
  compilan y tienen datos reales creados para poder probar este grupo con contenido real.

═══ ALCANCE (2 pantallas) ═══
1. Dashboard: reemplaza el placeholder actual (creado en el Grupo 1 como destino del
   Splash). Muestra:
   - Métricas: "Vencen hoy" (número), "Próximos 7 días" (número), "Por cobrar" (suma
     de price_charged de las asignaciones activas próximas).
   - Lista "Próximos a vencer": tarjetas con borde de color por urgencia (rojo=hoy,
     ámbar=pronto, verde=al día), logo del servicio, nombre del cliente,
     "Servicio · Perfil", precio, chip de días restantes.
   - FAB para "Nueva asignación" (entry point B del Grupo 4).
   - Tap en una tarjeta → Detalle de asignación.
2. Detalle de asignación: cliente, cuenta/servicio, perfil, precio, fecha de
   vencimiento, estado. Acciones:
   - "Marcar como cobrado / renovar": mueve due_date +1 mes (o +1 ciclo según
     billing_day de la cuenta, tu criterio) y resetea reminder_sent a false.
     Implementación REAL en este grupo.
   - "Editar precio o vencimiento": formulario simple, update de la asignación.
     Implementación REAL en este grupo.
   - "Liberar perfil (quitar cliente)": cambia status a 'cancelled' (o elimina,
     tu criterio, documenta cuál). Implementación REAL en este grupo.
   - "Mover a otra cuenta": SOLO placeholder/deshabilitado con texto "Próximamente" —
     es funcionalidad de Fase 2 de producto, NO la implementes completa aquí.

═══ DISEÑO ═══
El Dashboard SÍ está en el diseño de Claude Design importado ("01 · DASHBOARD").
Constrúyelo CON los componentes de :core:designsystem (YupanaStatCard,
YupanaAssignmentCard/YupanaListItem, YupanaChip, etc.), replicando su estructura:
saludo + avatar, fila de 3 métricas, sección "Próximos a vencer", FAB, bottom nav
con "Inicio" activo.
El Detalle de asignación NO tiene mockup: constrúyelo con los componentes del design
system manteniendo consistencia visual (puedes basarte en el bottom sheet de acciones
que se diseñó conceptualmente para "mover integrante", adaptándolo a un menú de
acciones sobre el detalle).
Si el diseño difiere del design system en algún valor, prioriza el design system y avísame.

═══ BACKEND (Supabase, vista ya existe) ═══
Usa la vista `upcoming_expirations` (ya creada, calcula days_left, junta
assignment+client+profile+account+service) vía postgrest-kt para el Dashboard.
Filtra/ordena por days_left para las métricas y la lista.
Para "renovar": update de `assignment` (due_date, reminder_sent).
Para "editar": update de `assignment` (price_charged y/o due_date).
Para "liberar": update de `assignment` (status).
Todas las escrituras respetan RLS automáticamente vía el token del usuario autenticado.

═══ ARQUITECTURA (mismo patrón de los grupos anteriores) ═══
- Módulo :feature:dashboard con: domain/ (interfaz DashboardRepository o reutiliza
  AssignmentRepository si ya existe del Grupo 4 + use cases: GetUpcomingExpirations,
  RenewAssignment, EditAssignment, LiberateProfile), data/ (implementación con
  supabase-kt sobre la vista y la tabla), ui/ (DashboardScreen/Route/ViewModel/Contract,
  AssignmentDetailScreen/Route/ViewModel/Contract), di/ (módulo Koin).
- MVI en cada pantalla: UiState/UiIntent/UiEvent, ViewModel extiende MviViewModel.
  Errores con Result + fold.
- Navegación: Dashboard (tab Inicio, ahora real) → tap tarjeta → Detalle de asignación
  → acciones → vuelve al Dashboard con los datos actualizados (recarga o actualización
  optimista, tu criterio).
- Reemplaza cualquier placeholder de Dashboard que haya quedado del Splash (Grupo 1)
  por esta implementación real.
- Estados: loading (skeletons en las tarjetas), vacío ("aún no tienes asignaciones
  próximas" con CTA a Nueva asignación), error — con los componentes del design system.

═══ SIN TESTS ═══
No escribas tests en esta fase ni en ninguna. Prioriza que el flujo funcione end-to-end.

═══ DISCIPLINA ═══
- Al terminar, Android COMPILA y se puede: login → caer en Dashboard real con las
  asignaciones creadas en el Grupo 4 → ver las métricas correctas → entrar al detalle
  de una → marcar como cobrado (ver que la fecha cambia) → editar precio → liberar un
  perfil (ver que vuelve a estar libre en el Detalle de cuenta del Grupo 3).
  Contra tu Supabase real.
- Este grupo CIERRA el MVP completo (11 pantallas). Al terminar, la app debe ser
  usable de punta a punta por ti en tu operación real.
- Commits atómicos en GitHub: "feat(dashboard): domain + usecases",
  "feat(dashboard): supabase repository (upcoming_expirations)",
  "feat(dashboard): dashboard ui + mvi",
  "feat(dashboard): assignment detail ui + mvi + acciones".

═══ AL FINALIZAR ═══
Muéstrame: (a) cómo probar el flujo completo en Android (incluyendo renovar/editar/
liberar), (b) confirmación de que las 11 pantallas del MVP funcionan de punta a punta,
(c) diferencias diseño↔design system si las hubo en el Dashboard.
Este es el ÚLTIMO grupo del MVP — no hay Grupo 6 pendiente. Al terminar, resume el
estado completo del proyecto y qué queda para Fase 2 de producto (Mover integrante,
Todos los vencimientos con filtros, Recuperar contraseña, Detalle de cliente, Ajustes)
y Fase 3 (Catálogo avanzado, Preferencias de notificación, Push/recordatorios).
```

## Referencias

- CLAUDE.md · sección 4 (vista `upcoming_expirations`, tabla `assignment`),
  sección 6 (pantallas 5 y 7).
