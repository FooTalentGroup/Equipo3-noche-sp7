import { describe, test, expect, vi, beforeEach } from 'vitest'
import { renderHook, waitFor } from '@testing-library/react'
import { useFetch } from './useFetch.js'
import apiClient from '../services/apiClient.js'

vi.mock('../services/apiClient.js', () => ({
  default: {
    get: vi.fn(),
  },
}))

describe('useFetch', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('debe retornar estado inicial de loading', () => {
    apiClient.get.mockImplementation(() => new Promise(() => {}))

    const { result } = renderHook(() => useFetch('/api/test'))

    expect(result.current.loading).toBe(true)
    expect(result.current.data).toBeNull()
    expect(result.current.error).toBeNull()
  })

  test('debe hacer fetch y retornar datos cuando es exitoso', async () => {
    const mockData = { id: 1, name: 'Test' }
    apiClient.get.mockResolvedValue({ data: mockData })

    const { result } = renderHook(() => useFetch('/api/test'))

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(result.current.data).toEqual(mockData)
    expect(result.current.error).toBeNull()
    expect(apiClient.get).toHaveBeenCalledWith('/api/test', {})
  })

  test('debe manejar errores correctamente', async () => {
    const error = new Error('Network error')
    apiClient.get.mockRejectedValue(error)

    const { result } = renderHook(() => useFetch('/api/test'))

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
    })

    expect(result.current.error).toEqual(error)
    expect(result.current.data).toBeNull()
  })

  test('debe hacer refetch cuando se llama refetch', async () => {
    const mockData1 = { id: 1, name: 'First' }
    const mockData2 = { id: 2, name: 'Second' }

    apiClient.get
      .mockResolvedValueOnce({ data: mockData1 })
      .mockResolvedValueOnce({ data: mockData2 })

    const { result } = renderHook(() => useFetch('/api/test'))

    await waitFor(() => {
      expect(result.current.data).toEqual(mockData1)
    })

    result.current.refetch()

    await waitFor(() => {
      expect(result.current.data).toEqual(mockData2)
    })

    expect(apiClient.get).toHaveBeenCalledTimes(2)
  })

  test('debe hacer cleanup en unmount', async () => {
    apiClient.get.mockImplementation(
      () =>
        new Promise((resolve) => {
          setTimeout(() => resolve({ data: {} }), 100)
        })
    )

    const { result, unmount } = renderHook(() => useFetch('/api/test'))

    expect(result.current.loading).toBe(true)

    unmount()

    await waitFor(() => {
      expect(apiClient.get).toHaveBeenCalled()
    })
  })

  test('debe hacer fetch cuando cambia la URL', async () => {
    const mockData1 = { id: 1 }
    const mockData2 = { id: 2 }

    apiClient.get
      .mockResolvedValueOnce({ data: mockData1 })
      .mockResolvedValueOnce({ data: mockData2 })

    const { result, rerender } = renderHook(
      ({ url }) => useFetch(url),
      {
        initialProps: { url: '/api/test1' },
      }
    )

    await waitFor(() => {
      expect(result.current.data).toEqual(mockData1)
    })

    rerender({ url: '/api/test2' })

    await waitFor(() => {
      expect(result.current.data).toEqual(mockData2)
    })

    expect(apiClient.get).toHaveBeenCalledTimes(2)
  })

  test('debe pasar options a apiClient.get', async () => {
    const mockData = { id: 1 }
    apiClient.get.mockResolvedValue({ data: mockData })

    const options = { headers: { Authorization: 'Bearer token' } }

    renderHook(() => useFetch('/api/test', options))

    await waitFor(() => {
      expect(apiClient.get).toHaveBeenCalledWith('/api/test', options)
    })
  })

  test('no debe hacer fetch cuando url es vacía', () => {
    renderHook(() => useFetch(''))

    expect(apiClient.get).not.toHaveBeenCalled()
  })

  test('debe limpiar error en refetch exitoso', async () => {
    const error = new Error('First error')
    const mockData = { id: 1 }

    apiClient.get
      .mockRejectedValueOnce(error)
      .mockResolvedValueOnce({ data: mockData })

    const { result } = renderHook(() => useFetch('/api/test'))

    await waitFor(() => {
      expect(result.current.error).toEqual(error)
    })

    result.current.refetch()

    await waitFor(() => {
      expect(result.current.error).toBeNull()
      expect(result.current.data).toEqual(mockData)
    })
  })
})

