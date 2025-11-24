import { ProtectedRoute } from "@/infrastructure/router/ProtectedRoute.jsx";
import { ProtectedLayout } from "@/shared/components/layout/ProtectedLayout.jsx";
import ReportsPage from "../pages/ReportsPage";


export const reportsRoutes = [
  {
    path: "/reports",
    element: (
      <ProtectedRoute>
        <ProtectedLayout />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: < ReportsPage />,
      },
    ],
  },
];