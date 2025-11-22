import { ProtectedRoute } from "@/infrastructure/router/ProtectedRoute.jsx";
import { ProtectedLayout } from "@/shared/components/layout/ProtectedLayout.jsx";
import PredictionsPage from "../pages/PredictionsPage";



export const predictionsRoutes = [
  {
    path: "/predictions-IA",
    element: (
      <ProtectedRoute>
        <ProtectedLayout />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: <PredictionsPage />,
      },
    ],
  },
];