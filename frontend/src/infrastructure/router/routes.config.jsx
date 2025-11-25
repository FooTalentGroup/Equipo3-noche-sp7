import React from 'react';
import { createBrowserRouter } from 'react-router-dom';
import App from '@/App.jsx';
import NotFoundPage from '@/infrastructure/pages/NotFoundPage.jsx';
import HomePage from '@/features/dashboard/pages/HomePage.jsx';
import ProductsPage from '@/features/products/pages/ProductsListPage.jsx';
import CustomersPage from '@/features/customers/pages/CustomersPage.jsx';
import SuppliersPage from '@/features/suppliers/pages/SuppliersPage.jsx';
import SalesPage from '@/features/sales/pages/SalesPage';
import DiscountsPage from '@/features/discounts/pages/DiscountPage.jsx';
import PredictionsPage from '@/features/predictions/pages/PredictionsPage.jsx';
import ReportsPage from '@/features/repor/pages/ReportsPage.jsx';
import { authRoutes } from '@/features/auth/routes/auth.routes.jsx';
import { ProtectedRoute } from '@/infrastructure/router/ProtectedRoute.jsx';

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
            { path: 'home', element: <HomePage /> },
            { path: 'products', element: <ProductsPage /> },
            { path: 'customers', element: <CustomersPage /> },
            { path: 'suppliers', element: <SuppliersPage /> },
            { path: 'sales', element: <SalesPage /> },
            { path: 'discounts', element: <DiscountsPage /> },
            { path: 'predictions-IA', element: <PredictionsPage /> },
            { path: 'reports', element: <ReportsPage /> },
            { path: '*', element: <NotFoundPage /> },
        ],
    },
]);

export default router;

