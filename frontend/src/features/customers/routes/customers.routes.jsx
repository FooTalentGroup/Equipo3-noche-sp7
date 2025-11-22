
import { ProtectedRoute } from "@/infrastructure/router/ProtectedRoute.jsx";
import { ProtectedLayout } from "@/shared/components/layout/ProtectedLayout.jsx";
import CustomersPage from "../pages/CustomersPage";


export const customersRoutes = [
  {
    path: "/customers",
    element: (
      <ProtectedRoute>
        <ProtectedLayout />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: <CustomersPage />,
      },
    ],
  },
];
