# Grupo 9 · Ajustes (Ajustes + Preferencias de notificación + Mi cuenta)

**Objetivo:** completar la pestaña de Ajustes con sus tres pantallas. Ajustes básicos
(pantalla raíz), preferencias de notificación (alimenta al push del Grupo 11), y mi
cuenta (perfil del admin + cambiar contraseña).

**Depende de:** Fase 1, Fase 2, Grupo 1 (Auth), MVP funcional.
**Prepara:** Grupo 11 (push) leerá las preferencias que se configuren aquí.

## Estado

- Estado: 🟡 en progreso (código y migración listos, falta QA manual en el emulador)
- Iniciada: 2026-07-03
- Terminada: —
- Notas de ejecución:
  - Persistencia: **Supabase (`user_preferences`)**, no DataStore como sugería este doc
    originalmente. Motivo: Grupo 11 corre como cron/Edge Function *server-side*
    (CLAUDE.md §4) y no puede leer un DataStore que vive en el dispositivo del admin.
    Migración `create_user_preferences` aplicada sobre el proyecto real
    (`axkkjfebvhceylccierg`): tabla con RLS `owner_id = auth.uid()` (mismo patrón que
    `client`/`assignment`), verificada tras aplicar (columnas + policy `user_preferences_all`).
  - Nombre del admin: **no se agregó** columna/tabla nueva. "Mi cuenta" solo muestra
    el email (no editable, identidad Supabase). Deuda conocida documentada.
  - "Contraseña actual" en Cambiar contraseña: se verifica de verdad server-side, no
    es solo UI — `UserUpdateBuilder.currentPassword` (confirmado leyendo el bytecode
    de `auth-kt` 3.6.0, no de memoria) se envía junto con la nueva contraseña en
    `auth.updateUser { ... }`.
  - `./gradlew compileCommonMainKotlinMetadata` → BUILD SUCCESSFUL.

## Criterio de "hecho"

- [x] Tab Ajustes ya no es placeholder: muestra pantalla raíz con secciones.
- [x] Preferencias de notificación permite configurar "días antes" y "hora del día".
- [x] Mi cuenta muestra datos del admin y permite cambiar contraseña.
- [x] Cerrar sesión funciona y vuelve a Login (mecanismo ya existente, sin cambios).
- [x] Las preferencias se persisten en Supabase (tabla + RLS aplicadas). **Falta la
      prueba manual end-to-end en el emulador** (guardar → reentrar → verificar que
      persiste) antes de dar el grupo por cerrado del todo.

## Prompt para Claude Code

```
Fase de construcción: GRUPO 9 — AJUSTES completo (3 pantallas).

ANTES DE NADA:
- Lee el CLAUDE.md.
- Confirma que los Grupos 1-5 (MVP) compilan y funcionan. La tab Ajustes existe pero
  probablemente como placeholder desde el Grupo 2 (Clientes).

═══ ALCANCE (3 pantallas) ═══
1. Ajustes (pantalla raíz):
   - Sección "Cuenta": Mi cuenta / perfil (navega a la pantalla 3), Cerrar sesión
     (con confirmación).
   - Sección "Notificaciones": Preferencias de notificación (navega a la pantalla 2).
   - Sección "General" (opcional para MVP+): tema (si se decide permitir claro/oscuro,
     por ahora la app es dark-first), idioma, acerca de.
   - Estructura tipo lista de items con iconos y chevron.

2. Preferencias de notificación:
   - "Días antes del vencimiento para avisar": selector de 1, 2, 3, 5, 7 días
     (múltiple o único, tu criterio; recomiendo múltiple).
   - "Hora del día para el recordatorio": time picker (ej. 09:00).
   - "Notificaciones activas": switch general (on/off).
   - Guardar al cambiar o botón "Guardar" explícito.

3. Mi cuenta:
   - Datos del admin: email (no editable, es su identidad en Supabase), nombre
     (editable si se quiere; requiere columna en un profile de usuario que aún no
     existe — documenta si lo agregas o no).
   - Cambiar contraseña: formulario con contraseña actual, nueva, confirmar.
     Usa supabase-kt: auth.updateUser { password = "..." }.

═══ DISEÑO ═══
No hay mockup en Claude Design para estas pantallas. Constrúyelas con los componentes
de :core:designsystem (YupanaListItem para secciones, YupanaSwitch/YupanaChip para
selectores, YupanaTextField para formulario). Estética consistente con el resto.

═══ BACKEND ═══
- Preferencias de notificación: decidir dónde persistir.
  - Opción A (recomendada): DataStore local. Simple, sin cambios de esquema, suficiente
    para MVP+. Desventaja: no sincroniza entre dispositivos.
  - Opción B: tabla nueva en Supabase (user_preferences: user_id, days_before, hour,
    enabled). Más trabajo pero sincroniza.
  Documenta cuál elegiste y por qué.
- Cambiar contraseña: supabase-kt auth.updateUser (requiere sesión activa, el SDK
  valida la contraseña actual internamente si se configura re-auth; verificar
  documentación).
- Cerrar sesión: supabase-kt auth.signOut() + limpiar navegación y volver a Login.

═══ ARQUITECTURA ═══
- Módulo :feature:settings (nuevo) o dentro de :feature:auth (mi cuenta) + un módulo
  compartido para preferencias, tu criterio.
- Use cases: GetNotificationPreferences, UpdateNotificationPreferences, ChangePassword,
  Logout, GetCurrentUser.
- Screens/Routes/ViewModels/Contracts para las 3 pantallas.

═══ SIN TESTS ═══
No escribas tests.

═══ DISCIPLINA ═══
- Al terminar, se puede: entrar a Ajustes → cambiar preferencias de notificación (y
  verificar que persisten al reabrir la app) → cambiar contraseña → cerrar sesión y
  volver a login.
- Commits: "feat(settings): domain + repositories",
  "feat(settings): settings root screen",
  "feat(settings): notification preferences",
  "feat(settings): my account + change password",
  "feat(settings): logout".

═══ AL FINALIZAR ═══
Muéstrame: (a) cómo probar cada pantalla, (b) qué decidiste sobre persistencia de
preferencias (DataStore o Supabase) y por qué, (c) qué decidiste sobre el nombre del
admin (columna nueva o solo email).
Detente y espera OK antes del Grupo 10.
```

## Referencias

- CLAUDE.md · sección 4 (auth con supabase-kt).
- Grupo 11 leerá las preferencias configuradas aquí para el timing de las push.
