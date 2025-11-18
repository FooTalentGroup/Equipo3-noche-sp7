import { describe, test, expect } from 'vitest'
import { productSchema } from './productValidators.js'

describe('productValidators', () => {
  describe('productSchema', () => {
    test('debe aceptar datos válidos', () => {
      const validData = {
        name: 'Producto Test',
        description: 'Descripción del producto',
        price: 99.99,
        category: 'Electrónica',
      }
      const result = productSchema.safeParse(validData)
      expect(result.success).toBe(true)
      if (result.success) {
        expect(result.data).toEqual(validData)
      }
    })

    test('debe rechazar cuando name está vacío', () => {
      const invalidData = {
        name: '',
        description: 'Descripción',
        price: 99.99,
        category: 'Electrónica',
      }
      const result = productSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const nameError = result.error.issues.find(
          issue => issue.path[0] === 'name'
        )
        expect(nameError?.message).toContain('requerido')
      }
    })

    test('debe rechazar name mayor a 100 caracteres', () => {
      const invalidData = {
        name: 'a'.repeat(101),
        description: 'Descripción',
        price: 99.99,
        category: 'Electrónica',
      }
      const result = productSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const nameError = result.error.issues.find(
          issue => issue.path[0] === 'name'
        )
        expect(nameError?.message).toContain('muy largo')
      }
    })

    test('debe aceptar name con exactamente 100 caracteres', () => {
      const validData = {
        name: 'a'.repeat(100),
        description: 'Descripción',
        price: 99.99,
        category: 'Electrónica',
      }
      const result = productSchema.safeParse(validData)
      expect(result.success).toBe(true)
    })

    test('debe rechazar cuando description está vacía', () => {
      const invalidData = {
        name: 'Producto',
        description: '',
        price: 99.99,
        category: 'Electrónica',
      }
      const result = productSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })

    test('debe rechazar description mayor a 500 caracteres', () => {
      const invalidData = {
        name: 'Producto',
        description: 'a'.repeat(501),
        price: 99.99,
        category: 'Electrónica',
      }
      const result = productSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const descError = result.error.issues.find(
          issue => issue.path[0] === 'description'
        )
        expect(descError?.message).toContain('muy larga')
      }
    })

    test('debe rechazar price menor a 0.01', () => {
      const invalidData = {
        name: 'Producto',
        description: 'Descripción',
        price: 0.005,
        category: 'Electrónica',
      }
      const result = productSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const priceError = result.error.issues.find(
          issue => issue.path[0] === 'price'
        )
        expect(priceError?.message).toContain('mayor a 0')
      }
    })

    test('debe aceptar price igual a 0.01', () => {
      const validData = {
        name: 'Producto',
        description: 'Descripción',
        price: 0.01,
        category: 'Electrónica',
      }
      const result = productSchema.safeParse(validData)
      expect(result.success).toBe(true)
    })

    test('debe rechazar price negativo', () => {
      const invalidData = {
        name: 'Producto',
        description: 'Descripción',
        price: -10,
        category: 'Electrónica',
      }
      const result = productSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })

    test('debe rechazar cuando price es undefined', () => {
      const invalidData = {
        name: 'Producto',
        description: 'Descripción',
        category: 'Electrónica',
      }
      const result = productSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const priceError = result.error.issues.find(
          issue => issue.path[0] === 'price'
        )
        expect(priceError).toBeDefined()
        expect(priceError?.message).toContain('number')
      }
    })

    test('debe rechazar cuando price no es un número', () => {
      const invalidData = {
        name: 'Producto',
        description: 'Descripción',
        price: 'no-es-numero',
        category: 'Electrónica',
      }
      const result = productSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const priceError = result.error.issues.find(
          issue => issue.path[0] === 'price'
        )
        expect(priceError).toBeDefined()
        expect(priceError?.message).toContain('number')
      }
    })

    test('debe rechazar cuando category está vacía', () => {
      const invalidData = {
        name: 'Producto',
        description: 'Descripción',
        price: 99.99,
        category: '',
      }
      const result = productSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const categoryError = result.error.issues.find(
          issue => issue.path[0] === 'category'
        )
        expect(categoryError?.message).toContain('requerida')
      }
    })
  })
})

