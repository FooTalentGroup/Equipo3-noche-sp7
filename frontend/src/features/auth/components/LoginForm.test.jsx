import { describe, test, expect, vi, beforeEach } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { LoginForm } from './LoginForm.jsx'
import * as useAuth from '../hooks/useAuth.js'
import * as authStorage from '../utils/authStorage.js'

vi.mock('../hooks/useAuth.js')
vi.mock('../utils/authStorage.js')

const mockNavigate = vi.fn()
const mockLocation = { state: { from: { pathname: '/dashboard' } } }

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useLocation: () => mockLocation,
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

describe('LoginForm', () => {
  const mockMutate = vi.fn()

  beforeEach(() => {
    vi.clearAllMocks()
    mockNavigate.mockClear()

    useAuth.useLogin.mockReturnValue({
      mutate: mockMutate,
      isPending: false,
    })
  })

  test('debe renderizar el formulario correctamente', () => {
    render(<LoginForm />, { wrapper: createWrapper() })

    expect(screen.getAllByText('Iniciar Sesión').length).toBeGreaterThan(0)
    expect(screen.getByPlaceholderText(/username/i)).toBeInTheDocument()
    expect(screen.getByPlaceholderText(/password/i)).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /iniciar sesión/i })).toBeInTheDocument()
    expect(screen.getByText(/regístrate aquí/i)).toBeInTheDocument()
  })

  test('debe mostrar y ocultar la contraseña al hacer click', async () => {
    const user = userEvent.setup()
    render(<LoginForm />, { wrapper: createWrapper() })

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
    render(<LoginForm />, { wrapper: createWrapper() })

    const submitButton = screen.getByRole('button', { name: /iniciar sesión/i })
    await user.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText(/username.*obligatorio/i)).toBeInTheDocument()
    })
  })

  test('debe validar longitud mínima de username', async () => {
    const user = userEvent.setup()
    render(<LoginForm />, { wrapper: createWrapper() })

    const usernameInput = screen.getByLabelText(/username/i)
    const submitButton = screen.getByRole('button', { name: /iniciar sesión/i })

    await user.type(usernameInput, 'ab')
    await user.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText(/al menos 3 caracteres/i)).toBeInTheDocument()
    })
  })

  test('debe validar longitud mínima de password', async () => {
    const user = userEvent.setup()
    render(<LoginForm />, { wrapper: createWrapper() })

    const usernameInput = screen.getByPlaceholderText(/username/i)
    const passwordInput = screen.getByPlaceholderText(/password/i)
    const submitButton = screen.getByRole('button', { name: /iniciar sesión/i })

    await user.type(usernameInput, 'testuser')
    await user.type(passwordInput, '123')
    await user.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText(/al menos 6 caracteres/i)).toBeInTheDocument()
    })
  })

  test('debe llamar a login.mutate con datos válidos', async () => {
    const user = userEvent.setup()
    render(<LoginForm />, { wrapper: createWrapper() })

    const usernameInput = screen.getByPlaceholderText(/username/i)
    const passwordInput = screen.getByPlaceholderText(/password/i)
    const submitButton = screen.getByRole('button', { name: /iniciar sesión/i })

    await user.type(usernameInput, 'testuser')
    await user.type(passwordInput, '123456')
    await user.click(submitButton)

    await waitFor(() => {
      expect(mockMutate).toHaveBeenCalledWith({
        username: 'testuser',
        password: '123456',
      })
    })
  })

  test('debe mostrar estado de loading durante submit', () => {
    useAuth.useLogin.mockReturnValue({
      mutate: mockMutate,
      isPending: true,
    })

    render(<LoginForm />, { wrapper: createWrapper() })

    expect(screen.getByRole('button', { name: /iniciando sesión/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /iniciando sesión/i })).toBeDisabled()
  })

  test('debe manejar error de login y mostrar mensaje', async () => {
    const user = userEvent.setup()

    useAuth.useLogin.mockImplementation((options) => {
      setTimeout(() => {
        options.onError({ message: 'Credenciales inválidas' })
      }, 0)
      return {
        mutate: mockMutate,
        isPending: false,
      }
    })

    render(<LoginForm />, { wrapper: createWrapper() })

    const usernameInput = screen.getByPlaceholderText(/username/i)
    const passwordInput = screen.getByPlaceholderText(/password/i)
    const submitButton = screen.getByRole('button', { name: /iniciar sesión/i })

    await user.type(usernameInput, 'testuser')
    await user.type(passwordInput, '123456')
    await user.click(submitButton)

    await waitFor(() => {
      expect(authStorage.clearAuthData).toHaveBeenCalled()
    })
  })

  test('debe guardar token y navegar en login exitoso', async () => {
    const user = userEvent.setup()

    useAuth.useLogin.mockImplementation((options) => {
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

    render(<LoginForm />, { wrapper: createWrapper() })

    const usernameInput = screen.getByPlaceholderText(/username/i)
    const passwordInput = screen.getByPlaceholderText(/password/i)
    const submitButton = screen.getByRole('button', { name: /iniciar sesión/i })

    await user.type(usernameInput, 'testuser')
    await user.type(passwordInput, '123456')
    await user.click(submitButton)

    await waitFor(() => {
      expect(authStorage.setAuthToken).toHaveBeenCalledWith('fake-token-123')
    })
  })
})

