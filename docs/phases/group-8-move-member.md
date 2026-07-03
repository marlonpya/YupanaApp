# Grupo 8 · Mover integrante entre cuentas

**Objetivo:** implementar el "Mover a otra cuenta" que quedó como placeholder
"Próximamente" en el Detalle de asignación (Grupo 5). Marlon señaló que es una
operación cotidiana en su negocio.

**Depende de:** Fase 1, Fase 2, Grupos 1-5 (MVP completo).
**Prepara:** — (autocontenida).

## Estado

- Estado: ⬜ pendiente
- Iniciada: —
- Terminada: —
- Notas de ejecución: —

## Criterio de "hecho"

- [ ] Desde Detalle de asignación → "Mover a otra cuenta" (ya no placeholder) →
      pantalla de selección de cuenta destino.
- [ ] Solo se muestran cuentas del MISMO servicio con perfiles libres.
- [ ] Al confirmar, el `profile_id` de la asignación se actualiza; precio y
      vencimiento se conservan.
- [ ] Verificar visualmente: el perfil origen queda libre, el destino queda ocupado.
- [ ] Manejo del caso "no hay cuentas destino con cupo": mensaje claro.

## Prompt para Claude Code

```
Fase de construcción: GRUPO 8 — MOVER INTEGRANTE de Yupana.
Implementa la funcionalidad "mover a otra cuenta" que quedó como placeholder en el
Detalle de asignación (Grupo 5).

ANTES DE NADA:
- Lee el CLAUDE.md.
- Confirma que los Grupos 1-5 (MVP) compilan y funcionan, y que el placeholder
  "Próximamente" del botón "Mover a otra cuenta" existe en el Detalle de asignación.

═══ ALCANCE (1 pantalla) ═══
Mover integrante: pantalla que se abre desde el Detalle de asignación al tocar
"Mover a otra cuenta" (que deja de ser placeholder).
Contenido:
- Encabezado: "Mover a [nombre del cliente]" + resumen "desde [servicio] · [cuenta] ·
  [perfil actual]".
- Lista de cuentas DESTINO candidatas: solo cuentas del MISMO servicio del origen que
  tengan al menos un perfil libre. Cada item muestra: alias, día de corte, cuántos
  perfiles libres tiene. Cuentas sin cupo NO aparecen (o aparecen deshabilitadas con
  "Llena", tu criterio).
- Al elegir una cuenta destino, mostrar los perfiles libres de esa cuenta y elegir uno.
- Confirmar: "Se conserva el precio (S/. X) y el vencimiento actual. Solo cambia de cuenta."
- Botón "Mover" que ejecuta el cambio.
- Estado vacío: "No tienes otras cuentas de [servicio] con cupo. Considera crear una
  nueva cuenta." + CTA a Crear cuenta.

═══ DISEÑO ═══
Hay un mockup conceptual que se hizo antes en la conversación (bottom sheet + lista
de destinos), pero NO está en el diseño oficial de Claude Design. Constrúyela con los
componentes de :core:designsystem manteniendo consistencia visual con "Asignar perfil"
(Grupo 4).

═══ BACKEND (Supabase, sin cambios de esquema) ═══
La operación es un UPDATE de la fila de assignment: cambia profile_id al nuevo.
El índice único (un perfil solo puede tener una asignación activa) protege contra
carreras: si el perfil destino se ocupó entre la selección y el guardado, el update
fallará y hay que mostrar error claro.
No borrar la asignación ni crear una nueva: es un update de profile_id.

═══ ARQUITECTURA ═══
- Añade al módulo :feature:assign (o donde vive la lógica de asignación del Grupo 4):
  use case MoveAssignment(assignmentId, newProfileId), método en repositorio.
- Considera también un use case GetMoveCandidates(currentAssignmentId) que devuelva
  las cuentas destino candidatas ya filtradas (mismo servicio, con perfiles libres).
- MoveMemberScreen/Route/ViewModel/Contract con MVI.
- Navegación: Detalle de asignación → Mover integrante → seleccionar cuenta → seleccionar
  perfil libre → confirmar → vuelve a Detalle de asignación (o al Dashboard) con datos
  actualizados.
- Actualiza el botón "Mover a otra cuenta" del Detalle de asignación: quita el
  "Próximamente" y hazlo funcional.

═══ SIN TESTS ═══
No escribas tests.

═══ DISCIPLINA ═══
- Al terminar, se puede: entrar al detalle de una asignación → tocar "Mover a otra
  cuenta" → elegir cuenta y perfil destino → confirmar → verificar que el origen
  queda libre y el destino ocupado.
- Prueba el caso de error: intenta mover a un perfil que ocupas simultáneamente
  desde otra sesión (o simula la carrera).
- Commits: "feat(assign): move member usecase + repository",
  "feat(assign): move member ui + mvi",
  "feat(dashboard): enable move member action in assignment detail".

═══ AL FINALIZAR ═══
Muéstrame: (a) cómo probar el flujo feliz, (b) cómo se ve el estado vacío "no hay
cuentas con cupo", (c) cómo se maneja el error de perfil ocupado.
Detente y espera OK antes del Grupo 9.
```

## Referencias

- CLAUDE.md · sección 4 (tabla assignment con índice único).
- Grupos 4 y 5 (Asignar perfil y Detalle de asignación).
