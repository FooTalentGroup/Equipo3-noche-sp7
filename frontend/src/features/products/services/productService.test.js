import { describe, test, expect, vi, beforeEach } from 'vitest'
import {
  getProducts,
  getProductById,
  createProduct,
  updateProduct,
  deleteProduct,
} from './productService.js'
import apiClient from '@/shared/services/apiClient.js'

vi.mock('@/shared/services/apiClient.js', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn(),
  },
}))

describe('productService', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getProducts', () => {
    test('debe llamar a apiClient.get con query params básicos', async () => {
      const mockResponse = { data: { products: [], total: 0 } }
      apiClient.get.mockResolvedValue(mockResponse)

      await getProducts()

      expect(apiClient.get).toHaveBeenCalledWith(
        '/api/products?page=0&size=20'
      )
      expect(apiClient.get).toHaveBeenCalledTimes(1)
    })

    test('debe construir query params con filtros de nombre', async () => {
      const mockResponse = { data: { products: [] } }
      apiClient.get.mockResolvedValue(mockResponse)

      await getProducts({ name: 'laptop' })

      expect(apiClient.get).toHaveBeenCalledWith(
        '/api/products?page=0&size=20&name=laptop'
      )
    })

    test('debe construir query params con filtros de categoría', async () => {
      const mockResponse = { data: { products: [] } }
      apiClient.get.mockResolvedValue(mockResponse)

      await getProducts({ category: 'Electrónica' })

      expect(apiClient.get).toHaveBeenCalledWith(
        '/api/products?page=0&size=20&category=Electr%C3%B3nica'
      )
    })

    test('debe construir query params con múltiples categorías', async () => {
      const mockResponse = { data: { products: [] } }
      apiClient.get.mockResolvedValue(mockResponse)

      await getProducts({ category: ['Electrónica', 'Hogar'] })

      const callUrl = apiClient.get.mock.calls[0][0]
      expect(callUrl).toContain('category=Electr')
      expect(callUrl).toContain('category=Hogar')
    })

    test('debe construir query params con rango de precios', async () => {
      const mockResponse = { data: { products: [] } }
      apiClient.get.mockResolvedValue(mockResponse)

      await getProducts({ minPrice: 10, maxPrice: 100 })

      const callUrl = apiClient.get.mock.calls[0][0]
      expect(callUrl).toContain('minPrice=10')
      expect(callUrl).toContain('maxPrice=100')
    })

    test('debe construir query params con sort', async () => {
      const mockResponse = { data: { products: [] } }
      apiClient.get.mockResolvedValue(mockResponse)

      await getProducts({ sort: 'price,asc' })

      expect(apiClient.get).toHaveBeenCalledWith(
        '/api/products?page=0&size=20&sort=price%2Casc'
      )
    })

    test('debe construir query params con sort como array', async () => {
      const mockResponse = { data: { products: [] } }
      apiClient.get.mockResolvedValue(mockResponse)

      await getProducts({ sort: ['price,asc', 'name,desc'] })

      const callUrl = apiClient.get.mock.calls[0][0]
      expect(callUrl).toContain('sort=price')
      expect(callUrl).toContain('sort=name')
    })

    test('debe construir query params con paginación personalizada', async () => {
      const mockResponse = { data: { products: [] } }
      apiClient.get.mockResolvedValue(mockResponse)

      await getProducts({ page: 2, size: 10 })

      expect(apiClient.get).toHaveBeenCalledWith(
        '/api/products?page=2&size=10'
      )
    })

    test('debe retornar los datos de la respuesta', async () => {
      const mockResponse = {
        data: { products: [{ id: 1, name: 'Product' }], total: 1 },
      }
      apiClient.get.mockResolvedValue(mockResponse)

      const result = await getProducts()

      expect(result).toEqual(mockResponse.data)
    })

    test('debe manejar todos los filtros combinados', async () => {
      const mockResponse = { data: { products: [] } }
      apiClient.get.mockResolvedValue(mockResponse)

      await getProducts({
        page: 1,
        size: 15,
        name: 'laptop',
        category: 'Electrónica',
        minPrice: 100,
        maxPrice: 1000,
        sort: 'price,asc',
      })

      const callUrl = apiClient.get.mock.calls[0][0]
      expect(callUrl).toContain('page=1')
      expect(callUrl).toContain('size=15')
      expect(callUrl).toContain('name=laptop')
      expect(callUrl).toContain('category=Electr')
      expect(callUrl).toContain('minPrice=100')
      expect(callUrl).toContain('maxPrice=1000')
      expect(callUrl).toContain('sort=price')
    })

    test('debe propagar errores de la API', async () => {
      const error = new Error('Network error')
      apiClient.get.mockRejectedValue(error)

      await expect(getProducts()).rejects.toThrow('Network error')
    })
  })

  describe('getProductById', () => {
    test('debe llamar a apiClient.get con el ID correcto', async () => {
      const mockResponse = { data: { id: 1, name: 'Product' } }
      apiClient.get.mockResolvedValue(mockResponse)

      await getProductById(1)

      expect(apiClient.get).toHaveBeenCalledWith('/api/products/1')
    })

    test('debe retornar los datos del producto', async () => {
      const mockResponse = { data: { id: 1, name: 'Product' } }
      apiClient.get.mockResolvedValue(mockResponse)

      const result = await getProductById(1)

      expect(result).toEqual(mockResponse.data)
    })

    test('debe propagar errores de la API', async () => {
      const error = new Error('Product not found')
      apiClient.get.mockRejectedValue(error)

      await expect(getProductById(999)).rejects.toThrow('Product not found')
    })
  })

  describe('createProduct', () => {
    test('debe llamar a apiClient.post con los datos correctos', async () => {
      const productData = {
        name: 'New Product',
        description: 'Description',
        price: 99.99,
        category: 'Electrónica',
      }
      const mockResponse = { data: { id: 1, ...productData } }
      apiClient.post.mockResolvedValue(mockResponse)

      await createProduct(productData)

      expect(apiClient.post).toHaveBeenCalledWith(
        '/api/products',
        productData
      )
    })

    test('debe retornar los datos del producto creado', async () => {
      const productData = { name: 'Product', price: 99.99 }
      const mockResponse = { data: { id: 1, ...productData } }
      apiClient.post.mockResolvedValue(mockResponse)

      const result = await createProduct(productData)

      expect(result).toEqual(mockResponse.data)
    })

    test('debe propagar errores de la API', async () => {
      const error = new Error('Validation failed')
      apiClient.post.mockRejectedValue(error)

      await expect(createProduct({})).rejects.toThrow('Validation failed')
    })
  })

  describe('updateProduct', () => {
    test('debe llamar a apiClient.put con el ID y datos correctos', async () => {
      const productData = { name: 'Updated Product' }
      const mockResponse = { data: { id: 1, ...productData } }
      apiClient.put.mockResolvedValue(mockResponse)

      await updateProduct(1, productData)

      expect(apiClient.put).toHaveBeenCalledWith(
        '/api/products/1',
        productData
      )
    })

    test('debe retornar los datos del producto actualizado', async () => {
      const productData = { name: 'Updated' }
      const mockResponse = { data: { id: 1, ...productData } }
      apiClient.put.mockResolvedValue(mockResponse)

      const result = await updateProduct(1, productData)

      expect(result).toEqual(mockResponse.data)
    })

    test('debe propagar errores de la API', async () => {
      const error = new Error('Product not found')
      apiClient.put.mockRejectedValue(error)

      await expect(updateProduct(999, {})).rejects.toThrow(
        'Product not found'
      )
    })
  })

  describe('deleteProduct', () => {
    test('debe llamar a apiClient.delete con el ID correcto', async () => {
      const mockResponse = { data: { success: true } }
      apiClient.delete.mockResolvedValue(mockResponse)

      await deleteProduct(1)

      expect(apiClient.delete).toHaveBeenCalledWith('/api/products/1')
    })

    test('debe retornar los datos de la respuesta', async () => {
      const mockResponse = { data: { success: true } }
      apiClient.delete.mockResolvedValue(mockResponse)

      const result = await deleteProduct(1)

      expect(result).toEqual(mockResponse.data)
    })

    test('debe propagar errores de la API', async () => {
      const error = new Error('Delete failed')
      apiClient.delete.mockRejectedValue(error)

      await expect(deleteProduct(999)).rejects.toThrow('Delete failed')
    })
  })
})

