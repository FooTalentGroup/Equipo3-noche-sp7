import SalesPage from "../pages/SalesPage.jsx";
import { ProtectedRoute } from "@/infrastructure/router/ProtectedRoute.jsx";
import { ProtectedLayout } from "@/shared/components/layout/ProtectedLayout.jsx";

export const salesRoutes = [
    {
        path: "/sales",
        element: (
            <ProtectedRoute>
                <ProtectedLayout />
            </ProtectedRoute>
        ),
        children: [
            {
                index: true,
                element: <SalesPage />,
            },
        ],
    },
];

