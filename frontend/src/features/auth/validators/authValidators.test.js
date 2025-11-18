import { describe, test, expect } from 'vitest'
import { loginSchema, registerSchema } from './authValidators.js'

describe('authValidators', () => {
  describe('loginSchema', () => {
    test('debe aceptar datos válidos', () => {
      const validData = {
        username: 'alejandro',
        password: '123456',
      }
      const result = loginSchema.safeParse(validData)
      expect(result.success).toBe(true)
      if (result.success) {
        expect(result.data).toEqual(validData)
      }
    })

    test('debe rechazar username con menos de 3 caracteres', () => {
      const invalidData = {
        username: 'ab',
        password: '123456',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        expect(result.error.issues[0].message).toContain('al menos 3 caracteres')
      }
    })

    test('debe rechazar username con caracteres especiales', () => {
      const invalidData = {
        username: 'alejandro!',
        password: '123456',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        expect(result.error.issues[0].message).toContain(
          'letras, números y guiones bajos'
        )
      }
    })

    test('debe aceptar username con guiones bajos', () => {
      const validData = {
        username: 'alejandro_123',
        password: '123456',
      }
      const result = loginSchema.safeParse(validData)
      expect(result.success).toBe(true)
    })

    test('debe rechazar password con menos de 6 caracteres', () => {
      const invalidData = {
        username: 'alejandro',
        password: '123',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        expect(result.error.issues[0].message).toContain('al menos 6 caracteres')
      }
    })

    test('debe rechazar cuando username está vacío', () => {
      const invalidData = {
        username: '',
        password: '123456',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })

    test('debe rechazar cuando password está vacío', () => {
      const invalidData = {
        username: 'alejandro',
        password: '',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })

    test('debe rechazar cuando username es undefined', () => {
      const invalidData = {
        password: '123456',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })

    test('debe rechazar cuando password es undefined', () => {
      const invalidData = {
        username: 'alejandro',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })

    test('debe verificar mensaje de error para username obligatorio', () => {
      const invalidData = {
        username: '',
        password: '123456',
      }
      const result = loginSchema.safeParse(invalidData)
      if (!result.success) {
        const usernameError = result.error.issues.find(
          issue => issue.path[0] === 'username'
        )
        expect(usernameError?.message).toContain('obligatorio')
      }
    })
  })

  describe('registerSchema', () => {
    test('debe aceptar datos válidos', () => {
      const validData = {
        username: 'alejandro',
        password: '123456',
      }
      const result = registerSchema.safeParse(validData)
      expect(result.success).toBe(true)
      if (result.success) {
        expect(result.data).toEqual(validData)
      }
    })

    test('debe rechazar username mayor a 20 caracteres', () => {
      const invalidData = {
        username: 'a'.repeat(21),
        password: '123456',
      }
      const result = registerSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        expect(result.error.issues[0].message).toContain('más de 20 caracteres')
      }
    })

    test('debe aceptar username con exactamente 20 caracteres', () => {
      const validData = {
        username: 'a'.repeat(20),
        password: '123456',
      }
      const result = registerSchema.safeParse(validData)
      expect(result.success).toBe(true)
    })

    test('debe rechazar password mayor a 50 caracteres', () => {
      const invalidData = {
        username: 'alejandro',
        password: 'a'.repeat(51),
      }
      const result = registerSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        expect(result.error.issues[0].message).toContain('más de 50 caracteres')
      }
    })

    test('debe aceptar password con exactamente 50 caracteres', () => {
      const validData = {
        username: 'alejandro',
        password: 'a'.repeat(50),
      }
      const result = registerSchema.safeParse(validData)
      expect(result.success).toBe(true)
    })

    test('debe aplicar todas las validaciones de loginSchema', () => {
      const invalidData = {
        username: 'ab',
        password: '123',
      }
      const result = registerSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })

    test('debe rechazar username con caracteres especiales (igual que loginSchema)', () => {
      const invalidData = {
        username: 'alejandro!',
        password: '123456',
      }
      const result = registerSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })
  })
})

