import { Navigate } from 'react-router-dom';
import { getAuthToken } from '@/features/auth/utils/authStorage.js';

export const PublicRoute = ({ children }) => {
  const token = getAuthToken();

  if (token) {
    return <Navigate to='/dashboard' replace />;
  }

  return children;
};
