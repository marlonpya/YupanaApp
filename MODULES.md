# Módulos de Yupana — mapa por fases

Estructura **KMP + Compose Multiplatform**. Los módulos `:core:*` y `:feature:*` se van
**adaptando desde la base `ce-android`** por fases (ver `CLAUDE.md` §3), compilando en cada paso.

## Existentes
| Módulo | Plugin | Rol |
|---|---|---|
| `:app` | `com.android.application` (+ compose compiler) | App Android; consume `:shared`; expone secrets vía BuildConfig. |
| `:shared` | `com.android.kotlin.multiplatform.library` (+ KMP + CMP) | UI/lógica compartida. `commonMain` / `androidMain` / `iosMain`. Targets `iosArm64`/`iosSimulatorArm64` (framework `shared`). |
| `iosApp/` | Xcode (se genera en macOS) | Esqueleto iOS que consume el framework `shared`. |
| `:core:designsystem` | Kotlin Multiplatform library | Tema "oscuro andino", tokens y componentes Compose (`component/`, `state/`, `theme/`, `showcase/`). |
| `:core:mvi` | Kotlin Multiplatform library | Contrato MVI (State/Intent/Event) + `MviViewModel` base multiplataforma. |
| `:core:supabase` | Kotlin Multiplatform library | Integración supabase-kt (cliente, módulo Auth). |
| `:feature:auth` | Kotlin Multiplatform library | Login, registro y splash: capas `domain/usecase`, `data`, `di` (Koin) y `ui` ya implementadas. |
| `:feature:clients` | Kotlin Multiplatform library | Lista de clientes con búsqueda y formulario crear/editar. |
| `:feature:accounts` | Kotlin Multiplatform library | Catálogo de servicios, crear/editar cuenta, "Mis cuentas" agrupado por servicio, detalle de cuenta. |
| `:feature:assignment` | Kotlin Multiplatform library | Asignar perfil (dos puntos de entrada: desde detalle de cuenta y desde cero). Módulo separado de `:feature:accounts` (decisión del Grupo 4). |
| `:feature:dashboard` | Kotlin Multiplatform library | "Próximos a vencer": métricas + lista desde `upcoming_expirations`, y detalle de asignación con acciones (renovar/editar/liberar). En rama `feature/dashboard`, aún no mergeado a `develop`. |

## Planeados (NO crear todavía)
| Módulo | Fase | Rol |
|---|---|---|
| `:core:persistence` | Fase 2 de producto | DataStore + token/sesión seguro (expect/actual: Keystore ↔ Keychain). |
| `:core:utils` | Fase 2 de producto | Dispatchers, extensiones, `AppLogger`. |

`:core:network` (Ktor + DTOs `@Serializable` + RemoteDataSources genéricos, si hiciera
falta más allá de lo que ya cubre `:core:supabase`) se evalúa cuando surja necesidad
concreta fuera de auth — no crear especulativamente.

## Bordes de plataforma (expect/actual en `:shared`) — pendientes
Token/sesión segura, creación de DataStore, `AppLogger`, engine HTTP (OkHttp/Darwin), push (FCM/APNs).
