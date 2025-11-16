# Configuración de Vercel para Evitar Auto-Deploy Innecesario

Este documento explica cómo configurar Vercel para evitar que haga auto-deploy cuando solo hay cambios en backend.

## Problema

Vercel detecta cambios en la rama conectada (`main`) y hace auto-deploy automáticamente, incluso si los cambios son solo en `backend/` y no afectan al frontend.

## Soluciones

### Opción 1: Desactivar Auto-Deploy en Vercel (Recomendado)

1. Ve a tu proyecto en Vercel
2. Settings → Git
3. En "Production Branch" o "Preview Branches":
   - **Desactiva** "Auto-deploy on push"
4. Guarda los cambios

**Resultado**: Vercel NO hará auto-deploy automáticamente. Solo deployará cuando lo hagas manualmente o desde GitHub Actions.

### Opción 2: Configurar Ignore Build Step

1. Ve a tu proyecto en Vercel
2. Settings → Git
3. En "Ignore Build Step", agrega:

```bash
git diff HEAD^ HEAD --quiet -- frontend/
```

**Explicación**:

- Solo hace build si hay cambios en `frontend/`
- Si solo cambia `backend/`, ignora el build
- Si no hay cambios en `frontend/`, no deploya

### Opción 3: Usar vercel.json con Ignore

Crea o actualiza `frontend/vercel.json`:

```json
{
  "ignoreCommand": "git diff HEAD^ HEAD --quiet -- frontend/ || exit 1"
}
```

**Resultado**: Vercel verifica si hay cambios en `frontend/` antes de hacer build.

### Opción 4: Configurar Root Directory en Vercel

1. Ve a tu proyecto en Vercel
2. Settings → General
3. En "Root Directory", establece: `frontend`
4. Guarda los cambios

**Resultado**: Vercel solo monitorea cambios en `frontend/`, ignora `backend/`.

## Recomendación: Opción 1 + GitHub Actions

**Mejor práctica**:

1. **Desactiva auto-deploy en Vercel** (Opción 1)
2. **Usa GitHub Actions para deployar** (workflows `production-deploy.yml`)

**Ventajas**:

- Control total sobre cuándo se deploya
- Solo deploya cuando realmente hay cambios en frontend
- Puedes agregar validaciones, tests, etc. antes de deployar
- Historial claro de deploys en GitHub Actions

## Configuración en GitHub Actions

Los workflows ya están configurados para:

1. **Detectar cambios reales**: Solo deployan si hay cambios en `frontend/` o `backend/`
2. **Usar paths filters**: Solo se ejecutan si hay cambios en las carpetas relevantes
3. **Deploy condicional**: Cada componente solo se deploya si tiene cambios

## Ejemplo de Deploy Manual desde GitHub Actions

Si desactivas auto-deploy en Vercel, puedes deployar manualmente desde el workflow:

```yaml
- name: Deploy Frontend to Vercel
  if: steps.frontend_changes.outputs.has_changes == 'true'
  working-directory: ./frontend
  run: |
    npx vercel --prod --token=${{ secrets.VERCEL_TOKEN }}
```

## Secrets Necesarios

Si usas GitHub Actions para deployar a Vercel, necesitas:

1. Ve a tu repositorio en GitHub
2. Settings → Secrets and variables → Actions
3. Agrega:
   - `VERCEL_TOKEN`: Token de Vercel (obtenerlo en Vercel → Settings → Tokens)

## Resumen

| Método                 | Control | Complejidad | Recomendado                 |
| ---------------------- | ------- | ----------- | --------------------------- |
| Desactivar auto-deploy | Alto    | Baja        | ✅ Sí                       |
| Ignore Build Step      | Medio   | Media       | ⚠️ Si necesitas auto-deploy |
| vercel.json ignore     | Medio   | Media       | ⚠️ Si necesitas auto-deploy |
| Root Directory         | Alto    | Baja        | ✅ Sí (si solo frontend)    |

**Recomendación final**: Desactiva auto-deploy en Vercel y usa GitHub Actions para tener control total.
