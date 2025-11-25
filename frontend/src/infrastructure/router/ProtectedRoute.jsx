import { Navigate } from 'react-router-dom';
import { getAuthToken } from '@/features/auth/utils/authStorage.js';

export const ProtectedRoute = ({ children }) => {
  const token = getAuthToken();

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

