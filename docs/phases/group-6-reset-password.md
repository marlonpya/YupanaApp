# Grupo 6 · Recuperar contraseña

**Objetivo:** completar el flujo de autenticación. En el Grupo 1 dejamos "¿olvidaste tu
contraseña?" como navegación placeholder; ahora se implementa real.

**Depende de:** Fase 1, Fase 2, Grupo 1 (Auth).
**Prepara:** — (autocontenida).

## Estado

- Estado: ⬜ pendiente
- Iniciada: —
- Terminada: —
- Notas de ejecución: —

## Criterio de "hecho"

- [ ] Desde Login → "¿olvidaste tu contraseña?" → pantalla de recuperar → envío real.
- [ ] Supabase envía correo de recuperación (verificable en Auth logs).
- [ ] Después de enviar, la app muestra confirmación clara ("revisa tu correo").
- [ ] Manejo de errores: email no registrado, sin conexión.
- [ ] El link del correo abre la app o web para setear nueva contraseña (deep link
      básico o pantalla propia, tu criterio, documentar).

## Prompt para Claude Code

```
Fase de construcción: GRUPO 6 — RECUPERAR CONTRASEÑA de Yupana.
Completa el flujo de Auth (Grupo 1) que dejó "¿olvidaste tu contraseña?" como placeholder.

ANTES DE NADA:
- Lee el CLAUDE.md.
- Confirma que el Grupo 1 (Auth) compila y funciona.

═══ ALCANCE (1-2 pantallas) ═══
1. Recuperar contraseña: formulario simple con email, botón "Enviar instrucciones".
   Al enviar, muestra pantalla/estado de confirmación ("Revisa tu correo — te enviamos
   un enlace para restablecer tu contraseña").
2. (Opcional) Establecer nueva contraseña: si decides manejar el reset dentro de la app
   vía deep link, incluye la pantalla de "nueva contraseña + confirmar". Si prefieres
   que Supabase maneje el reset en su UI web, documenta esa decisión y no crees esta
   segunda pantalla. Recomendación: para MVP, deja que Supabase maneje el reset web —
   es más simple y funciona sin deep links.

═══ DISEÑO ═══
No hay mockup en Claude Design para estas pantallas. Constrúyelas con los componentes
de :core:designsystem (YupanaTopBar con back, YupanaTextField, YupanaButton,
mensajes de éxito/error), manteniendo la identidad visual del Login.

═══ BACKEND (Supabase, ya soportado) ═══
Usa supabase-kt Auth: auth.resetPasswordForEmail(email). El SDK envía el correo con
el enlace de recuperación configurado en tu proyecto Supabase.
IMPORTANTE: verifica en tu dashboard de Supabase que el template de email de
"Reset Password" esté configurado y que la URL de redirect apunte a tu dominio o
esquema deep link si vas por esa ruta.

═══ ARQUITECTURA (mismo patrón del Grupo 1) ═══
- Añade al módulo :feature:auth: use case ResetPassword, y método correspondiente en
  DefaultAuthRepository.
- ResetPasswordScreen/Route/ViewModel/Contract con MVI.
- Estados: idle, loading, success (con mensaje claro), error (email no válido, sin
  conexión, email no registrado si Supabase lo devuelve).

═══ SIN TESTS ═══
No escribas tests.

═══ DISCIPLINA ═══
- Al terminar, se puede: Login → "¿olvidaste tu contraseña?" → escribir email →
  recibir correo real de Supabase.
- Commits: "feat(auth): reset password usecase + repository",
  "feat(auth): reset password ui + mvi".

═══ AL FINALIZAR ═══
Muéstrame: (a) cómo probar el flujo, (b) qué decidiste sobre "establecer nueva
contraseña" (web de Supabase o pantalla propia), (c) cualquier configuración adicional
que hiciste en Supabase (template de email, redirect URL).
Detente y espera OK antes del Grupo 7.
```

## Referencias

- CLAUDE.md · sección 4 (Auth con supabase-kt).
- Documentación supabase-kt: `Auth.resetPasswordForEmail`.
