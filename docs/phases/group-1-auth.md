# Grupo 1 · Auth (Splash + Login + Registro)

**Objetivo:** primer grupo de pantallas reales. Valida la tubería completa end-to-end:
arquitectura MVI + Koin + design system + Supabase Auth + sesión persistente.

**Depende de:** Fase 1 y Fase 2.
**Prepara:** el resto de grupos (todos requieren sesión activa por RLS).

## Estado

- Estado: ✅ hecho
- Iniciada: 2026-07-01
- Terminada: 2026-07-01
- Notas de ejecución: commits `22bfa0b`…`98d9f9c` (más una serie de `refactor(auth):`
  que tradujo dominio/casos de uso/contratos MVI a inglés). Módulos `:core:mvi` y
  `:core:supabase` creados como base compartida; `:feature:auth` con Login, Registro
  y Splash routing según sesión, sobre `supabase-kt` (Auth PKCE).

## Criterio de "hecho"

- [ ] Se puede: abrir la app → registrarse → cerrar → reabrir (va directo si hay sesión).
- [ ] Login funciona contra el Supabase real del proyecto.
- [ ] Splash decide destino según sesión.
- [ ] Estructura `:feature:auth` con domain/data/ui/di replicable.
- [ ] Todos los commits atómicos en GitHub.

## Prompt para Claude Code

```
Fase de construcción: GRUPO 1 — AUTENTICACIÓN de Yupana.
Este es el primer grupo de pantallas reales. Valida la tubería completa end-to-end:
arquitectura MVI + Koin + design system + Supabase Auth + sesión persistente.

ANTES DE NADA:
- Lee el CLAUDE.md (producto, backend Supabase, decisiones).
- Confirma que Fase 1 (esqueleto) y Fase 2 (:core:designsystem) compilan. Este grupo
  se construye SOBRE ellas y usa sus componentes (YupanaButton, YupanaTextField, etc.).

═══ ALCANCE (solo estas 3 pantallas) ═══
1. Splash / Routing: al abrir, decide destino según haya sesión activa en Supabase
   → si hay sesión válida va al Dashboard (placeholder por ahora), si no, al Login.
2. Login: email + contraseña contra Supabase Auth (supabase-kt auth.signInWith Email).
3. Registro: crear cuenta nueva de admin (auth.signUpWith Email).
Incluye "¿olvidaste tu contraseña?" solo como navegación placeholder (la pantalla real
es fase posterior).

═══ DISEÑO ═══
Importa el diseño de Claude Design como REFERENCIA visual para Login/Registro:
[MARLON PEGA AQUÍ SU INSTRUCCIÓN EXACTA DE IMPORTACIÓN DEL .dc.html]
Regla: NO reproduzcas el diseño de forma independiente. CONSTRÚYELO con los componentes
del :core:designsystem (YupanaTextField, YupanaButton, YupanaTheme, etc.). Si el diseño
difiere del design system en algún valor, prioriza el design system y AVÍSAME de la
diferencia (reconciliación). El motivo khipu del header del login puede ser un
Composable propio.

═══ ARQUITECTURA (replica el patrón para cada pantalla) ═══
- Módulo :feature:auth con: domain/ (interfaces + modelos + use cases: Login, Register,
  ObserveSession/GetSession), data/ (DefaultAuthRepository usando supabase-kt),
  ui/ (Screen + Route + ViewModel + Contract por pantalla), di/ (módulo Koin).
- MVI: cada pantalla con su UiState (data class), UiIntent y UiEvent (sealed).
  ViewModel extiende el MviViewModel base de :core:mvi. Errores con Result + fold.
- Navegación: NavHost central; NavController solo en *Route; navegación como UiEvent.
- Sesión persistente: el SDK supabase-kt persiste la sesión; el Splash la consulta para
  decidir routing. Configura el almacenamiento seguro de sesión (expect/actual si aplica).
- Validaciones de formulario: email válido, contraseña mínima, estados de error inline.
- Estados: loading (botón con spinner), error (credenciales inválidas, sin conexión →
  usar los componentes ErrorState/estados del design system).

═══ SIN TESTS ═══
No escribas tests en esta fase ni en ninguna. Prioriza que el flujo funcione end-to-end.

═══ DISCIPLINA ═══
- Al terminar, Android COMPILA y se puede: abrir la app → registrarse → cerrar →
  reabrir (va directo si hay sesión) → login funciona contra mi Supabase real.
- Commits atómicos en GitHub por sub-paso: "feat(auth): domain + usecases",
  "feat(auth): supabase repository", "feat(auth): login ui + mvi",
  "feat(auth): register ui", "feat(auth): splash routing".
- Recuerda: SUPABASE_URL y PUBLISHABLE_KEY desde secrets no versionados.

═══ AL FINALIZAR ═══
Muéstrame: (a) cómo probar el flujo completo en Android, (b) diferencias diseño↔design
system si las hubo, (c) estructura final de :feature:auth.
Detente y espera mi OK antes del Grupo 2 (Clientes).
```

## Referencias

- CLAUDE.md · sección 4 (backend), sección 6 (pantalla 2 y 3).
- Paso previo a ejecutar: pegar la instrucción exacta de importación de Claude Design
  en el placeholder.
