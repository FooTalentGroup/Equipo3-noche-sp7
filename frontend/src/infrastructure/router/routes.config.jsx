import { authRoutes } from "@/features/auth/routes/auth.routes.jsx";
import { dashboardRoutes } from "@/features/dashboard/routes/dashboard.routes.jsx";
import { productsRoutes } from "@/features/products/routes/products.routes.jsx";
import { notfoundRoutes } from "./notfound.routes.jsx";
import {createBrowserRouter, Navigate} from "react-router-dom";
import {salesRoutes} from "@/features/sales/routes/sales.routes.jsx";
import { ProtectedLayout } from '@/shared/components/layout/ProtectedLayout.jsx';
import DashboardPage from '@/features/dashboard/pages/DashboardPage.jsx';
import discountPage from "@/features/discounts/pages/DiscountPage.jsx";
import {discountsRoutes} from "@/features/discounts/routes/discounts.routes.jsx";


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
    ...dashboardRoutes,
    ...productsRoutes,
    ...salesRoutes,
    ...discountsRoutes,
    ...notfoundRoutes

]);

export default router;

