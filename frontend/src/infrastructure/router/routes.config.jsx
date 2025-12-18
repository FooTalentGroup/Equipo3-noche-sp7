import React from 'react';
import { createBrowserRouter } from 'react-router-dom';
import App from '@/App.jsx';
import NotFoundPage from '@/infrastructure/pages/NotFoundPage.jsx';
import DiscountsPage from '@/features/discounts/pages/DiscountPage.jsx';
import { productsRoutes } from '@/features/products/routes/products.routes';
import { authRoutes } from '@/features/auth/routes/auth.routes.jsx';
import { customersRoutes } from '@/features/customers/routes/customers.routes.jsx';
import { ProtectedRoute } from '@/infrastructure/router/ProtectedRoute.jsx';
import { inventoryRoutes } from '@/features/products/routes/inventory.routes.jsx';
import UsersPage from '@/features/users/pages/UsersPage.jsx';
import HomePage from '@/features/home/pages/HomePage';
import { salesRoutes } from '@/features/sales/routes/sales.routes';
import { reportsRoutes } from '@/features/repor/routes/reports.routes';

const router = createBrowserRouter([
    ...authRoutes,
    {
        path: '/',
        element: (
            <ProtectedRoute>
                <App />
            </ProtectedRoute>
        ),
        children: [
            { index: true, element: <HomePage /> },

            ...productsRoutes,
            ...inventoryRoutes,
            ...customersRoutes,

            ...salesRoutes,
            { path: 'discounts', element: <DiscountsPage /> },
            ...reportsRoutes,

            { path: 'users', element: <UsersPage /> },
            { path: '*', element: <NotFoundPage /> },
        ],
    },
]);

export default router;
