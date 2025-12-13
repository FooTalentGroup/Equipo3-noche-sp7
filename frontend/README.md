# Base Repository

Configuración base para proyectos con CI/CD, plantillas de GitHub y estructura de ramas.

## 🚀 Inicio Rápido

### Ramas Principales

- `main` - Producción
- `staging` - QA
- `dev-frontend` - Desarrollo frontend
- `dev-backend` - Desarrollo backend

> Recomendación: mantener `staging` como entorno de QA estable para validar PRs antes de promover a `main`.

## 📚 Documentación

- [Estrategia de Ramas](./docs/BRANCH_STRATEGY.md)
- [Configuración de Vercel](./docs/VERCEL_CONFIG.md)

## 🔧 Configuración Mínima

1. Clonar el repositorio
2. Configurar variables de entorno (ver sección `Entornos y despliegues` más abajo)
3. Personalizar CI/CD y secret keys en el proveedor de hosting (Vercel / Netlify / GitHub Actions)

## 📝 Convenciones (Opcionales)

- **Ramas**: `tipo/equipo/nombre` (ej: `feature/frontend/login`)
- **Equipos**: `frontend`, `backend`
- **Tipos**: `feature`, `bugfix`, `refactor`, `hotfix`, `release`

**Nota:** Estas son recomendaciones. El equipo puede ajustarlas según sus necesidades.

---

# Stokia — Frontend

Este documento complementa y amplía la información existente en el proyecto frontend de Stokia. Está escrito en español y contiene:
- Descripción general del proyecto
- Arquitectura por features
- Estructura de carpetas y responsabilidades
- Módulos y componentes principales
- Servicios y llamadas a API
- Flujo de desarrollo local, pruebas y despliegue
- Consejos de debugging y preguntas frecuentes

> Nota: este README está pensado para desarrolladores que trabajan sobre el código en `frontend/`.

---

## Resumen rápido

- Stack principal: React + Vite + Tailwind CSS.
- Estado remoto / sincronización: Axios a través del wrapper `apiClient` (con interceptor para Authorization).
- Manejo de datos asincrónicos: @tanstack/react-query (consultar `lib/query-client.js`).
- Estilo: Tailwind CSS + utilidades propias y componentes en `shared/components/ui`.
- Arquitectura: `features/` (feature folders) — cada feature contiene componentes, servicios, hooks y páginas relacionadas.

---

## Estructura de alto nivel (carpeta `src/`)

- `assets/` — imágenes, logos y gráficos estáticos.
- `features/` — carpetas por funcionalidad (auth, users, customers, products, sales, etc.). Cada feature suele contener:
  - `components/` — componentes UI específicos de la feature
  - `pages/` — páginas (rutas) que usan estos componentes
  - `services/` — llamadas HTTP relacionadas con la feature
  - `hooks/` — hooks reutilizables aplicados a la feature
  - `validators/` — reglas de validación (opcional)
- `infrastructure/` — configuración global de la aplicación:
  - `api/axios.config.js` — instancia de axios con interceptor de Authorization
  - `constants/config.js` — constantes como `API_BASE_URL`
  - `router/` — definición de rutas y protecciones (ProtectedRoute, PublicRoute)
- `lib/` — utilidades y configuración de librerías (ej. `query-client.js` para React Query)
- `shared/` — componentes, hooks y servicios reutilizables (UI primitives, buttons, inputs, modals)

---

## Mapa de carpetas (detallado)

frontend/
- package.json — scripts y dependencias (dev, build, test)
- vite.config.js — configuración de Vite
- src/
  - main.jsx — punto de entrada React
  - App.jsx — wrapper de app y proveedor(es) (React Query, Router)
  - assets/ — imágenes y recursos estáticos
  - features/
    - auth/ — login/register/reset + servicios de autenticación
    - users/ — gestión de usuarios (tabla, roles, historial, eliminar, restaurar)
    - customers/ — gestión de clientes (tabla, registro, historial, export)
    - products/ — gestión de productos
    - sales/ — páginas y componentes relacionados a ventas
    - ...otras features...
  - infrastructure/
    - api/axios.config.js — instancia axios y manejo de token
    - constants/config.js — variables de entorno y defaults
    - router/ — rutas, redirect y protecciones
  - lib/
    - query-client.js — configuración de React Query
  - shared/
    - components/ui/ — primitives (Button, Input, Dialog, ConfirmDialog, SuccessModal, etc.)
    - services/apiClient.js — wrapper de axios con manejo uniforme de errores

---

## Principales módulos y responsabilidades

- `apiClient` (shared/services/apiClient.js)
  - Encapsula axios y normaliza la respuesta.
  - Centraliza el manejo de errores a través de `infrastructure/api/errorHandler.js`.

- `infrastructure/api/axios.config.js`
  - Crea `axiosInstance` con `baseURL`, `timeout` y un interceptor de petición que añade `Authorization` cuando existe token en `localStorage`.

- Features
  - `auth` — login, registro y gestión de tokens. `authStorage` (utils) lee/guarda token y metadatos en `localStorage`.
  - `users` — tabla de usuarios, modal de historial, eliminar/restore, roles. Usa `usersService.js` para llamadas a `/api/users`.
  - `customers` — listado de clientes, registro (popup), export CSV, historial de compras. Usa `customerService.js` para `/api/clients`.

- UI compartida (`shared/components/ui`)
  - Button, Input, ConfirmDialog, SuccessModal y otros componentes de formulario y layout que se reutilizan.

---

## Documentación de endpoints (front-consumer)

Las llamadas a la API usan los servicios de cada feature. Aquí algunos ejemplos relevantes:

- Users
  - GET /api/users?page&size — lista paginada
  - PUT /api/users/{id} — actualizar (usado para restaurar con { accountStatus: 'ACTIVE' })
  - DELETE /api/users/{id} — eliminar (server behavior: permanent or soft, check backend)

- Clients (Customers)
  - GET /api/clients?page&size — lista paginada
  - POST /api/clients — crear cliente
  - PUT /api/clients/{id} — actualizar cliente
  - DELETE /api/clients/{id} — soft-delete (server marks clientStatus: INACTIVE) — frontend treats as inactive
  - GET /api/clients/{id}/purchase-history — historial de compras

Nota: siempre revisa el servicio `features/*/services/*Service.js` para conocer el shape exacto de request/response.

---

## Cómo ejecutar el frontend localmente

Requisitos: Node.js (v18+ recomendado), npm

1) Instala dependencias

```bash
cd frontend
npm install
```

2) Ejecuta en modo desarrollo

```bash
npm run dev
```

El servidor correrá por defecto en http://localhost:5173 (Vite). Abre la consola para ver logs y errores.

3) Construcción para producción

```bash
npm run build
```

4) Previsualizar artefacto de build

```bash
npm run preview
```

---

## Tests

El proyecto usa Vitest. Comandos útiles:

- Ejecutar tests una sola vez:
  npm run test
- Ejecutar tests en modo watch:
  npm run test:watch
- UI de tests:
  npm run test:ui
- Reporte de coverage:
  npm run test:coverage

Los tests localizan componentes y hooks dentro de cada feature (p. ej. `useAuth.test.jsx`).

---

## Buenas prácticas y recomendaciones de desarrollo

- Sigue la convención de `features/` para agregar nueva funcionalidad: crea subcarpetas `components/`, `pages/`, `services/`, `hooks/`.
- Usa `shared/components/ui` para componentes visuales reutilizables. Evita replicar estilos.
- Para llamadas a la API usa siempre el servicio de feature (ej. `customers/services/customerService.js`) para evitar duplicar lógica de serialización.
- Normaliza los campos esperados del backend (p. ej. `accountStatus` o `clientStatus`) usando `.trim().toUpperCase()` si el backend no es estricto con el case.
- Maneja estados de carga y errores (isLoading, disabled buttons) para evitar peticiones duplicadas.

---

## Common troubleshooting

- Si el listado aparece vacío:
  - Revisa la consola para ver el objeto retornado por `getCustomers` (se imprimirá en desarrollo).
  - Verifica que `localStorage.token` exista si el backend exige autenticación.
  - Revisa la ruta y parámetros de la petición en Network (GET /api/clients).

- Si una petición PUT/DELETE no parece surtir efecto:
  - Comprueba en Postman que el endpoint funciona como esperas.
  - Verifica que el frontend envíe exactamente el payload que el backend espera (ej. `{ accountStatus: 'ACTIVE' }` para restaurar un usuario).

---

## Sugerencias de mejoras (próximos pasos)

- Extraer componentes visuales comunes (`RoleChip`, `ActionButton`) en `shared/components`.
- Añadir Snackbar/Toasts globales para feedback de éxito/error (usar `sonner` ya incluido en deps).
- Implementar paginación en modales que cargan muchos elementos (p.ej. historial de usuarios) y usar React Query para caching/invalidation.
- Estandarizar shapes de respuesta usando un pequeño adapter `apiAdapters/*` si la API cambia de forma entre endpoints.

---

## Entornos y despliegues

Es importante tener un flujo claro de despliegue y entornos para que QA pueda validar cambios antes de producción. A continuación se describen los entornos habituales y recomendaciones para usarlos en este proyecto:

- `development` - Desarrollo local
  - Uso: desarrollo diario. Ejecuta `npm run dev` y apunta `VITE_API_BASE_URL` al backend de desarrollo.
  - Objetivo: desarrollo rápido, hot-reload, debugging.

- `staging` - QA
  - Uso: ambiente de preproducción dedicado a pruebas de QA (control de calidad).
  - Objetivo: desplegar cambios que el equipo QA deberá validar antes de promoverlos a producción.
  - Recomendaciones:
    - Mantener una rama dedicada (por ejemplo `staging` o `qa`) que active despliegues automáticos al entorno `staging` (Vercel, Netlify o CI/CD del equipo).
    - Configurar variables de entorno de `staging` (ej.: `VITE_API_BASE_URL=https://staging-api.example.com`) en el proveedor de hosting o en el pipeline.
    - Asegurar que las credenciales y servicios externos en `staging` sean de prueba y no afecten datos reales.
    - Ejecutar la batería de pruebas de QA (smoke tests, flujos críticos: registro, login, CRUD de usuarios/clientes, pagos si aplica).

- `production` - Producción
  - Uso: entorno en vivo con datos reales.
  - Objetivo: servir la aplicación a usuarios finales.
  - Recomendaciones:
    - Despliegues a producción desde la rama `main` o `master` mediante pipeline/CICD.
    - Revisar que `VITE_API_BASE_URL` apunte al backend de producción y que las keys/servicios externos sean correctos.

### Cómo probar en `staging` localmente

Si necesitas simular `staging` en tu máquina local:
- Crea un archivo `.env.staging` (Vite detecta variables que comiencen con `VITE_`) con:

```bash
VITE_API_BASE_URL=https://staging-api.example.com
# otras variables necesarias para staging
```

- Ejecuta `npm run dev` y asegúrate que tu código lea `import.meta.env.VITE_API_BASE_URL` o tu `constants/config.js` use la variable apropiada.
- Alternativamente, exporta la variable en la sesión del terminal:

```powershell
$env:VITE_API_BASE_URL = 'https://staging-api.example.com'; npm run dev
```

### Checklist de QA (staging)

Antes de promover a producción, QA típicamente valida:
- Flujos de alta prioridad: login/registro, gestión de usuarios (crear/editar/eliminar/restaurar), gestión de clientes, ventas (si aplica).
- Validación de endpoints críticos y formatos (status codes, payloads).
- Validación UI en varios navegadores y tamaños de pantalla.
- Pruebas de seguridad básicas (acceso sin token, roles/privilegios).

---

Si quieres, puedo:
- Añadir un ejemplo de `workflow` para GitHub Actions / Vercel con ramas `staging` y `main`.
- Crear una plantilla `.env.example` que incluya `VITE_API_BASE_URL` y otras variables necesarias.

¿Quieres que añada el `workflow` y la plantilla `.env.example` ahora?
