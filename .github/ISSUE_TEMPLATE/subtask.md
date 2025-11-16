---
name: Sub-tarea Técnica
about: Crear una sub-tarea técnica partiendo de una User Story o Feature
title: '[SUBTASK] '
labels: subtask, technical
assignees: ''
---

## ¿Cuándo usar este template?
- ✅ Tarea técnica que es **parte de una User Story** (se crea como sub-issue desde la US)
  - Ejemplo: Si la US es "Login y Registro" (#1), los subtasks serían: "Maquetar formulario" (relacionado con #1), "Conectar con API" (relacionado con #1), "Validaciones" (relacionado con #1)
- ✅ Tarea técnica **independiente** (configuración, documentación, investigación, etc.)
  - Ejemplo: "Configurar CI/CD", "Documentar API", "Investigar librería X"
- ❌ NO usar para bugs o refactors (se trabajan directamente sobre la issue original de bug/refactor)
- ❌ NO usar si es una funcionalidad nueva completa para el usuario (usa User Story o Feature Request en ese caso)

## User Story o Feature Relacionada (Opcional)
Si esta sub-tarea es parte de una User Story o Feature más grande, indícalo aquí:
- Pertenece a: #issue-number
- Si es una tarea técnica independiente, deja este campo vacío

## Descripción de la Sub-tarea
Descripción clara y técnica de lo que se debe implementar en esta sub-tarea específica.

## Tipo de Tarea
- [ ] 🎨 UI/UX (Componentes, estilos, layouts)
- [ ] 🔌 API/Endpoint (Crear o modificar endpoints)
- [ ] 🗄️ Base de Datos (Modelos, migraciones, queries)
- [ ] 🔐 Autenticación/Autorización
- [ ] ✅ Testing (Unitarios, integración, E2E)
- [ ] 📚 Documentación (Actualizar docs)
- [ ] ⚙️ Configuración (Setup, CI/CD, deployment)
- [ ] 🔍 Investigación (Spike, POC)

**Nota:** Los bugs y refactors se trabajan directamente sobre la issue original (no se crean subtasks para ellos).

## Componente
- [ ] Frontend
- [ ] Backend
- [ ] Fullstack

## Archivos/Áreas Afectadas
Lista los archivos, módulos o áreas del código que se verán afectados:
- `ruta/al/archivo.ts`
- `ruta/al/componente.tsx`
- etc.

## Implementación Propuesta
Describe brevemente cómo planeas implementar esta sub-tarea:

```typescript
// Ejemplo de código o pseudocódigo si aplica
```

## Criterios de Completitud
- [ ] El código está implementado y funciona
- [ ] Los tests están escritos y pasan
- [ ] El código sigue las convenciones del proyecto
- [ ] Se ha actualizado la documentación si es necesario
- [ ] Se ha revisado el código (self-review)

## Dependencias Técnicas
- Depende de: #issue-number (debe completarse antes)
- Bloquea: #issue-number (esta tarea debe completarse antes)

## Notas de Implementación
Consideraciones técnicas, decisiones de diseño, o información relevante para el desarrollador:

## Checklist
- [ ] La sub-tarea está claramente definida
- [ ] Se ha identificado la User Story/Feature padre (si aplica)
- [ ] Se han identificado las dependencias técnicas
- [ ] La estimación es realista (< 1 día idealmente)
- [ ] No es un bug o refactor (esos se trabajan sobre la issue original)

