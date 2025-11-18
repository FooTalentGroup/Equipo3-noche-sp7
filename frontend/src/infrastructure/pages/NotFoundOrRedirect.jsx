import { getAuthToken } from '@/features/auth/utils/authStorage.js';
import { AuthRedirect } from '@/infrastructure/router/AuthRedirect.jsx';
import NotFound from './NotFoundPage.jsx';

const NotFoundOrRedirect = () => {
  const token = getAuthToken();

  if (!token) {
    return <AuthRedirect />;
  }

  return <NotFound />;
};

export default NotFoundOrRedirect;

