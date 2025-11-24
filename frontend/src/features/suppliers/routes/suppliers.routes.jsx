import { ProtectedRoute } from "@/infrastructure/router/ProtectedRoute.jsx";
import { ProtectedLayout } from "@/shared/components/layout/ProtectedLayout.jsx";
import SuppliersPage from "../pages/SuppliersPage";



export const suppliersRoutes = [
  {
    path: "/suppliers",
    element: (
      <ProtectedRoute>
        <ProtectedLayout />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: <SuppliersPage />,
      },
    ],
  },
];