import React from 'react';
import { createBrowserRouter, Navigate } from 'react-router-dom';
import App from '@/App.jsx';
import NotFoundPage from '@/infrastructure/pages/NotFoundPage.jsx';
import SuppliersPage from '@/features/suppliers/pages/SuppliersPage.jsx';
import SalesPage from '@/features/sales/pages/SalesPage';
import DiscountsPage from '@/features/discounts/pages/DiscountPage.jsx';
import PredictionsPage from '@/features/predictions/pages/PredictionsPage.jsx';
import ReportsPage from '@/features/repor/pages/ReportsPage.jsx';
import ReportsSales from '@/features/repor/pages/ReportsSales.jsx';
import ReportsUsers from '@/features/repor/pages/ReportsUsers.jsx';
import { productsRoutes } from '@/features/products/routes/products.routes';
import { authRoutes } from '@/features/auth/routes/auth.routes.jsx';
import { customersRoutes } from '@/features/customers/routes/customers.routes.jsx';
import { ProtectedRoute } from '@/infrastructure/router/ProtectedRoute.jsx';
import { inventoryRoutes } from '@/features/products/routes/inventory.routes.jsx';
import ProductsReport from '@/features/repor/pages/ProductsReportPage';
import UsersPage from '@/features/users/pages/UsersPage.jsx';
import HomePage from '@/features/home/pages/HomePage';
import BestSellersReport from '@/features/repor/components/BestSellersReport';
import PricingReport from '@/features/repor/components/PricingReport';
import StockReport from '@/features/repor/components/StockReport';
import { ProductsReportProvider } from '@/features/repor/contexts/ProductsReportContext';

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
            { path: 'suppliers', element: <SuppliersPage /> },
            { path: 'sales', element: <SalesPage /> },
            { path: 'discounts', element: <DiscountsPage /> },
            { path: 'predictions-IA', element: <PredictionsPage /> },
            {
                path: 'reports',
                element: <ReportsPage />,
                children: [
                    { index: true, element: <Navigate to="sales" replace /> },
                    { path: 'sales', element: <ReportsSales /> },
                    {
                        path: 'products', element: <ProductsReportProvider><ProductsReport /></ProductsReportProvider>,
                        children: [
                            { index: true, element: <Navigate to="best_sellers" replace /> },
                            { path: 'best_sellers', element: <BestSellersReport /> },
                            { path: 'pricing', element: <PricingReport /> },
                            { path: 'stock', element: <StockReport /> }
                        ]
                    },
                    { path: 'users', element: <ReportsUsers /> },
                ]
            },
            { path: 'users', element: <UsersPage /> },
            { path: 'productsrep', element: <ProductsReport /> },
            { path: '*', element: <NotFoundPage /> },
        ],
    },
]);

export default router;