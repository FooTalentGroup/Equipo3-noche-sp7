import { describe, test, expect, vi, beforeEach } from 'vitest'
import { login, register, logout } from './authService.js'
import apiClient from '@/shared/services/apiClient.js'

vi.mock('@/shared/services/apiClient.js', () => ({
  default: {
    post: vi.fn(),
  },
}))

describe('authService', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('login', () => {
    test('debe llamar a apiClient.post con el endpoint y credenciales correctas', async () => {
      const credentials = {
        username: 'alejandro',
        password: '123456',
      }
      const mockResponse = {
        data: {
          token: 'fake-token',
          user: { username: 'alejandro' },
        },
      }

      apiClient.post.mockResolvedValue(mockResponse)

      const result = await login(credentials)

      expect(apiClient.post).toHaveBeenCalledWith(
        '/api/auth/login',
        credentials
      )
      expect(result).toEqual(mockResponse.data)
    })

    test('debe retornar los datos de la respuesta', async () => {
      const credentials = { username: 'test', password: '123456' }
      const mockResponse = {
        data: {
          token: 'token-123',
          user: { username: 'test' },
        },
      }

      apiClient.post.mockResolvedValue(mockResponse)

      const result = await login(credentials)
      expect(result).toEqual(mockResponse.data)
    })

    test('debe propagar errores de la API', async () => {
      const credentials = { username: 'test', password: 'wrong' }
      const error = new Error('Invalid credentials')

      apiClient.post.mockRejectedValue(error)

      await expect(login(credentials)).rejects.toThrow('Invalid credentials')
    })
  })

  describe('register', () => {
    test('debe llamar a apiClient.post con el endpoint y datos de usuario correctos', async () => {
      const userData = {
        username: 'nuevo_usuario',
        password: '123456',
      }
      const mockResponse = {
        data: {
          token: 'new-token',
          user: { username: 'nuevo_usuario' },
        },
      }

      apiClient.post.mockResolvedValue(mockResponse)

      const result = await register(userData)

      expect(apiClient.post).toHaveBeenCalledWith(
        '/api/auth/register',
        userData
      )
      expect(result).toEqual(mockResponse.data)
    })

    test('debe retornar los datos de la respuesta', async () => {
      const userData = { username: 'test', password: '123456' }
      const mockResponse = {
        data: {
          token: 'token-456',
          user: { username: 'test' },
        },
      }

      apiClient.post.mockResolvedValue(mockResponse)

      const result = await register(userData)
      expect(result).toEqual(mockResponse.data)
    })

    test('debe propagar errores de la API', async () => {
      const userData = { username: 'test', password: '123456' }
      const error = new Error('Username already exists')

      apiClient.post.mockRejectedValue(error)

      await expect(register(userData)).rejects.toThrow(
        'Username already exists'
      )
    })
  })

  describe('logout', () => {
    test('debe llamar a apiClient.post con el endpoint correcto', async () => {
      apiClient.post.mockResolvedValue({})

      await logout()

      expect(apiClient.post).toHaveBeenCalledWith('/api/auth/logout')
      expect(apiClient.post).toHaveBeenCalledTimes(1)
    })

    test('debe completar sin error cuando la API responde correctamente', async () => {
      apiClient.post.mockResolvedValue({})

      await expect(logout()).resolves.not.toThrow()
    })

    test('debe propagar errores de la API', async () => {
      const error = new Error('Logout failed')
      apiClient.post.mockRejectedValue(error)

      await expect(logout()).rejects.toThrow('Logout failed')
    })
  })
})

