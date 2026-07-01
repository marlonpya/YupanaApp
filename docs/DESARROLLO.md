# Desarrollo — compilar y verificar

## Entorno WSL

El repo vive en el filesystem de Windows (`/mnt/c/...`) pero suele abrirse desde WSL,
que **no** trae JDK ni SDK Android nativos de Linux. El Android Studio del usuario es
Windows (`java.exe`, build-tools `.exe`); esos binarios no sirven para un build Gradle
ejecutado dentro de WSL.

Para compilar por CLI desde WSL:

1. **JDK**: descargar un Temurin portable (ej. 21) y exportar `JAVA_HOME`/`PATH` hacia
   él. AGP 9 + Gradle 9.4 lo requieren.
2. **SDK Android propio para Linux**: `sdk.dir` en `local.properties` normalmente apunta
   a una ruta Windows (`C:\...`), inválida en WSL. Pasos:
   1. Respaldar `local.properties` y reescribir `sdk.dir` apuntando a un SDK Android
      Linux propio.
   2. Instalar cmdline-tools Linux (extraer con `jar xf`, no hay `unzip`; luego
      `chmod +x bin/*`).
   3. `sdkmanager` para `platform-tools` y `build-tools;36.0.0` (binarios nativos Linux
      de `aapt`/`aapt2`/`zipalign`).
   4. La plataforma (`platforms/android-37.0`) es `android.jar`, cross-platform: se
      puede copiar directo del SDK de Windows en vez de volver a descargarla.
   5. Copiar las `licenses/` del SDK de Windows para auto-aceptarlas.
3. **Restaurar `local.properties`** a la ruta Windows original al terminar — Android
   Studio (Windows) la necesita así.

## Comandos de verificación

- `./gradlew compileCommonMainKotlinMetadata` — type-check rápido de todo `commonMain`,
  **no necesita el SDK Android completo**. Es el atajo recomendado para verificar cambios
  de dominio/lógica compartida sin montar el SDK Linux.
- `./gradlew test` — corre los tests (`commonTest`, Given/When/Then, fakes; ver
  `CLAUDE.md` §7).
- `./gradlew :app:assembleDebug` — build completo del APK; requiere el SDK Android Linux
  configurado como arriba. Verificado funcionando sobre el módulo `:feature:auth`.

## Límite conocido: `git push` en WSL

`git push` hacia GitHub falla en WSL por falta de credenciales HTTPS configuradas ahí.
El usuario empuja los commits desde su propio entorno (Windows), no desde la sesión WSL.
