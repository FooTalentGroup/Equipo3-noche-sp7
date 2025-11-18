import { describe, test, expect, vi, beforeEach } from 'vitest'
import { renderHook, waitFor } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import {
  useApiQuery,
  useApiMutation,
  useApiPost,
  useApiPut,
  useApiPatch,
  useApiDelete,
} from './useApi.js'
import apiClient from '../services/apiClient.js'

vi.mock('../services/apiClient.js', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    patch: vi.fn(),
    delete: vi.fn(),
  },
}))

function createWrapper() {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: { retry: false, gcTime: 0 },
      mutations: { retry: false },
    },
  })

  return ({ children }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  )
}

describe('useApi hooks', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('useApiQuery', () => {
    test('debe retornar estado inicial de loading', () => {
      apiClient.get.mockImplementation(() => new Promise(() => {}))

      const { result } = renderHook(
        () => useApiQuery(['test'], '/api/test'),
        { wrapper: createWrapper() }
      )

      expect(result.current.isLoading).toBe(true)
      expect(result.current.data).toBeUndefined()
    })

    test('debe retornar datos cuando la query es exitosa', async () => {
      const mockData = { id: 1, name: 'Test' }
      apiClient.get.mockResolvedValue({ data: mockData })

      const { result } = renderHook(
        () => useApiQuery(['test'], '/api/test'),
        { wrapper: createWrapper() }
      )

      await waitFor(() => {
        expect(result.current.isSuccess).toBe(true)
      })

    expect(result.current.data).toEqual(mockData)
    expect(apiClient.get).toHaveBeenCalled()
    })

    test('debe manejar errores correctamente', async () => {
      const error = new Error('Network error')
      apiClient.get.mockRejectedValue(error)

      const { result } = renderHook(
        () => useApiQuery(['test'], '/api/test'),
        { wrapper: createWrapper() }
      )

      await waitFor(() => {
        expect(result.current.isError).toBe(true)
      })

      expect(result.current.error).toEqual(error)
    })
  })

  describe('useApiMutation', () => {
    test('debe retornar estado inicial', () => {
      const mutationFn = vi.fn()

      const { result } = renderHook(
        () => useApiMutation(mutationFn),
        { wrapper: createWrapper() }
      )

      expect(result.current.isPending).toBe(false)
      expect(result.current.isError).toBe(false)
      expect(result.current.isSuccess).toBe(false)
    })

    test('debe ejecutar mutationFn cuando se llama mutate', async () => {
      const mutationFn = vi.fn().mockResolvedValue({ data: 'success' })

      const { result } = renderHook(
        () => useApiMutation(mutationFn),
        { wrapper: createWrapper() }
      )

      result.current.mutate({ test: 'data' })

      await waitFor(() => {
        expect(mutationFn).toHaveBeenCalled()
        expect(mutationFn.mock.calls[0][0]).toEqual({ test: 'data' })
      })
    })

    test('debe ejecutar onSuccess callback cuando se proporciona', async () => {
      const mutationFn = vi.fn().mockResolvedValue({ data: 'success' })
      const onSuccess = vi.fn()

      const { result } = renderHook(
        () => useApiMutation(mutationFn, { onSuccess }),
        { wrapper: createWrapper() }
      )

      result.current.mutate({})

      await waitFor(() => {
        expect(onSuccess).toHaveBeenCalled()
      })
    })

    test('debe ejecutar onError callback cuando falla', async () => {
      const error = new Error('Mutation failed')
      const mutationFn = vi.fn().mockRejectedValue(error)
      const onError = vi.fn()

      const { result } = renderHook(
        () => useApiMutation(mutationFn, { onError }),
        { wrapper: createWrapper() }
      )

      result.current.mutate({})

      await waitFor(() => {
        expect(onError).toHaveBeenCalled()
      })
    })
  })

  describe('useApiPost', () => {
    test('debe llamar a apiClient.post con la URL y datos correctos', async () => {
      const mockData = { name: 'Test' }
      apiClient.post.mockResolvedValue({ data: mockData })

      const { result } = renderHook(() => useApiPost('/api/products'), {
        wrapper: createWrapper(),
      })

      result.current.mutate({ name: 'Product' })

      await waitFor(() => {
        expect(apiClient.post).toHaveBeenCalledWith(
          '/api/products',
          { name: 'Product' },
          undefined
        )
      })
    })
  })

  describe('useApiPut', () => {
    test('debe llamar a apiClient.put con la URL y datos correctos', async () => {
      const mockData = { id: 1, name: 'Updated' }
      apiClient.put.mockResolvedValue({ data: mockData })

      const { result } = renderHook(() => useApiPut('/api/products/1'), {
        wrapper: createWrapper(),
      })

      result.current.mutate({ name: 'Updated' })

      await waitFor(() => {
        expect(apiClient.put).toHaveBeenCalledWith(
          '/api/products/1',
          { name: 'Updated' },
          undefined
        )
      })
    })
  })

  describe('useApiPatch', () => {
    test('debe llamar a apiClient.patch con la URL y datos correctos', async () => {
      const mockData = { id: 1, name: 'Patched' }
      apiClient.patch.mockResolvedValue({ data: mockData })

      const { result } = renderHook(() => useApiPatch('/api/products/1'), {
        wrapper: createWrapper(),
      })

      result.current.mutate({ name: 'Patched' })

      await waitFor(() => {
        expect(apiClient.patch).toHaveBeenCalledWith(
          '/api/products/1',
          { name: 'Patched' },
          undefined
        )
      })
    })
  })

  describe('useApiDelete', () => {
    test('debe llamar a apiClient.delete con la URL correcta', async () => {
      apiClient.delete.mockResolvedValue({ data: {} })

      const { result } = renderHook(() => useApiDelete('/api/products/1'), {
        wrapper: createWrapper(),
      })

      result.current.mutate()

      await waitFor(() => {
        expect(apiClient.delete).toHaveBeenCalledWith('/api/products/1', undefined)
      })
    })
  })
})

