# 🛍️ Stokia – Panel de Gestión para Emprendedores

## 🚀 Descripción General

**Stokia** es una aplicación web integral diseñada para **emprendedores y empresas**. Su misión es digitalizar y simplificar la gestión administrativa centralizando el **control de stock, productos, clientes, proveedores y ventas**. Ofrece un panel intuitivo con funcionalidades clave como registro de ventas, alertas automatizadas y generación de reportes detallados y visualizaciones que permiten tomar decisiones de negocio informadas.

---

## ⚙ Tecnologías Utilizadas

Stokia utiliza un *stack* moderno dividido entre un frontend reactivo y un backend robusto basado en Java.

### 🧩 Frontend – React + Vite

| Área | Tecnología | Detalle / Propósito |
| :--- | :--- | :--- |
| **Librería Principal** | **React** (a través de Vite) | Construcción de la UI. |
| **Estilado** | **Tailwind CSS** | Estilado rápido basado en utilidades. |
| **Estado Asíncrono** | **@tanstack/react-query** | Caching, fetching y sincronización de datos. |
| **Cliente HTTP** | **Axios** (mediante `apiClient`) | Consumo de la API REST. |
| **Rutas** | **React Router DOM** | Manejo de rutas y navegación. |

### 🧱 Backend – Java + Spring Boot

| Área | Tecnología | Detalle / Propósito |
| :--- | :--- | :--- |
| **Framework Principal** | **Spring Boot** | 3.3.1 |
| **Lenguaje** | **Java** | JDK 17. |
| **Bases de Datos** | **MySQL / MariaDB** | Motores soportados para almacenamiento. |
| **Documentación** | **Springdoc OpenAPI** | Generación de Swagger UI (`/swagger-ui.html`). |

---

## 🏗️ Arquitectura del Proyecto

### 📌 Frontend: Arquitectura por Features

El frontend (`/frontend/src/`) adopta la arquitectura de **Feature Folders**, donde cada funcionalidad reside en su propia carpeta, encapsulando componentes, servicios, *hooks* y páginas relacionadas.

* **`src/features/`**: Lógica de negocio modularizada (e.g., `auth/`, `users/`, `customers/`, `products/`, `sales/`).
* **`src/infrastructure/`**: Configuración global (ej. `api/axios.config.js`, `router/`).
* **`src/shared/`**: Componentes, *hooks* y servicios altamente reutilizables.

### 📌 Backend: Arquitectura en Capas (Spring Boot)

El backend (`/backend/src/main/java/fooTalent/misino`) sigue el patrón de Arquitectura en Capas:

* **`controller/`**: Expone los *endpoints* REST.
* **`service/`**: Contiene la lógica de negocio principal.
* **`repository/`**: Abstracción del acceso a datos (JPA).
* **`auth/`**: Manejo de seguridad, registro y generación/validación de JWT.

---

## 🛠️ Instrucciones para Correr Localmente

### 1. Requisitos Previos

* **Node.js** (v18+) y **npm** (para Frontend).
* **JDK 17** y **Maven** (para Backend).
* **Base de Datos** (MySQL/MariaDB) corriendo localmente.

### 2. Clonar el Repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd stokia
