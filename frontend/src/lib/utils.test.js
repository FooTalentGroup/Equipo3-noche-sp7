import { describe, test, expect } from 'vitest'
import { cn } from './utils.js'

describe('cn', () => {
  test('debe combinar clases correctamente', () => {
    const result = cn('class1', 'class2', 'class3')
    expect(result).toContain('class1')
    expect(result).toContain('class2')
    expect(result).toContain('class3')
  })

  test('debe manejar clases condicionales con valores falsy', () => {
    const condition = false
    const result = cn('class1', condition && 'class2', 'class3')
    expect(result).toContain('class1')
    expect(result).toContain('class3')
    expect(result).not.toContain('class2')
  })

  test('debe manejar clases condicionales con valores truthy', () => {
    const condition = true
    const result = cn('class1', condition && 'class2')
    expect(result).toContain('class1')
    expect(result).toContain('class2')
  })

  test('debe manejar arrays de clases', () => {
    const result = cn(['class1', 'class2'], 'class3')
    expect(result).toContain('class1')
    expect(result).toContain('class2')
    expect(result).toContain('class3')
  })

  test('debe manejar objetos con clases condicionales', () => {
    const result = cn({
      class1: true,
      class2: false,
      class3: true,
    })
    expect(result).toContain('class1')
    expect(result).toContain('class3')
    expect(result).not.toContain('class2')
  })

  test('debe eliminar clases duplicadas de Tailwind (última prevalece)', () => {
    const result = cn('p-4', 'p-6')
    expect(result).toBe('p-6')
  })

  test('debe manejar strings vacíos y undefined', () => {
    const result = cn('class1', '', undefined, null, 'class2')
    expect(result).toContain('class1')
    expect(result).toContain('class2')
  })

  test('debe retornar string vacío cuando no hay clases', () => {
    const result = cn()
    expect(result).toBe('')
  })
})

