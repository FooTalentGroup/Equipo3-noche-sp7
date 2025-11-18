import { useQuery, useMutation } from '@tanstack/react-query';
import apiClient from '../services/apiClient.js';

export const useApiQuery = (queryKey, url, options = {}) => {
  return useQuery({
    queryKey,
    queryFn: async () => {
      const response = await apiClient.get(url, options);
      return response.data;
    },
    staleTime: 60 * 1000,
    refetchOnWindowFocus: false,
    ...options
  });
};

export const useApiMutation = (mutationFn, options = {}) => {
  const { onSuccess, onError, ...restOptions } = options;

  return useMutation({
    mutationFn,
    onSuccess: (data, variables, context) => {
      console.log('useApiMutation - onSuccess ejecutado:', {
        data,
        variables,
        context
      });
      if (onSuccess) {
        onSuccess(data, variables, context);
      }
    },
    onError: (error, variables, context) => {
      console.error('useApiMutation - onError ejecutado:', {
        error,
        variables,
        context
      });
      if (onError) {
        onError(error, variables, context);
      }
    },
    ...restOptions
  });
};

export const useApiPost = (url, options = {}) => {
  return useApiMutation(async data => {
    const response = await apiClient.post(url, data, options.config);
    return response.data;
  }, options);
};

export const useApiPut = (url, options = {}) => {
  return useApiMutation(async data => {
    const response = await apiClient.put(url, data, options.config);
    return response.data;
  }, options);
};

export const useApiPatch = (url, options = {}) => {
  return useApiMutation(async data => {
    const response = await apiClient.patch(url, data, options.config);
    return response.data;
  }, options);
};

export const useApiDelete = (url, options = {}) => {
  return useApiMutation(async () => {
    const response = await apiClient.delete(url, options.config);
    return response.data;
  }, options);
};
