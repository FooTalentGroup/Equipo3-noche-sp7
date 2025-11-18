import { describe, test, expect, vi, beforeEach } from 'vitest'
import { renderHook, waitFor } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { useLogin, useRegister, useLogout } from './useAuth.js'
import * as authService from '../services/authService.js'

vi.mock('../services/authService.js', () => ({
  login: vi.fn(),
  register: vi.fn(),
  logout: vi.fn(),
}))

function createWrapper() {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: { retry: false },
      mutations: { retry: false },
    },
  })

  return ({ children }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  )
}

describe('useAuth hooks', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('useLogin', () => {
    test('debe retornar mutation con estado inicial', () => {
      const { result } = renderHook(() => useLogin(), {
        wrapper: createWrapper(),
      })

      expect(result.current.isPending).toBe(false)
      expect(result.current.isError).toBe(false)
      expect(result.current.isSuccess).toBe(false)
      expect(result.current.data).toBeUndefined()
    })

    test('debe llamar a login service cuando se ejecuta mutate', async () => {
      const credentials = { username: 'test', password: '123456' }
      const mockResponse = { token: 'fake-token', user: { username: 'test' } }

      authService.login.mockResolvedValue(mockResponse)

      const { result } = renderHook(() => useLogin(), {
        wrapper: createWrapper(),
      })

      result.current.mutate(credentials)

      await waitFor(() => {
        expect(authService.login).toHaveBeenCalled()
        expect(authService.login.mock.calls[0][0]).toEqual(credentials)
      })
    })

    test('debe actualizar estado a success cuando login es exitoso', async () => {
      const credentials = { username: 'test', password: '123456' }
      const mockResponse = { token: 'fake-token' }

      authService.login.mockResolvedValue(mockResponse)

      const { result } = renderHook(() => useLogin(), {
        wrapper: createWrapper(),
      })

      result.current.mutate(credentials)

      await waitFor(() => {
        expect(result.current.isSuccess).toBe(true)
        expect(result.current.data).toEqual(mockResponse)
      })
    })

    test('debe actualizar estado a error cuando login falla', async () => {
      const credentials = { username: 'test', password: 'wrong' }
      const error = new Error('Invalid credentials')

      authService.login.mockRejectedValue(error)

      const { result } = renderHook(() => useLogin(), {
        wrapper: createWrapper(),
      })

      result.current.mutate(credentials)

      await waitFor(() => {
        expect(result.current.isError).toBe(true)
        expect(result.current.error).toEqual(error)
      })
    })

    test('debe ejecutar onSuccess callback cuando se proporciona', async () => {
      const credentials = { username: 'test', password: '123456' }
      const mockResponse = { token: 'fake-token' }
      const onSuccess = vi.fn()

      authService.login.mockResolvedValue(mockResponse)

      const { result } = renderHook(
        () => useLogin({ onSuccess }),
        {
          wrapper: createWrapper(),
        }
      )

      result.current.mutate(credentials)

      await waitFor(() => {
        expect(onSuccess).toHaveBeenCalledWith(
          mockResponse,
          credentials,
          undefined
        )
      })
    })

    test('debe ejecutar onError callback cuando se proporciona', async () => {
      const credentials = { username: 'test', password: 'wrong' }
      const error = new Error('Invalid credentials')
      const onError = vi.fn()

      authService.login.mockRejectedValue(error)

      const { result } = renderHook(
        () => useLogin({ onError }),
        {
          wrapper: createWrapper(),
        }
      )

      result.current.mutate(credentials)

      await waitFor(() => {
        expect(onError).toHaveBeenCalled()
      })
    })
  })

  describe('useRegister', () => {
    test('debe retornar mutation con estado inicial', () => {
      const { result } = renderHook(() => useRegister(), {
        wrapper: createWrapper(),
      })

      expect(result.current.isPending).toBe(false)
      expect(result.current.isError).toBe(false)
      expect(result.current.isSuccess).toBe(false)
    })

    test('debe llamar a register service cuando se ejecuta mutate', async () => {
      const userData = { username: 'newuser', password: '123456' }
      const mockResponse = { token: 'fake-token', user: { username: 'newuser' } }

      authService.register.mockResolvedValue(mockResponse)

      const { result } = renderHook(() => useRegister(), {
        wrapper: createWrapper(),
      })

      result.current.mutate(userData)

      await waitFor(() => {
        expect(authService.register).toHaveBeenCalled()
        expect(authService.register.mock.calls[0][0]).toEqual(userData)
      })
    })

    test('debe actualizar estado a success cuando register es exitoso', async () => {
      const userData = { username: 'newuser', password: '123456' }
      const mockResponse = { token: 'fake-token' }

      authService.register.mockResolvedValue(mockResponse)

      const { result } = renderHook(() => useRegister(), {
        wrapper: createWrapper(),
      })

      result.current.mutate(userData)

      await waitFor(() => {
        expect(result.current.isSuccess).toBe(true)
        expect(result.current.data).toEqual(mockResponse)
      })
    })

    test('debe actualizar estado a error cuando register falla', async () => {
      const userData = { username: 'existing', password: '123456' }
      const error = new Error('Username already exists')

      authService.register.mockRejectedValue(error)

      const { result } = renderHook(() => useRegister(), {
        wrapper: createWrapper(),
      })

      result.current.mutate(userData)

      await waitFor(() => {
        expect(result.current.isError).toBe(true)
        expect(result.current.error).toEqual(error)
      })
    })
  })

  describe('useLogout', () => {
    test('debe retornar mutation con estado inicial', () => {
      const { result } = renderHook(() => useLogout(), {
        wrapper: createWrapper(),
      })

      expect(result.current.isPending).toBe(false)
      expect(result.current.isError).toBe(false)
      expect(result.current.isSuccess).toBe(false)
    })

    test('debe llamar a logout service cuando se ejecuta mutate', async () => {
      authService.logout.mockResolvedValue()

      const { result } = renderHook(() => useLogout(), {
        wrapper: createWrapper(),
      })

      result.current.mutate()

      await waitFor(() => {
        expect(authService.logout).toHaveBeenCalled()
      })
    })

    test('debe actualizar estado a success cuando logout es exitoso', async () => {
      authService.logout.mockResolvedValue()

      const { result } = renderHook(() => useLogout(), {
        wrapper: createWrapper(),
      })

      result.current.mutate()

      await waitFor(() => {
        expect(result.current.isSuccess).toBe(true)
      })
    })

    test('debe actualizar estado a error cuando logout falla', async () => {
      const error = new Error('Logout failed')
      authService.logout.mockRejectedValue(error)

      const { result } = renderHook(() => useLogout(), {
        wrapper: createWrapper(),
      })

      result.current.mutate()

      await waitFor(() => {
        expect(result.current.isError).toBe(true)
        expect(result.current.error).toEqual(error)
      })
    })
  })
})

