# Grupo 11 · Push y recordatorios (Edge Function + FCM + APNs)

**Objetivo:** el motor de recordatorios automáticos. Es lo que originalmente motivó
el proyecto: dejar de recordar manualmente los vencimientos. Fase técnica más que de
pantallas: cron en Supabase que revisa vencimientos y dispara push al dispositivo.

**Depende de:** Fase 1, Fase 2, todos los grupos previos (especialmente Grupo 9 para
preferencias de notificación).
**Prepara:** — (último grupo del producto completo).

## Estado

- Estado: ⬜ pendiente
- Iniciada: —
- Terminada: —
- Notas de ejecución: —

## Criterio de "hecho"

- [ ] Al iniciar sesión, la app registra su token FCM en Supabase asociado al usuario.
- [ ] Edge Function corre diario (cron) y busca vencimientos próximos según las
      preferencias del usuario.
- [ ] Envía push vía FCM con mensaje claro ("Cobrar a X - Netflix vence hoy").
- [ ] Al recibir la notificación en Android, al tocarla abre el Detalle de asignación
      correspondiente.
- [ ] `reminder_sent` se marca en true tras enviar, para no duplicar.
- [ ] iOS queda estructuralmente listo (APNs) aunque su implementación final vaya con
      la activación iOS más adelante.

## Prompt para Claude Code

```
Fase de construcción: GRUPO 11 — PUSH Y RECORDATORIOS de Yupana.
Fase técnica final. Habilita las notificaciones push automáticas de vencimientos,
que fueron la motivación original del proyecto.

ANTES DE NADA:
- Lee el CLAUDE.md.
- Confirma que todos los grupos previos compilan y funcionan, especialmente el 9
  (preferencias de notificación) porque este grupo lee esas preferencias.
- Verifica que tienes un proyecto Firebase creado y el archivo google-services.json
  disponible (fuera del repo por seguridad).

═══ ALCANCE (2 frentes: cliente app + backend Supabase) ═══

**FRENTE 1: cliente Android**
- Integrar Firebase Cloud Messaging (FCM) en :app.
- Al iniciar sesión (o al abrir la app con sesión activa), obtener el token FCM y
  registrarlo en Supabase en una tabla nueva `device_token`.
- Servicio FirebaseMessagingService que recibe las notificaciones cuando llegan.
- Cuando el usuario toca una notificación, abrir la app en Detalle de asignación
  correspondiente (deep link o navegación programática con id).
- Manejo del ciclo: refresh del token, borrado al cerrar sesión.

**FRENTE 2: iOS (dejar preparado, no implementar completo)**
- APNs / expect-actual del token de push.
- Documentar los TODOs para la activación iOS futura.

**FRENTE 3: backend Supabase**
- Crear tabla `device_token`:
  ```sql
  create table public.device_token (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references auth.users(id) on delete cascade,
    platform text not null check (platform in ('android','ios')),
    token text not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
  );
  create unique index uq_device_token on public.device_token (user_id, token);
  alter table public.device_token enable row level security;
  create policy device_token_own on public.device_token
    for all using (user_id = auth.uid()) with check (user_id = auth.uid());
  ```
- Aplicar esta migración en Supabase (Marlon puede hacerlo vía MCP desde Claude
  Desktop, o Claude Code con acceso al MCP puede aplicarla directamente).

- Crear Edge Function `send_expiration_reminders` en Supabase:
  1. Consulta `upcoming_expirations` cruzando con las preferencias del usuario
     (leer días_antes de user_preferences si existe la tabla, o usar default 3 días
     si se persistió local — en ese caso el filtro se hace en el cliente o con un
     default fijo del servidor).
  2. Filtra las asignaciones donde `days_left <= días_antes` y `reminder_sent = false`.
  3. Para cada resultado: envía push vía FCM al token del user_id correspondiente
     (usa la variable de entorno FCM_SERVER_KEY en la Edge Function; nunca en el
     cliente).
  4. Marca `reminder_sent = true` en la asignación tras enviar.
  5. Loggea resultados.

- Configurar cron con pg_cron para ejecutar la Edge Function diaria a las 09:00 (o
  la hora que Marlon haya configurado si va con tabla de preferencias).

═══ DISEÑO ═══
No hay pantallas nuevas en este grupo. Solo la notificación push en sí, cuyo formato
se define en el prompt: "Cobrar a [cliente] — [servicio] vence [hoy / en X días]".
Icono con color de marca.

═══ ARQUITECTURA ═══
- Módulo :feature:notifications o dentro de :feature:auth (para el registro del token
  al hacer login), tu criterio, documenta.
- Use cases: RegisterDeviceToken, UnregisterDeviceToken (al logout).
- FirebaseMessagingService en androidMain.
- expect/actual para el token de push (Android: FCM, iOS: APNs).

═══ SEGURIDAD ═══
- El FCM_SERVER_KEY vive SOLO en Supabase (Edge Function env vars). Nunca en la app.
- La app usa solo la SENDER_ID / config pública de FCM.
- Verifica que google-services.json esté en .gitignore.

═══ SIN TESTS ═══
No escribas tests.

═══ DISCIPLINA ═══
- Al terminar, se puede: crear una asignación que venza hoy o mañana → esperar al
  cron (o dispararlo manualmente para probar) → recibir la notificación push en el
  Android real → tocarla → abrir en el Detalle de asignación.
- Probar que reminder_sent evita duplicados en corridas siguientes.
- Commits: "feat(notifications): fcm integration",
  "feat(notifications): device_token table + registration",
  "feat(notifications): edge function send_expiration_reminders",
  "feat(notifications): pg_cron schedule",
  "chore(notifications): ios placeholders for apns".

═══ AL FINALIZAR ═══
Muéstrame: (a) cómo probar el flujo end-to-end (creando un vencimiento cercano y
disparando el cron manualmente), (b) qué decisiones tomaste sobre lectura de
preferencias (default fijo, tabla en Supabase, o desde cliente), (c) qué queda como
TODO documentado para la activación iOS.
Este es el ÚLTIMO grupo del producto completo. Al terminar, resume el estado global
del proyecto y qué queda solo para publicación en tiendas (hardening) y activación iOS.
```

## Referencias

- CLAUDE.md · sección 4 (tabla assignment con reminder_sent).
- Grupo 9 (preferencias de notificación).
- Documentación de Supabase Edge Functions y pg_cron.
- Documentación de FCM (Firebase Cloud Messaging).
