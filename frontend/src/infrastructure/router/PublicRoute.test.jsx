import { describe, test, expect, vi, beforeEach } from 'vitest'
import { render, screen } from '@testing-library/react'
import { createMemoryRouter, RouterProvider } from 'react-router-dom'
import { PublicRoute } from './PublicRoute.jsx'
import * as authStorage from '@/features/auth/utils/authStorage.js'

vi.mock('@/features/auth/utils/authStorage.js')

describe('PublicRoute', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('debe renderizar children cuando no hay token', () => {
    authStorage.getAuthToken.mockReturnValue(null)

    const router = createMemoryRouter(
      [
        {
          path: '/login',
          element: (
            <PublicRoute>
              <div>Login Form</div>
            </PublicRoute>
          ),
        },
      ],
      { initialEntries: ['/login'] }
    )

    render(<RouterProvider router={router} />)

    expect(screen.getByText('Login Form')).toBeInTheDocument()
  })

  test('debe redirigir a /dashboard cuando hay token', () => {
    authStorage.getAuthToken.mockReturnValue('fake-token')

    const router = createMemoryRouter(
      [
        {
          path: '/login',
          element: (
            <PublicRoute>
              <div>Login Form</div>
            </PublicRoute>
          ),
        },
        {
          path: '/dashboard',
          element: <div>Dashboard</div>,
        },
      ],
      { initialEntries: ['/login'] }
    )

    render(<RouterProvider router={router} />)

    expect(screen.getByText('Dashboard')).toBeInTheDocument()
    expect(screen.queryByText('Login Form')).not.toBeInTheDocument()
  })

  test('debe usar replace en navegación', () => {
    authStorage.getAuthToken.mockReturnValue('fake-token')

    const router = createMemoryRouter(
      [
        {
          path: '/login',
          element: (
            <PublicRoute>
              <div>Login Form</div>
            </PublicRoute>
          ),
        },
        {
          path: '/dashboard',
          element: <div>Dashboard</div>,
        },
      ],
      { initialEntries: ['/login'] }
    )

    render(<RouterProvider router={router} />)

    expect(router.state.historyAction).toBe('REPLACE')
  })

  test('debe permitir acceso cuando token es string vacía (tratado como null)', () => {
    authStorage.getAuthToken.mockReturnValue('')

    const router = createMemoryRouter(
      [
        {
          path: '/login',
          element: (
            <PublicRoute>
              <div>Login Form</div>
            </PublicRoute>
          ),
        },
        {
          path: '/dashboard',
          element: <div>Dashboard</div>,
        },
      ],
      { initialEntries: ['/login'] }
    )

    render(<RouterProvider router={router} />)

    expect(screen.getByText('Login Form')).toBeInTheDocument()
  })

  test('debe permitir acceso cuando token es null', () => {
    authStorage.getAuthToken.mockReturnValue(null)

    const router = createMemoryRouter(
      [
        {
          path: '/register',
          element: (
            <PublicRoute>
              <div>Register Form</div>
            </PublicRoute>
          ),
        },
      ],
      { initialEntries: ['/register'] }
    )

    render(<RouterProvider router={router} />)

    expect(screen.getByText('Register Form')).toBeInTheDocument()
  })
})

