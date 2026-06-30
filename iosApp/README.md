# iosApp — esqueleto iOS de Yupana

Consume el framework `shared` (Compose Multiplatform) generado por el módulo `:shared`.
El target iOS ya está configurado en `shared/build.gradle.kts`
(`iosArm64` + `iosSimulatorArm64`, framework `baseName = "shared"`, estático).

## Estado (Fase 1)
- Fuentes Swift listas: `iOSApp.swift` (entry point) y `ContentView.swift`
  (envuelve `MainViewControllerKt.MainViewController()` del framework `shared`).
- **El `iosApp.xcodeproj` se genera en macOS** (este repo se está construyendo en Windows/WSL,
  sin Xcode). No se compila iOS en esta fase; solo se valida que la estructura y los targets
  Kotlin/Native configuran sin romper el build de Android.

## Cómo completar en macOS (fase posterior)
1. Abrir el proyecto en Android Studio con el plugin **Kotlin Multiplatform**, o crear el
   `iosApp.xcodeproj` en Xcode apuntando a esta carpeta `iosApp/`.
2. Añadir un **Build Phase → Run Script** antes de "Compile Sources":
   ```sh
   cd "$SRCROOT/.."
   ./gradlew :shared:embedAndSignAppleFrameworkForXcode
   ```
3. En *Framework Search Paths* incluir `$(SRCROOT)/../shared/build/xcode-frameworks/...`
   (lo gestiona la task `embedAndSignAppleFrameworkForXcode`).
4. Copiar `Configuration/Config.xcconfig.example` → `Config.xcconfig` y enlazarlo en el target.
5. Ejecutar en simulador (`iosSimulatorArm64`) o dispositivo (`iosArm64`).
