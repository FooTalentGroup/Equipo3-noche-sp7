import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { render } from '@testing-library/react'
import { createMemoryRouter, RouterProvider } from 'react-router-dom'
import { vi } from 'vitest'

function createTestQueryClient() {
  return new QueryClient({
    defaultOptions: {
      queries: {
        retry: false,
        gcTime: 0,
      },
      mutations: {
        retry: false,
      },
    },
  })
}

export function renderWithProviders(
  ui,
  {
    route = '/',
    routes = [],
    queryClient = createTestQueryClient(),
    ...renderOptions
  } = {}
) {
  const router = createMemoryRouter(
    routes.length > 0 ? routes : [{ path: route, element: ui }],
    {
      initialEntries: [route],
    }
  )

  function Wrapper({ children }) {
    return (
      <QueryClientProvider client={queryClient}>
        {children || <RouterProvider router={router} />}
      </QueryClientProvider>
    )
  }

  return {
    ...render(ui, { wrapper: Wrapper, ...renderOptions }),
    queryClient,
    router,
  }
}

export function createMockRouter(initialEntries = ['/']) {
  return createMemoryRouter([{ path: '/', element: <div>Test</div> }], {
    initialEntries,
  })
}

export * from '@testing-library/react'
export { renderWithProviders as render }

