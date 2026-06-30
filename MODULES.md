# Módulos de Yupana — mapa por fases

Estructura **KMP + Compose Multiplatform**. Fase 1 crea solo el esqueleto que compila/corre en
Android, con iOS configurado en la estructura. Los módulos `:core:*` y `:feature:*` se irán
**adaptando desde la base `ce-android`** en fases posteriores (ver `CLAUDE.md` §3).

## Existentes (Fase 1)
| Módulo | Plugin | Rol |
|---|---|---|
| `:app` | `com.android.application` (+ compose compiler) | App Android; consume `:shared`; expone secrets vía BuildConfig. |
| `:shared` | `com.android.kotlin.multiplatform.library` (+ KMP + CMP) | UI/lógica compartida. `commonMain` / `androidMain` / `iosMain`. Targets `iosArm64`/`iosSimulatorArm64` (framework `shared`). |
| `iosApp/` | Xcode (se genera en macOS) | Esqueleto iOS que consume el framework `shared`. |

## Planeados (NO crear todavía)
| Módulo | Fase | Rol |
|---|---|---|
| `:core:designsystem` | 2 | Tema "oscuro andino", tokens y componentes Compose (portar desde base). |
| `:core:mvi` | 2 | Contrato MVI + `MviViewModel` base (ViewModel multiplataforma). |
| `:core:network` | 2 | Ktor + supabase-kt + DTOs `@Serializable` + RemoteDataSources. |
| `:core:persistence` | 3 | DataStore + token/sesión seguro (expect/actual: Keystore ↔ Keychain). |
| `:core:utils` | 2 | Dispatchers, extensiones, `AppLogger`. |
| `:feature:*` | 3+ | auth, dashboard, cuentas, asignar, mover (Login→Dashboard end-to-end primero). |

## Bordes de plataforma (expect/actual en `:shared`) — pendientes
Token/sesión segura, creación de DataStore, `AppLogger`, engine HTTP (OkHttp/Darwin), push (FCM/APNs).
