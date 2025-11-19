# Stokia — Frontend

¡Bienvenido al frontend de **Stokia**! Esta es la interfaz de usuario de la plataforma.

## 🧰 Tecnologías utilizadas

| Logo | Tecnología     | Descripción                                                                 |
|------|----------------|-----------------------------------------------------------------------------|
| <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/react/react-original.svg" width="30"/> | **React**         | Libreria principal del frontend.           |
| <img src="https://vitejs.dev/logo.svg" width="30"/> | **Vite**          | Herramienta de compilación y entorno de desarrollo para frontend.                               |
| <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/javascript/javascript-original.svg" width="30"/> | **JavaScript**    | Lenguaje Base.                                     |
|<img src="https://upload.wikimedia.org/wikipedia/commons/d/d5/Tailwind_CSS_Logo.svg" width="30"/> | **Tailwind CSS**  | Framework de CSS para estilos.       |
|<img src="https://reactrouter.com/splash/hero-3d-logo.dark.webp" width="30"/> | **React Router**  | Manejo de rutas.       |
|<img src="https://raw.githubusercontent.com/TanStack/query/main/media/repo-dark.png" width="30"/> | **React Query**  | Manejo de estados asincronos y manejo de API.       |


## 🗂 Estructura del Proyecto

El proyecto Stokia esta organizado bajo una arquitectura por features, agrupando el codigo segun las funcionalidades del sistema, ademas el proyecto incluye carpetas para manejar utilidades compartidas y configuración global, manteniendo un orden claro entre logica de negocio y los componentes reutilizables. A continuación se describe la estructura de carpetas dentro de la carpeta `frontend`:

### 📁 `frontend/`
La carpeta principal donde se encuentra el código fuente del frontend de la aplicación.

#### 📁 `src/`
Contiene todos los archivos de código fuente necesarios para la aplicación.

- **📁 `assets/`**: 
  - Recursos estáticos.

- **📁 `features/`**: 
  - Contiene los modulos.

- **📁 `infrastructure/`**:
  - Dentro de esta carpeta se encuentran las configuraciones de API, rutas, constantes.

- **📁 `lib/`**:
  - Dentro de esta carpeta se encuentran las utiliddades y configuración de React Query.

- **📁 `shared/`**:
  - Dentro de esta carpeta se encuentran componentes, hooks y servicios reutilizables.

- **📁 `test/`**:
  - Pruebas.

Esta organización facilita mantener un código modular y facil de entender.

## 🚀 Comenzando

Sigue estos pasos para levantar el servidor de desarrollo:

### 1️⃣ Clona el repositorio
```bash
git clone URL repositorio
```
### 2️⃣ Accede a la carpeta del frontend
```bash
cd Equipo3-noche-SP7/frontend
```
### 3️⃣ Instala las dependencias
```bash
npm install
```
### 4️⃣ Inicia el servidor de desarrollo
```bash
npm run dev
```

## Despliegue del Frontend en Vercel

Este proyecto está desplegado en [Vercel](https://vercel.com).
- [Configuración de Vercel](./docs/VERCEL_CONFIG.md)

  
### Enlances Imnportantes

- 🚀 **Despliegue del Frontend**
  El frontend está desplegado y disponible en [Vercel]().








