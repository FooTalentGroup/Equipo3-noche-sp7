---
name: Backend Task
about: Crear una tarea técnica de backend
title: '[BACK] '
labels: backend
assignees: ''
---

## ¿Cuándo usar este template?
- ✅ Tarea técnica de **backend** que es parte de una User Story (se crea como sub-issue desde la US)
  - Ejemplo: Si la US es "Login y Registro" (#1), los subtasks serían: "Crear endpoint de login" (relacionado con #1), "Implementar validaciones" (relacionado con #1), "Crear tests" (relacionado con #1)
- ✅ Tarea técnica **independiente** de backend (endpoints, modelos, servicios, etc.)
  - Ejemplo: "Crear endpoint de usuarios", "Implementar servicio de autenticación", "Configurar base de datos"
- ❌ NO usar para bugs o refactors (se trabajan directamente sobre la issue original de bug/refactor)
- ❌ NO usar si es una funcionalidad nueva completa para el usuario (usa User Story o Feature Request en ese caso)

## User Story o Feature Relacionada (Opcional)
Si esta tarea es parte de una User Story o Feature más grande, indícalo aquí:
- Pertenece a: #issue-number
- Si es una tarea técnica independiente, deja este campo vacío

## Descripción de la Tarea
Descripción clara y técnica de lo que se debe implementar en esta tarea específica de backend.

## Tipo de Tarea
- [ ] 🔌 API/Endpoint (Crear o modificar endpoints)
- [ ] 🗄️ Base de Datos (Modelos, migraciones, queries)
- [ ] 🔐 Autenticación/Autorización
- [ ] 🛡️ Seguridad (Validaciones, sanitización, rate limiting)
- [ ] 📊 Lógica de Negocio (Servicios, casos de uso)
- [ ] ✅ Testing (Unitarios, integración, E2E)
- [ ] 📚 Documentación (Actualizar docs, Swagger/OpenAPI)
- [ ] ⚙️ Configuración (Setup, CI/CD, deployment)
- [ ] 🔍 Investigación (Spike, POC)
- [ ] 🚀 Performance (Optimización, caching)

**Nota:** Los bugs y refactors se trabajan directamente sobre la issue original (no se crean subtasks para ellos).

## Archivos/Áreas Afectadas
Lista los archivos, módulos o áreas del código que se verán afectados:
- `ruta/al/archivo.ts`
- `ruta/al/servicio.ts`
- etc.

## Implementación Propuesta
Describe brevemente cómo planeas implementar esta tarea:

```typescript
// Ejemplo de código o pseudocódigo si aplica
```

## Criterios de Completitud
- [ ] El código está implementado y funciona
- [ ] Los tests están escritos y pasan
- [ ] El código sigue las convenciones del proyecto
- [ ] Se ha actualizado la documentación si es necesario
- [ ] Se ha revisado el código (self-review)
- [ ] Se han validado los datos de entrada
- [ ] Se manejan los errores correctamente

## Dependencias Técnicas
- Depende de: #issue-number (debe completarse antes)
- Bloquea: #issue-number (esta tarea debe completarse antes)

## Notas de Implementación
Consideraciones técnicas, decisiones de diseño, o información relevante para el desarrollador:

## Checklist
- [ ] La tarea está claramente definida
- [ ] Se ha identificado la User Story/Feature padre (si aplica)
- [ ] Se han identificado las dependencias técnicas
- [ ] La estimación es realista (< 1 día idealmente)
- [ ] No es un bug o refactor (esos se trabajan sobre la issue original)

