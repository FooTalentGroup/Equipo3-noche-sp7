import { useApiMutation } from '@/shared/hooks/useApi.js';
import { login as loginService, register as registerService, logout as logoutService } from '../services/authService.js';

export const useLogin = (options = {}) => {
  return useApiMutation(loginService, options);
};

export const useRegister = (options = {}) => {
  return useApiMutation(registerService, options);
};

export const useLogout = (options = {}) => {
  return useApiMutation(logoutService, options);
};

