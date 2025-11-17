---
name: Frontend Task
about: Crear una tarea técnica de frontend
title: '[FRONT] '
labels: frontend
assignees: ''
---

## ¿Cuándo usar este template?
- ✅ Tarea técnica de **frontend** que es parte de una User Story (se crea como sub-issue desde la US)
  - Ejemplo: Si la US es "Login y Registro" (#1), los subtasks serían: "Maquetar formulario" (relacionado con #1), "Validaciones frontend" (relacionado con #1), "Integrar con API" (relacionado con #1)
- ✅ Tarea técnica **independiente** de frontend (componentes, hooks, utilidades, etc.)
  - Ejemplo: "Crear componente Button", "Implementar hook useAuth", "Configurar Storybook"
- ❌ NO usar para bugs o refactors (se trabajan directamente sobre la issue original de bug/refactor)
- ❌ NO usar si es una funcionalidad nueva completa para el usuario (usa User Story o Feature Request en ese caso)

## User Story o Feature Relacionada (Opcional)
Si esta tarea es parte de una User Story o Feature más grande, indícalo aquí:
- Pertenece a: #issue-number
- Si es una tarea técnica independiente, deja este campo vacío

## Descripción de la Tarea
Descripción clara y técnica de lo que se debe implementar en esta tarea específica de frontend.

## Tipo de Tarea
- [ ] 🎨 UI/UX (Componentes, estilos, layouts)
- [ ] 🔌 Integración API (Conectar con endpoints del backend)
- [ ] 🧩 Componentes (Crear o modificar componentes React/Vue/etc.)
- [ ] 🎣 Hooks/Custom Hooks (Lógica reutilizable)
- [ ] 🗂️ Estado (Redux, Context, Zustand, etc.)
- [ ] ✅ Testing (Unitarios, integración, E2E)
- [ ] 📚 Documentación (Actualizar docs)
- [ ] ⚙️ Configuración (Build, bundler, herramientas)
- [ ] 🔍 Investigación (Spike, POC)
- [ ] ♿ Accesibilidad (Mejoras de accesibilidad)
- [ ] 📱 Responsive (Adaptación a diferentes tamaños)

**Nota:** Los bugs y refactors se trabajan directamente sobre la issue original (no se crean subtasks para ellos).

## Archivos/Áreas Afectadas
Lista los archivos, módulos o áreas del código que se verán afectados:
- `ruta/al/archivo.ts`
- `ruta/al/componente.tsx`
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
- [ ] Es responsive (si aplica)
- [ ] Cumple con estándares de accesibilidad (si aplica)

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

