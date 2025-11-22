import { ProtectedRoute } from "@/infrastructure/router/ProtectedRoute.jsx";
import { ProtectedLayout } from "@/shared/components/layout/ProtectedLayout.jsx";
import HomePage from "../pages/HomePage.jsx";

export const homeRoutes = [
  {
    path: "/home",
    element: (
      <ProtectedRoute>
        <ProtectedLayout />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: <HomePage />,
      },
    ],
  },
];

