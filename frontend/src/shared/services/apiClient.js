import axiosInstance from '@/infrastructure/api/axios.config.js'
import { handleApiError } from '@/infrastructure/api/errorHandler.js'

const apiClient = {
  get: async (url, config = {}) => {
    try {
      const response = await axiosInstance.get(url, config)
      return { data: response.data, status: response.status }
    } catch (error) {
      throw handleApiError(error)
    }
  },

  post: async (url, data, config = {}) => {
    try {
      const response = await axiosInstance.post(url, data, config)
      return { data: response.data, status: response.status }
    } catch (error) {
      throw handleApiError(error)
    }
  },

  put: async (url, data, config = {}) => {
    try {
      const response = await axiosInstance.put(url, data, config)
      return { data: response.data, status: response.status }
    } catch (error) {
      throw handleApiError(error)
    }
  },

  patch: async (url, data, config = {}) => {
    try {
      const response = await axiosInstance.patch(url, data, config)
      return { data: response.data, status: response.status }
    } catch (error) {
      throw handleApiError(error)
    }
  },

  delete: async (url, config = {}) => {
    try {
      const response = await axiosInstance.delete(url, config)
      return { data: response.data, status: response.status }
    } catch (error) {
      throw handleApiError(error)
    }
  },
}

export default apiClient


