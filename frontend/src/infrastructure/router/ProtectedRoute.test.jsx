import { describe, test, expect, vi, beforeEach } from 'vitest'
import { render, screen } from '@testing-library/react'
import { createMemoryRouter, RouterProvider } from 'react-router-dom'
import { ProtectedRoute } from './ProtectedRoute.jsx'
import * as authStorage from '@/features/auth/utils/authStorage.js'

vi.mock('@/features/auth/utils/authStorage.js')

describe('ProtectedRoute', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('debe renderizar children cuando hay token', () => {
    authStorage.getAuthToken.mockReturnValue('fake-token')

    const router = createMemoryRouter(
      [
        {
          path: '/protected',
          element: (
            <ProtectedRoute>
              <div>Protected Content</div>
            </ProtectedRoute>
          ),
        },
      ],
      { initialEntries: ['/protected'] }
    )

    render(<RouterProvider router={router} />)

    expect(screen.getByText('Protected Content')).toBeInTheDocument()
  })

  test('debe redirigir a /login cuando no hay token', () => {
    authStorage.getAuthToken.mockReturnValue(null)

    const router = createMemoryRouter(
      [
        {
          path: '/protected',
          element: (
            <ProtectedRoute>
              <div>Protected Content</div>
            </ProtectedRoute>
          ),
        },
        {
          path: '/login',
          element: <div>Login Page</div>,
        },
      ],
      { initialEntries: ['/protected'] }
    )

    render(<RouterProvider router={router} />)

    expect(screen.getByText('Login Page')).toBeInTheDocument()
    expect(screen.queryByText('Protected Content')).not.toBeInTheDocument()
  })

  test('debe preservar location state en redirección', () => {
    authStorage.getAuthToken.mockReturnValue(null)

    const router = createMemoryRouter(
      [
        {
          path: '/protected',
          element: (
            <ProtectedRoute>
              <div>Protected Content</div>
            </ProtectedRoute>
          ),
        },
        {
          path: '/login',
          element: <div>Login Page</div>,
        },
      ],
      {
        initialEntries: ['/protected'],
      }
    )

    render(<RouterProvider router={router} />)

    expect(router.state.location.pathname).toBe('/login')
  })

  test('debe usar replace en navegación', () => {
    authStorage.getAuthToken.mockReturnValue(null)

    const router = createMemoryRouter(
      [
        {
          path: '/protected',
          element: (
            <ProtectedRoute>
              <div>Protected Content</div>
            </ProtectedRoute>
          ),
        },
        {
          path: '/login',
          element: <div>Login Page</div>,
        },
      ],
      { initialEntries: ['/protected'] }
    )

    render(<RouterProvider router={router} />)

    expect(router.state.historyAction).toBe('REPLACE')
  })

  test('debe redirigir cuando token es string vacía', () => {
    authStorage.getAuthToken.mockReturnValue('')

    const router = createMemoryRouter(
      [
        {
          path: '/protected',
          element: (
            <ProtectedRoute>
              <div>Protected Content</div>
            </ProtectedRoute>
          ),
        },
        {
          path: '/login',
          element: <div>Login Page</div>,
        },
      ],
      { initialEntries: ['/protected'] }
    )

    render(<RouterProvider router={router} />)

    expect(screen.getByText('Login Page')).toBeInTheDocument()
  })
})

