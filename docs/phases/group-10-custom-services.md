# Grupo 10 · Catálogo de servicios avanzado (agregar propios)

**Objetivo:** completar el catálogo híbrido diseñado desde el inicio. El Grupo 3
ya muestra los 7 globales y expone el catálogo como picker; falta habilitar el
"+ Agregar servicio propio" con su formulario y persistencia.

**Depende de:** Fase 1, Fase 2, Grupo 3 (Cuentas).
**Prepara:** — (autocontenida).

## Estado

- Estado: ⬜ pendiente
- Iniciada: —
- Terminada: —
- Notas de ejecución: —

## Criterio de "hecho"

- [ ] Desde Catálogo → "+ Agregar servicio" → formulario funciona.
- [ ] Servicios propios (owner_id = mi user_id) aparecen junto a los globales.
- [ ] Puedo editar/eliminar mis servicios propios (no los globales).
- [ ] Al eliminar: si hay cuentas asociadas, mensaje claro (por FK restrict).
- [ ] RLS se respeta: no puedo ver ni tocar servicios propios de otros admins.

## Prompt para Claude Code

```
Fase de construcción: GRUPO 10 — CATÁLOGO DE SERVICIOS AVANZADO.

ANTES DE NADA:
- Lee el CLAUDE.md.
- Confirma que el Grupo 3 (Cuentas) compila y funciona. El catálogo ya muestra los 7
  globales; aquí completamos el "híbrido" agregando servicios propios.

═══ ALCANCE (mejoras a 1 pantalla + formulario nuevo) ═══
1. Catálogo de servicios (mejora):
   - Sección "Servicios globales" (7 pre-cargados, solo lectura, no editables).
   - Sección "Mis servicios" (los que yo he creado, con acciones editar/eliminar).
   - Botón "+ Agregar servicio" bien visible.

2. Formulario Crear/Editar servicio propio:
   - Campos: nombre (obligatorio), color (color picker o selección de una paleta
     predefinida de colores compatibles con el design system), logo (opcional; para
     MVP+ puede ser solo un color + inicial, sin subida de imagen).
   - Validación: nombre único entre los propios del usuario (la BD ya tiene un índice
     único por owner_id + lower(name); manejar el error si el usuario intenta duplicar).
   - Botón "Guardar".

═══ DISEÑO ═══
No hay mockup en Claude Design. Reutiliza YupanaTextField, YupanaButton, YupanaCard, y
crea un componente ColorPicker/ColorSwatch si no existe (paleta acotada mejor que
selector libre, para mantener coherencia visual).

═══ BACKEND (Supabase, tabla ya existe con RLS híbrido) ═══
- Tabla streaming_service: RLS ya permite ver globales (owner_id IS NULL) + propios
  (owner_id = auth.uid()), y solo insertar/editar/eliminar propios.
- Al insertar, el default de owner_id ya es auth.uid() (documentado en el CLAUDE.md).
  Si por alguna razón el schema aplicado no tiene ese default, ajústalo o setéalo
  explícitamente al insertar.
- Al eliminar: la FK de account.service_id tiene ON DELETE RESTRICT → si hay cuentas
  usando ese servicio, Postgres rechaza. Captura ese error y muestra mensaje claro:
  "No puedes eliminar este servicio porque tienes cuentas usándolo. Elimina o cambia
  esas cuentas primero."

═══ ARQUITECTURA ═══
- Añade al módulo :feature:accounts (donde vive ServiceRepository del Grupo 3):
  use cases CreateCustomService, UpdateCustomService, DeleteCustomService.
- ServiceFormScreen/Route/ViewModel/Contract (crear/editar).
- Mejora CatalogScreen para mostrar las dos secciones y las acciones sobre los
  propios.

═══ SIN TESTS ═══
No escribas tests.

═══ DISCIPLINA ═══
- Al terminar, se puede: entrar al catálogo → agregar "HBO Local" con color propio →
  usarlo al crear una cuenta → intentar eliminarlo mientras hay cuentas usándolo
  (ver mensaje de error) → eliminar las cuentas → eliminar el servicio.
- Commits: "feat(accounts): custom service usecases",
  "feat(accounts): service form ui + mvi",
  "feat(accounts): catalog with global + own sections".

═══ AL FINALIZAR ═══
Muéstrame: (a) cómo probar el flujo feliz, (b) cómo se ve el error de "no puedes
eliminar porque hay cuentas", (c) qué decidiste sobre el logo (solo color+inicial o
subida de imagen).
Detente y espera OK antes del Grupo 11 (push/recordatorios).
```

## Referencias

- CLAUDE.md · sección 4 (tabla streaming_service, catálogo híbrido, RLS híbrido).
- Grupo 3 (donde ya vive el catálogo básico y ServiceRepository).
