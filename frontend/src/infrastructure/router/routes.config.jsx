import { authRoutes } from "@/features/auth/routes/auth.routes.jsx";
import { homeRoutes } from "@/features/dashboard/routes/home.routes.jsx";
import { productsRoutes } from "@/features/products/routes/products.routes.jsx";
import { notfoundRoutes } from "./notfound.routes.jsx";
import {createBrowserRouter, Navigate} from "react-router-dom";
import {salesRoutes} from "@/features/sales/routes/sales.routes.jsx";
import { ProtectedLayout } from '@/shared/components/layout/ProtectedLayout.jsx';
import DashboardPage from '@/features/dashboard/pages/HomePage.jsx';
import {discountsRoutes} from "@/features/discounts/routes/discounts.routes.jsx";
import { customersRoutes } from "@/features/customers/routes/customers.routes.jsx";
import { suppliersRoutes } from "@/features/suppliers/routes/suppliers.routes.jsx";
import { predictionsRoutes } from "@/features/predictions/routes/predictions.router.jsx";
import { reportsRoutes } from "@/features/repor/routes/reports.routes.jsx";


const router = createBrowserRouter([
    {
        path: '/',
        element: <ProtectedLayout />,
        children: [
            {
                index: true,
                element: <Navigate to="/dashboard" replace />
            },
            {
                path: 'dashboard',
                element: <DashboardPage />
            },
        ]
    },
    ...authRoutes,
    ...homeRoutes,
    ...productsRoutes,
    ...salesRoutes,
    ...discountsRoutes,
    ...customersRoutes,
    ...suppliersRoutes,
    ...predictionsRoutes,
    ...reportsRoutes,
    ...notfoundRoutes

]);

export default router;

