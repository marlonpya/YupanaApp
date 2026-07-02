# Yupana · Plan de fases

> Mapa maestro de la construcción de Yupana. Cada fase es un archivo autocontenido en
> esta carpeta con su prompt ejecutable, criterios de "hecho" y estado. Este índice
> refleja el progreso global y sirve como punto de entrada.

**Paquete base:** `com.strawtechberry.yupana`
**Repo:** github.com/marlonpya/YupanaApp
**Contexto permanente del producto:** ver `/CLAUDE.md` en la raíz del proyecto.

---

## Cómo usar estos documentos

- Antes de arrancar cualquier fase, lee su archivo completo y el `CLAUDE.md` de la raíz.
- Ejecuta las fases **en orden**. Cada una asume que las anteriores compilan y funcionan.
- Al terminar una fase: actualiza su bloque `## Estado` en el archivo y este índice.
- Los prompts están escritos para pegar en Claude Code apuntando al repo `YupanaApp`.
- **No tests en ningún prompt** — decisión del proyecto para priorizar velocidad de MVP.

---

## Estado global

| # | Fase / Grupo | Archivo | Estado |
|---|---|---|---|
| 1 | Esqueleto + librerías | `phase-1-scaffold.md` | ✅ hecho |
| 2 | Design System | `phase-2-designsystem.md` | ✅ hecho |
| 3 | Grupo 1 — Auth | `group-1-auth.md` | ✅ hecho |
| 4 | Grupo 2 — Clientes | `group-2-clients.md` | ✅ hecho |
| 5 | Grupo 3 — Cuentas | `group-3-accounts.md` | ✅ hecho |
| 6 | Grupo 4 — Asignar perfil | `group-4-assign.md` | ✅ hecho |
| 7 | Grupo 5 — Dashboard | `group-5-dashboard.md` | 🟡 en curso (código completo en `feature/dashboard`, pendiente merge a `develop` y validación end-to-end) |

**Leyenda:** ⬜ pendiente · 🟡 en curso · ✅ hecho · ⚠️ bloqueada

---

## Fuera de este plan (fases futuras, no planificar aún)

Cuando el MVP compile, funcione y lo hayas usado en tu operación real 1-2 semanas,
volvemos aquí y desglosamos estas en archivos propios con base en la experiencia real.

**Fase 2 de producto** (comodidad): Recuperar contraseña, Todos los vencimientos con filtros,
Mover integrante, Detalle de cliente, Ajustes básicos.

**Fase 3 de producto** (pulido + push): Catálogo de servicios (agregar propios), Preferencias
de notificación, Mi cuenta / cambiar contraseña, Push/recordatorios (Edge Function + FCM).

**Hardening previo a publicar en tiendas**: flavors por ambiente, R8/minify, linters
(detekt/ktlint), CI/CD (GitHub Actions), revisión de secrets, HTTPS estricto.

**Activación iOS**: probar el target ya configurado desde Fase 1, resolver Keychain,
APNs, engine Darwin, shell del `iosApp`.

---

## Convenciones que se respetan en todos los prompts

- **Sin tests** en ninguna fase.
- **Commits atómicos** con mensajes convencionales (`feat(scope): …`, `chore: …`).
- **Checkpoint tras la matriz de versiones** en la Fase 1 (se detiene antes de escribir builds).
- **Secrets fuera del repo** desde el commit 1 (`secrets.properties` no versionado).
- **Diseño con components del design system**, no reproducciones independientes.
- **Reconciliación diseño ↔ design system**: si algo difiere, gana el design system y se avisa.
- **Todas las escrituras a Supabase respetan RLS** vía sesión autenticada (nunca `service_role`).
