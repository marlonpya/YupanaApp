# Fase 2 · Design System

**Objetivo:** construir `:core:designsystem` (Compose Multiplatform) con tokens y
componentes reutilizables, para que todas las pantallas futuras se construyan sobre una
base visual consistente.

**Depende de:** Fase 1 (esqueleto compilando).
**Prepara:** todos los grupos de features.

## Estado

- Estado: ✅ hecho
- Iniciada: 2026-06-30
- Terminada: 2026-06-30
- Notas de ejecución: commits `267b0f7`…`7e09b41`. Módulo `:core:designsystem` (KMP
  library) con tokens, `YupanaTheme`, componentes base, estados (empty/loading/error)
  y `DesignSystemShowcase`. Compose Resources configurado (`b63aef1`).

## Criterio de "hecho"

- [ ] Módulo `:core:designsystem` compila en Android.
- [ ] Tokens (`YupanaColors`, `YupanaTypography`, `YupanaShapes`, `YupanaSpacing`,
      `YupanaElevation`) creados con los valores exactos del CLAUDE.md.
- [ ] `YupanaTheme` provee los tokens vía `MaterialTheme` + CompositionLocals.
- [ ] Componentes base creados: `YupanaButton` (con variantes), `YupanaTextField`,
      `YupanaCard`, `YupanaChip`, `YupanaTopBar`, `YupanaBottomNav`, `YupanaServiceLogo`,
      `YupanaStatCard`, `YupanaListItem`, `YupanaAvatar`.
- [ ] Estados transversales creados: `EmptyState`, `LoadingState` (skeletons/shimmer),
      `ErrorState`.
- [ ] Pantalla `DesignSystemShowcase` renderiza todo junto en Android.
- [ ] Cero valores hardcodeados fuera del módulo (verificar).

## Prompt para Claude Code

```
Fase 2 de Yupana: construir el módulo :core:designsystem en Compose Multiplatform.
El design system NACE del CLAUDE.md (fuente de verdad de los tokens) y CONSIDERA los
componentes UI que se usan en el diseño de Yupana para definir qué componentes crear.

ANTES DE NADA:
- Lee el CLAUDE.md (sección 5, identidad visual "oscuro andino"). De AHÍ salen los
  valores exactos de los tokens (colores, etc.).
- Confirma que la Fase 1 compila. Este módulo se integra al proyecto existente.

═══ PRINCIPIO RECTOR ═══
- Los VALORES (colores, tipografía, formas, spacing) salen del CLAUDE.md, NO de ningún
  diseño externo. El CLAUDE.md es la fuente de verdad.
- El diseño de Claude Design (4 pantallas: Dashboard, Login/Registro, Mis Cuentas,
  Asignar Perfil) sólo sirve como REFERENCIA de QUÉ componentes existen. NO importes
  el diseño en esta fase; los componentes están listados abajo. Claude Design se
  adaptará a este design system en las fases de pantallas, no al revés.

═══ QUÉ CREAR en :core:designsystem (commonMain, Compose Multiplatform) ═══

1. TOKENS con los valores EXACTOS del CLAUDE.md:
   - YupanaColors: fondo #15110E, superficie #1E1813, superficie elevada #271F18,
     marca/terracota #C8643C, acento/dorado #E0A458, texto #F3E9DF, texto secundario
     #B8A793, líneas #322820, estados éxito #6FBF8E / alerta #E0A458 / peligro #E5594E.
   - YupanaTypography: escala (títulos, cuerpo, labels, captions) con Roboto/Roboto Flex.
   - YupanaShapes: radios (cards ~18px, botones/chips pill).
   - YupanaSpacing: escala 4/8/12/16/20/24...
   - YupanaElevation / bordes.

2. TEMA: un YupanaTheme { } que provea todos los tokens vía MaterialTheme +
   CompositionLocals. Tema oscuro como default (la app es dark-first).

3. COMPONENTES base reutilizables (los que se repiten en las 4 pantallas de referencia):
   - YupanaButton (primario terracota; + variantes secundario, texto, destructivo)
   - YupanaTextField (outlined, dark)
   - YupanaCard
   - YupanaChip (con estados de color: hoy/pronto/al día)
   - YupanaTopBar
   - YupanaBottomNav (4 ítems: Inicio, Cuentas, Clientes, Ajustes)
   - YupanaServiceLogo (cuadro con inicial + color del servicio)
   - YupanaStatCard (métrica del dashboard: número + label)
   - YupanaListItem / YupanaAssignmentCard (item de la lista de vencimientos)
   - YupanaAvatar (con gradiente terracota→dorado)

4. COMPLETITUD para las 15 pantallas futuras aún no diseñadas: incluye también
   componentes/estados de:
   - EmptyState (vacío)
   - LoadingState (skeletons/shimmer)
   - ErrorState (con reintentar)

5. SHOWCASE: una pantalla/Composable "DesignSystemShowcase" que renderice TODOS los
   tokens y componentes juntos, para validarlos de un vistazo en Android.

═══ DISCIPLINA ═══
- El módulo compila y el showcase renderiza en Android.
- Cero valores hardcodeados de color/tamaño fuera del design system: todo vía tokens.
- Reemplaza el TODO que quedó en la Fase 1 (colores literales en el entry point) por
  el uso de los tokens del design system.
- Commits atómicos: "feat(designsystem): tokens", "feat(designsystem): theme",
  "feat(designsystem): base components", "feat(designsystem): states (empty/loading/error)",
  "feat(designsystem): showcase", "refactor(app): entry point usa design system".

═══ AL FINALIZAR ═══
Muéstrame:
(a) la lista de tokens implementados,
(b) la lista de componentes creados con una breve descripción,
(c) cómo ver el DesignSystemShowcase en Android.

Detente y espera mi OK antes del Grupo 1 (Auth).
```

## Referencias

- CLAUDE.md · sección 5 (identidad visual oscuro andino).
