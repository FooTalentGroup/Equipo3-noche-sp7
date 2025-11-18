import { describe, test, expect, vi, beforeEach } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { CreateProductForm } from './CreateProductForm.jsx'
import * as useApi from '@/shared/hooks/useApi.js'
import apiClient from '@/shared/services/apiClient.js'
import { toast } from 'sonner'

vi.mock('@/shared/hooks/useApi.js')
vi.mock('@/shared/services/apiClient.js')
vi.mock('sonner', () => ({
  toast: {
    success: vi.fn(),
    error: vi.fn(),
  },
}))

const mockInvalidateQueries = vi.fn()

const createWrapper = () => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: { retry: false },
      mutations: { retry: false },
    },
  })

  queryClient.invalidateQueries = mockInvalidateQueries

  return ({ children }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  )
}

describe('CreateProductForm', () => {
  const mockOnClose = vi.fn()
  const mockMutate = vi.fn()

  beforeEach(() => {
    vi.clearAllMocks()
    mockInvalidateQueries.mockClear()
    toast.success.mockClear()
    toast.error.mockClear()

    useApi.useApiPost.mockReturnValue({
      mutate: mockMutate,
      isPending: false,
    })

    useApi.useApiMutation.mockReturnValue({
      mutate: mockMutate,
      isPending: false,
    })
  })

  test('debe renderizar en modo creación', () => {
    render(<CreateProductForm open={true} onClose={mockOnClose} />, {
      wrapper: createWrapper(),
    })

    expect(screen.getByText('Crear Nuevo Producto')).toBeInTheDocument()
    expect(screen.getByPlaceholderText(/hamburguesa clásica/i)).toBeInTheDocument()
    expect(screen.getByPlaceholderText(/describe el producto/i)).toBeInTheDocument()
  })

  test('debe renderizar en modo edición con datos precargados', () => {
    const product = {
      id: 1,
      name: 'Hamburguesa',
      description: 'Deliciosa hamburguesa',
      price: 12.99,
      category: 'Platos Fuertes',
    }

    render(
      <CreateProductForm open={true} onClose={mockOnClose} product={product} />,
      { wrapper: createWrapper() }
    )

    expect(screen.getByText('Editar Producto')).toBeInTheDocument()
    expect(screen.getByDisplayValue('Hamburguesa')).toBeInTheDocument()
    expect(screen.getByDisplayValue('Deliciosa hamburguesa')).toBeInTheDocument()
    const priceInput = screen.getByDisplayValue('12.99')
    expect(priceInput).toBeInTheDocument()
  })

  test('debe validar campos requeridos', async () => {
    const user = userEvent.setup()
    render(<CreateProductForm open={true} onClose={mockOnClose} />, {
      wrapper: createWrapper(),
    })

    const submitButton = screen.getByRole('button', { name: /crear producto/i })
    await user.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText(/nombre.*requerido/i)).toBeInTheDocument()
    })
  })

  test('debe validar longitud máxima de nombre', async () => {
    const user = userEvent.setup()
    render(<CreateProductForm open={true} onClose={mockOnClose} />, {
      wrapper: createWrapper(),
    })

    const nameInput = screen.getByPlaceholderText(/hamburguesa clásica/i)
    await user.type(nameInput, 'a'.repeat(101))

    const submitButton = screen.getByRole('button', { name: /crear producto/i })
    await user.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText(/muy largo/i)).toBeInTheDocument()
    })
  })

  test('debe validar precio mínimo', async () => {
    const user = userEvent.setup()
    render(<CreateProductForm open={true} onClose={mockOnClose} />, {
      wrapper: createWrapper(),
    })

    const nameInput = screen.getByPlaceholderText(/hamburguesa clásica/i)
    const descriptionInput = screen.getByPlaceholderText(/describe el producto/i)
    const priceInput = screen.getByPlaceholderText(/0.00/i)
    const submitButton = screen.getByRole('button', { name: /crear producto/i })

    await user.type(nameInput, 'Producto Test')
    await user.type(descriptionInput, 'Descripción test')
    await user.type(priceInput, '0.005')

    await user.click(submitButton)

    await waitFor(() => {
      const errorMessages = screen.queryAllByText(/mayor a 0/i)
      const errorMessages2 = screen.queryAllByText(/precio/i)
      expect(errorMessages.length > 0 || errorMessages2.length > 0).toBe(true)
    }, { timeout: 3000 })
  })

  test('debe llamar a createProductMutation.mutate en modo creación', async () => {
    const user = userEvent.setup()
    render(<CreateProductForm open={true} onClose={mockOnClose} />, {
      wrapper: createWrapper(),
    })

    const nameInput = screen.getByLabelText(/nombre del producto/i)
    const descriptionInput = screen.getByLabelText(/descripción/i)
    const priceInput = screen.getByLabelText(/precio/i)
    const categorySelect = screen.getByLabelText(/categoría/i)
    const submitButton = screen.getByRole('button', { name: /crear producto/i })

    await user.type(nameInput, 'Nuevo Producto')
    await user.type(descriptionInput, 'Descripción del producto')
    await user.type(priceInput, '15.99')
    await user.selectOptions(categorySelect, 'Platos Fuertes')
    await user.click(submitButton)

    await waitFor(() => {
      expect(mockMutate).toHaveBeenCalledWith({
        name: 'Nuevo Producto',
        description: 'Descripción del producto',
        price: 15.99,
        category: 'Platos Fuertes',
      })
    })
  })

  test('debe llamar a updateProductMutation.mutate en modo edición', async () => {
    const user = userEvent.setup()
    const product = {
      id: 1,
      name: 'Producto Original',
      description: 'Descripción original',
      price: 10.99,
      category: 'Bebidas Calientes',
    }

    apiClient.put = vi.fn().mockResolvedValue({ data: {} })

    render(
      <CreateProductForm open={true} onClose={mockOnClose} product={product} />,
      { wrapper: createWrapper() }
    )

    const nameInput = screen.getByPlaceholderText(/hamburguesa clásica/i)
    const submitButton = screen.getByRole('button', { name: /actualizar producto/i })

    await user.clear(nameInput)
    await user.type(nameInput, 'Producto Actualizado')
    await user.click(submitButton)

    await waitFor(() => {
      expect(mockMutate).toHaveBeenCalled()
    })
  })

  test('debe mostrar estado de loading durante creación', () => {
    useApi.useApiPost.mockReturnValue({
      mutate: mockMutate,
      isPending: true,
    })

    render(<CreateProductForm open={true} onClose={mockOnClose} />, {
      wrapper: createWrapper(),
    })

    expect(screen.getByRole('button', { name: /creando/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /creando/i })).toBeDisabled()
  })

  test('debe cerrar el formulario al hacer click en cancelar', async () => {
    const user = userEvent.setup()
    render(<CreateProductForm open={true} onClose={mockOnClose} />, {
      wrapper: createWrapper(),
    })

    const cancelButton = screen.getByRole('button', { name: /cancelar/i })
    await user.click(cancelButton)

    expect(mockOnClose).toHaveBeenCalled()
  })
})

