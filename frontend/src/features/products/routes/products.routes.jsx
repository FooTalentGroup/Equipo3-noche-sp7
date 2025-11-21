import ProductsListPage from '../pages/ProductsListPage.jsx';
import { ProtectedRoute } from '@/infrastructure/router/ProtectedRoute.jsx';
import { ProtectedLayout } from '@/shared/components/layout/ProtectedLayout.jsx';
import { CreateProductComponent } from '../components/CreateProductComponent.jsx';

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
      {
        path: '/products/create',
        element: <CreateProductComponent />,
      }
    ],
  },
];

