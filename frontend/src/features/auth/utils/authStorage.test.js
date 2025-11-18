import { describe, test, expect, beforeEach, vi } from 'vitest'
import {
  getAuthToken,
  getUsername,
  getUserRole,
  setAuthToken,
  clearAuthData,
} from './authStorage.js'

describe('authStorage', () => {
  beforeEach(() => {
    localStorage.clear()
    vi.clearAllMocks()
  })

  describe('getAuthToken', () => {
    test('debe retornar el token cuando existe en localStorage', () => {
      localStorage.setItem('token', 'mi-token-123')
      expect(getAuthToken()).toBe('mi-token-123')
    })

    test('debe retornar null cuando no hay token', () => {
      expect(getAuthToken()).toBeNull()
    })

    test('debe retornar null cuando el token es una string vacía', () => {
      localStorage.setItem('token', '')
      expect(getAuthToken()).toBeNull()
    })
  })

  describe('getUsername', () => {
    test('debe retornar el username cuando existe en localStorage', () => {
      localStorage.setItem('username', 'alejandro')
      expect(getUsername()).toBe('alejandro')
    })

    test('debe retornar null cuando no hay username', () => {
      expect(getUsername()).toBeNull()
    })
  })

  describe('getUserRole', () => {
    test('debe retornar el role cuando existe en localStorage', () => {
      localStorage.setItem('role', 'admin')
      expect(getUserRole()).toBe('admin')
    })

    test('debe retornar null cuando no hay role', () => {
      expect(getUserRole()).toBeNull()
    })
  })

  describe('setAuthToken', () => {
    test('debe guardar el token en localStorage cuando se proporciona un token válido', () => {
      const token =
        'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFsZWphbmRybyIsInJvbGUiOiJhZG1pbiJ9.signature'
      setAuthToken(token)
      expect(localStorage.getItem('token')).toBe(token)
    })

    test('debe decodificar el JWT y guardar username y role cuando están presentes', () => {
      const token =
        'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFsZWphbmRybyIsInJvbGUiOiJhZG1pbiJ9.signature'
      setAuthToken(token)
      expect(localStorage.getItem('username')).toBe('alejandro')
      expect(localStorage.getItem('role')).toBe('admin')
    })

    test('debe limpiar localStorage cuando se proporciona null', () => {
      localStorage.setItem('token', 'token-existente')
      localStorage.setItem('username', 'usuario-existente')
      localStorage.setItem('role', 'role-existente')
      setAuthToken(null)
      expect(localStorage.getItem('token')).toBeNull()
      expect(localStorage.getItem('username')).toBeNull()
      expect(localStorage.getItem('role')).toBeNull()
    })

    test('debe limpiar localStorage cuando se proporciona undefined', () => {
      localStorage.setItem('token', 'token-existente')
      setAuthToken(undefined)
      expect(localStorage.getItem('token')).toBeNull()
    })

    test('debe manejar tokens JWT inválidos sin lanzar error', () => {
      const invalidToken = 'token-invalido'
      expect(() => setAuthToken(invalidToken)).not.toThrow()
      expect(localStorage.getItem('token')).toBe(invalidToken)
    })

    test('debe manejar JWT sin username o role', () => {
      const tokenWithoutUserData =
        'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJvdGhlciI6ImRhdGEifQ.signature'
      setAuthToken(tokenWithoutUserData)
      expect(localStorage.getItem('token')).toBe(tokenWithoutUserData)
      expect(localStorage.getItem('username')).toBeNull()
      expect(localStorage.getItem('role')).toBeNull()
    })
  })

  describe('clearAuthData', () => {
    test('debe eliminar token, username y role del localStorage', () => {
      localStorage.setItem('token', 'mi-token')
      localStorage.setItem('username', 'alejandro')
      localStorage.setItem('role', 'admin')
      clearAuthData()
      expect(localStorage.getItem('token')).toBeNull()
      expect(localStorage.getItem('username')).toBeNull()
      expect(localStorage.getItem('role')).toBeNull()
    })

    test('debe funcionar correctamente cuando localStorage está vacío', () => {
      expect(() => clearAuthData()).not.toThrow()
    })
  })
})

