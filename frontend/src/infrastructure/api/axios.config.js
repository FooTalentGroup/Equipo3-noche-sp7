import axios from 'axios'
import { API_BASE_URL, API_TIMEOUT, DEFAULT_HEADERS } from '../constants/config.js'
import { getAuthToken } from '@/features/auth/utils/authStorage.js'

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: API_TIMEOUT,
  headers: DEFAULT_HEADERS,
})

axiosInstance.interceptors.request.use(
  (config) => {
    const token = getAuthToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

export default axiosInstance


