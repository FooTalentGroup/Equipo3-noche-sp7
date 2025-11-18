import { Navigate } from 'react-router-dom';
import { getAuthToken } from '@/features/auth/utils/authStorage.js';

export const AuthRedirect = () => {
  const token = getAuthToken();

  if (token) {
    return <Navigate to="/dashboard" replace />;
  }

  return <Navigate to="/login" replace />;
};

