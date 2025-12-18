import ForgotPasswordPage from "../pages/ForgotPasswordPage.jsx";
import LoginPage from "../pages/LoginPage.jsx";
import { PublicRoute } from "@/infrastructure/router/PublicRoute.jsx";
import ResetPasswordPage from "../pages/ResetPasswordPage.jsx";
import { ResetPasswordSuccess } from "../pages/ResetPasswordSucessPage.jsx";


export const authRoutes = [
  {
    path: "/login",
    element: (
      <PublicRoute>
        <LoginPage />
      </PublicRoute>
    ),
  },
   {
    path: "/forgot-password",
    element: (
      <PublicRoute>
        <ForgotPasswordPage />
      </PublicRoute>
    ),
  },
  {
    path: "/reset-password",
    element: (
      <PublicRoute>
        <ResetPasswordPage />
      </PublicRoute>
    ),
  },
    {
    path: "/reset-password-success",
    element: (
      <PublicRoute>
        <ResetPasswordSuccess />
      </PublicRoute>
    ),
  }
];