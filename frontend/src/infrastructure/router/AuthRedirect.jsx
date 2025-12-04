import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAuthToken, removeAuthData } from '@/features/auth/utils/authStorage';

export function AuthRedirect({ children }) {
  const navigate = useNavigate();

  useEffect(() => {
    const token = getAuthToken();

    if (!token) {
      removeAuthData();
      navigate('/login', { replace: true });
    }
  }, [navigate]);

  const token = getAuthToken();

  if (!token) {
    return null;
  }

  return children;
}

