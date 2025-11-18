import ProductsListPage from '../pages/ProductsListPage.jsx';
import { ProtectedRoute } from '@/infrastructure/router/ProtectedRoute.jsx';
import { ProtectedLayout } from '@/shared/components/layout/ProtectedLayout.jsx';

export const productsRoutes = [
  {
    path: '/products',
    element: (
      <ProtectedRoute>
        <ProtectedLayout />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: <ProductsListPage />,
      },
    ],
  },
];

