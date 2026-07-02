# Fase 1 · Esqueleto + librerías

**Objetivo:** crear el proyecto Yupana con toda la configuración base y librerías
alineadas, que compile en Android y tenga iOS estructuralmente listo.

**Depende de:** nada.
**Prepara:** todas las fases siguientes.

## Estado

- Estado: ✅ hecho
- Iniciada: 2026-06-30
- Terminada: 2026-06-30
- Notas de ejecución: commits `fdc03d6`…`90ee1e7`. Version catalog + settings KMP,
  módulo `:shared` (KMP + CMP), `:app` Android consumiendo `:shared`, esqueleto iOS,
  secrets fuera del repo (`secrets.properties` gitignored + `.example`), entry point
  "Yupana" en `#C8643C` sobre `#15110E`. Namespace `com.strawtechberry.yupana`,
  minSdk 26 confirmados en `app/build.gradle.kts` / `libs.versions.toml`.

## Criterio de "hecho"

- [ ] La matriz de versiones está reportada y aprobada por Marlon **antes** de escribir builds.
- [ ] `./gradlew :app:assembleDebug` compila sin errores.
- [ ] La app Android arranca y renderiza el texto "Yupana".
- [ ] El framework de `:shared` se genera para iOS.
- [ ] El proyecto `iosApp` abre en Xcode sin errores de configuración.
- [ ] Los secrets no están en el repo (`secrets.properties` en `.gitignore`).
- [ ] Todos los commits atómicos están en la rama principal en GitHub.

## Prompt para Claude Code

```
Vas a iniciar "Yupana", un proyecto Kotlin Multiplatform (KMP) con Compose Multiplatform
(CMP) para UI compartida (Android + iOS), priorizando ARQUITECTURA LIMPIA desde la base.
Repo: github.com/marlonpya/YupanaApp.

ANTES DE NADA:
- Lee el CLAUDE.md en la raíz del proyecto (fuente de verdad: producto, stack, backend
  Supabase ya existente, identidad visual, decisiones).
- Verifica que mi entorno tiene JAVA_HOME y ANDROID_HOME/ANDROID_SDK_ROOT configurados.
  Si falta alguno, DÍMELO y detente; no intentes trabajar sin ellos.

═══ NOMBRE DE PAQUETE (fijo, no cambiar) ═══
com.strawtechberry.yupana
Aplica este paquete a la app Android, al módulo :shared (namespace/package) y al bundle
identifier de iOS.

═══ ALCANCE DE ESTA FASE ═══
Crear el ESQUELETO con TODAS las dependencias y librerías configuradas y alineadas, que
COMPILE y CORRA en Android (target iOS configurado en la estructura y verificable
estructuralmente, aunque no se ejecute todavía). NADA de features ni pantallas reales:
solo un punto de entrada mínimo que renderice "Yupana" y confirme que la tubería funciona.

═══ PASO 1 OBLIGATORIO: matriz de versiones ═══
NO escribas builds hasta haber determinado, consultando documentación/repositorios
ACTUALES (no de memoria), versiones compatibles entre sí de:

- Kotlin, plugin KMP, AGP, Gradle
- Compose Multiplatform (JetBrains)
- Ktor 3.x
- supabase-kt 3.x (auth-kt, postgrest-kt, functions-kt) — y qué versión EXACTA de Ktor exige
- Koin (koin-core + koin-compose, multiplataforma)
- kotlinx.serialization, kotlinx.coroutines
- lifecycle-viewmodel multiplataforma (para el futuro MVI base)
- AndroidX necesario solo en androidMain

Si NO tienes acceso a internet en este entorno, DILO explícitamente y pídeme que yo te
confirme las versiones. NO recurras a memoria si esto ocurre.

Repórtame la matriz en una TABLA MARKDOWN y marca cualquier incompatibilidad. Prioriza
la versión de Ktor que EXIGE supabase-kt sobre la última disponible.

⚠️ CHECKPOINT — DETENTE AQUÍ.
Después de reportar la matriz, ESPERA MI APROBACIÓN EXPLÍCITA antes de escribir el
version catalog, los build.gradle.kts o cualquier archivo. NO avances aunque las
versiones parezcan compatibles. Este checkpoint es obligatorio.

═══ PASO 2 (SOLO tras mi OK): construir el esqueleto ═══

1. `settings.gradle.kts` + `gradle/libs.versions.toml` con TODAS las librerías de la
   matriz declaradas (aunque algunas no se usen aún en esta fase).

2. Módulos base, configurados y compilando:
   - `:app` → app Android con Compose Multiplatform.
   - `:shared` → módulo KMP con sourceSets commonMain / androidMain / iosMain.
   - `iosApp` → esqueleto Xcode que consume el framework de :shared.

   Deja documentado como COMENTARIO en `settings.gradle.kts` qué módulos vendrán en
   fases siguientes (`:core:designsystem`, `:core:mvi`, `:core:network`,
   `:core:persistence`, `:core:utils`) — no los crees ahora.

3. Configuración por plataforma:
   - Engine Ktor: OkHttp o CIO (Android), Darwin (iOS), declarados en los sourceSets
     correctos.
   - `minSdk = 26` (requisito de supabase-kt), `targetSdk` y `compileSdk` según la matriz.
   - JVM toolchain 17, sourceCompatibility 17, targetCompatibility 17.

4. Mecanismo de secrets SIN hardcodear:
   - Los valores `SUPABASE_URL` y `SUPABASE_PUBLISHABLE_KEY` se leen de un archivo
     `secrets.properties` en la raíz (NO versionado).
   - En Gradle, lee ese archivo con `java.util.Properties` y pásalos como
     `buildConfigField` al módulo `:app` (y expón por `expect/actual` a `:shared` si
     hace falta en esta fase; si no, déjalo listo para la siguiente).
   - Crea un `secrets.properties.example` (SÍ versionado) con las claves y valores
     placeholder para que cualquiera sepa qué configurar.

5. `.gitignore` correcto DESDE EL PRIMER COMMIT: `secrets.properties`,
   `local.properties`, `google-services.json`, `*.xcconfig` con secretos, keys,
   `.gradle/`, `build/`, `.idea/`, `xcuserdata/`.

6. Punto de entrada mínimo:
   - En `:app`, un Composable que muestre el texto "Yupana" en color `#C8643C` sobre
     fondo `#15110E`. Añade un `// TODO: mover a :core:designsystem en Fase 2` sobre
     esos colores literales.
   - iOS: verifica que el framework de :shared se genera y que `iosApp` abre en Xcode
     sin errores. NO es obligatorio que renderice todavía; sí es obligatorio que
     estructuralmente esté listo.

═══ DISCIPLINA ═══
- Commits atómicos, mensajes convencionales:
  `chore: version catalog`, `chore: módulo shared`, `chore: ios skeleton`,
  `chore: secrets mechanism`, `chore: gitignore`, `feat: entry point mínimo`,
  y un commit final `chore(phase-1): scaffold complete`.
- NUNCA subas `secrets.properties` (verifica el `.gitignore` antes del primer commit).
- Trabaja directamente en la rama principal del repo (main).

═══ AL FINALIZAR ═══
Repórtame:
(a) la matriz de versiones final fijada,
(b) cómo compilar y correr Android (comando exacto),
(c) confirmación de que el framework de :shared se genera y que iosApp abre en Xcode,
(d) qué módulos quedaron documentados como comentarios para las próximas fases,
(e) confirmación de que `secrets.properties` NO está en el repo pero
    `secrets.properties.example` SÍ.

Luego DETENTE y espera mi visto bueno antes de la Fase 2 (design system). No avances
por tu cuenta.
```

## Referencias

- CLAUDE.md · secciones 1 (producto), 2 (stack y decisiones), 4 (backend Supabase).
- Reporte previo `ANALISIS_BASE.md` que caracterizó `ce-android` como base KMP-friendly.
