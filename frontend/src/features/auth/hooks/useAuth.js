import { useApiMutation } from '@/shared/hooks/useApi.js';
import { login as loginService, register as registerService, logout as logoutService,forgotPassword as forgotPasswordService, resetPassword as resetPasswordService } from '../services/authService.js';

export const useLogin = (options = {}) => {
  return useApiMutation(loginService, options);
};

export const useRegister = (options = {}) => {
  return useApiMutation(registerService, options);
};

export const useLogout = (options = {}) => {
  return useApiMutation(logoutService, options);
};

export const useForgotPassword = (options = {}) => {
  return useApiMutation(forgotPasswordService, options);
};

export const useResetPassword = (options = {}) => {
  return useApiMutation(resetPasswordService, options);
};