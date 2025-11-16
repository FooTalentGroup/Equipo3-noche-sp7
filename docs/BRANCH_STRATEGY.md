# Estrategia de Ramas

Estrategia simple basada en Git Flow adaptada para equipos de frontend y backend.

### Ramas Principales

- **`main`**: Rama de producción

  - Contiene el código estable y listo para producción
  - Solo se actualiza mediante merge desde `dev-frontend` o `dev-backend`
  - Protegida contra pushes directos

- **`dev-frontend`**: Rama de desarrollo para Frontend

  - Rama principal de desarrollo para el frontend
  - Se actualiza mediante merge desde feature branches del frontend
  - Se mergea a `main` cuando está listo para producción

- **`dev-backend`**: Rama de desarrollo para Backend

  - Rama principal de desarrollo para el backend
  - Se actualiza mediante merge desde feature branches del backend
  - Se mergea a `main` cuando está listo para producción

## Flujo de Trabajo Simple

1. **Crear rama feature** desde `dev-frontend` o `dev-backend`
2. **Desarrollar** y hacer commits
3. **Crear Pull Request** hacia la rama de desarrollo correspondiente
4. **Mergear** después de revisión

### Proceso hacia Producción

1. Cuando `dev-frontend` o `dev-backend` están listos para producción:

   ```bash
   git checkout main
   git merge dev-frontend    # o dev-backend para backend
   ```

2. Crear Pull Request desde `dev-frontend`/`dev-backend` hacia `main` para revisión final

3. Después de aprobación, mergear y crear tag de versión

## Convenciones de Nombres (Opcionales)

Formato recomendado: `tipo/equipo/nombre`

**Tipos:** `feature`, `bugfix`, `refactor`, `hotfix`, `release`  
**Equipos:** `frontend`, `backend`

**Ejemplos:**
- `feature/frontend/login-google`
- `bugfix/backend/api-timeout`
- `hotfix/frontend/security-patch`

**Nota:** Estas son recomendaciones. El equipo puede ajustarlas según sus necesidades. Las ramas se crean desde GitHub Projects.
