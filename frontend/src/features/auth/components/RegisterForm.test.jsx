import { describe, test, expect, vi, beforeEach } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { RegisterForm } from './RegisterForm.jsx'
import * as useAuth from '../hooks/useAuth.js'
import * as authStorage from '../utils/authStorage.js'
import { toast } from 'sonner'

vi.mock('../hooks/useAuth.js')
vi.mock('../utils/authStorage.js')
vi.mock('sonner', () => ({
  toast: {
    success: vi.fn(),
    error: vi.fn(),
    info: vi.fn(),
  },
}))

const mockNavigate = vi.fn()

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  }
})

const createWrapper = () => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: { retry: false },
      mutations: { retry: false },
    },
  })

  return ({ children }) => (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>{children}</BrowserRouter>
    </QueryClientProvider>
  )
}

describe('RegisterForm', () => {
  const mockMutate = vi.fn()

  beforeEach(() => {
    vi.clearAllMocks()
    mockNavigate.mockClear()
    toast.success.mockClear()
    toast.error.mockClear()
    toast.info.mockClear()

    useAuth.useRegister.mockReturnValue({
      mutate: mockMutate,
      isPending: false,
    })
  })

  test('debe renderizar el formulario correctamente', () => {
    render(<RegisterForm />, { wrapper: createWrapper() })

    expect(screen.getByText('Crear Cuenta')).toBeInTheDocument()
    expect(screen.getByText(/regístrate para comenzar/i)).toBeInTheDocument()
    expect(screen.getByPlaceholderText(/username/i)).toBeInTheDocument()
    expect(screen.getByPlaceholderText(/password/i)).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /registrarse/i })).toBeInTheDocument()
    expect(screen.getByText(/inicia sesión aquí/i)).toBeInTheDocument()
  })

  test('debe mostrar y ocultar la contraseña al hacer click', async () => {
    const user = userEvent.setup()
    render(<RegisterForm />, { wrapper: createWrapper() })

    const passwordInput = screen.getByPlaceholderText(/password/i)
    const toggleButton = screen.getByLabelText(/mostrar contraseña|ocultar contraseña/i)

    expect(passwordInput).toHaveAttribute('type', 'password')

    await user.click(toggleButton)
    expect(passwordInput).toHaveAttribute('type', 'text')

    await user.click(toggleButton)
    expect(passwordInput).toHaveAttribute('type', 'password')
  })

  test('debe validar campos requeridos', async () => {
    const user = userEvent.setup()
    render(<RegisterForm />, { wrapper: createWrapper() })

    const submitButton = screen.getByRole('button', { name: /registrarse/i })
    await user.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText(/username.*obligatorio/i)).toBeInTheDocument()
    })
  })

  test('debe validar longitud mínima de username', async () => {
    const user = userEvent.setup()
    render(<RegisterForm />, { wrapper: createWrapper() })

    const usernameInput = screen.getByLabelText(/username/i)
    const submitButton = screen.getByRole('button', { name: /registrarse/i })

    await user.type(usernameInput, 'ab')
    await user.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText(/al menos 3 caracteres/i)).toBeInTheDocument()
    })
  })

  test('debe validar longitud máxima de username', async () => {
    const user = userEvent.setup()
    render(<RegisterForm />, { wrapper: createWrapper() })

    const usernameInput = screen.getByLabelText(/username/i)
    const submitButton = screen.getByRole('button', { name: /registrarse/i })

    await user.type(usernameInput, 'a'.repeat(21))
    await user.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText(/más de 20 caracteres/i)).toBeInTheDocument()
    })
  })

  test('debe llamar a register.mutate con datos válidos', async () => {
    const user = userEvent.setup()
    render(<RegisterForm />, { wrapper: createWrapper() })

    const usernameInput = screen.getByPlaceholderText(/username/i)
    const passwordInput = screen.getByPlaceholderText(/password/i)
    const submitButton = screen.getByRole('button', { name: /registrarse/i })

    await user.type(usernameInput, 'newuser')
    await user.type(passwordInput, '123456')
    await user.click(submitButton)

    await waitFor(() => {
      expect(mockMutate).toHaveBeenCalledWith({
        username: 'newuser',
        password: '123456',
      })
    })
  })

  test('debe mostrar estado de loading durante submit', () => {
    useAuth.useRegister.mockReturnValue({
      mutate: mockMutate,
      isPending: true,
    })

    render(<RegisterForm />, { wrapper: createWrapper() })

    expect(screen.getByRole('button', { name: /registrando/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /registrando/i })).toBeDisabled()
  })

  test('debe guardar token, mostrar toast y navegar en registro exitoso', async () => {
    const user = userEvent.setup()

    useAuth.useRegister.mockImplementation((options) => {
      setTimeout(() => {
        options.onSuccess({
          success: true,
          data: {
            token: 'fake-token-123',
          },
        })
      }, 0)
      return {
        mutate: mockMutate,
        isPending: false,
      }
    })

    render(<RegisterForm />, { wrapper: createWrapper() })

    const usernameInput = screen.getByPlaceholderText(/username/i)
    const passwordInput = screen.getByPlaceholderText(/password/i)
    const submitButton = screen.getByRole('button', { name: /registrarse/i })

    await user.type(usernameInput, 'newuser')
    await user.type(passwordInput, '123456')
    await user.click(submitButton)

    await waitFor(() => {
      expect(authStorage.setAuthToken).toHaveBeenCalledWith('fake-token-123')
      expect(toast.success).toHaveBeenCalled()
      expect(mockNavigate).toHaveBeenCalledWith('/dashboard', { replace: true })
    })
  })

  test('debe manejar error de validación del servidor', async () => {
    const user = userEvent.setup()

    useAuth.useRegister.mockImplementation((options) => {
      setTimeout(() => {
        options.onError({
          response: {
            data: {
              error: 'VALIDATION_ERROR',
              message: 'Error de validación',
              details: {
                username: 'Username ya existe',
              },
            },
          },
        })
      }, 0)
      return {
        mutate: mockMutate,
        isPending: false,
      }
    })

    render(<RegisterForm />, { wrapper: createWrapper() })

    const usernameInput = screen.getByPlaceholderText(/username/i)
    const passwordInput = screen.getByPlaceholderText(/password/i)
    const submitButton = screen.getByRole('button', { name: /registrarse/i })

    await user.type(usernameInput, 'existing')
    await user.type(passwordInput, '123456')
    await user.click(submitButton)

    await waitFor(() => {
      expect(toast.error).toHaveBeenCalled()
      expect(authStorage.clearAuthData).toHaveBeenCalled()
    })
  })

  test('debe manejar error genérico', async () => {
    const user = userEvent.setup()

    useAuth.useRegister.mockImplementation((options) => {
      setTimeout(() => {
        options.onError({
          message: 'Error al registrarse',
        })
      }, 0)
      return {
        mutate: mockMutate,
        isPending: false,
      }
    })

    render(<RegisterForm />, { wrapper: createWrapper() })

    const usernameInput = screen.getByPlaceholderText(/username/i)
    const passwordInput = screen.getByPlaceholderText(/password/i)
    const submitButton = screen.getByRole('button', { name: /registrarse/i })

    await user.type(usernameInput, 'testuser')
    await user.type(passwordInput, '123456')
    await user.click(submitButton)

    await waitFor(() => {
      expect(toast.error).toHaveBeenCalled()
      expect(authStorage.clearAuthData).toHaveBeenCalled()
    })
  })
})

