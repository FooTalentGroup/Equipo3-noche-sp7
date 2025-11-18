import { ProtectedRoute } from "@/infrastructure/router/ProtectedRoute.jsx";
import { ProtectedLayout } from "@/shared/components/layout/ProtectedLayout.jsx";
import DiscountPage from "@/features/discounts/pages/DiscountPage.jsx";

export const discountsRoutes = [
    {
        path: "/discounts",
        element: (
            <ProtectedRoute>
                <ProtectedLayout />
            </ProtectedRoute>
        ),
        children: [
            {
                index: true,
                element: <DiscountPage />,
            },
        ],
    },
];

