import { useState, useEffect } from 'react'
import apiClient from '../services/apiClient.js'

export const useFetch = (url, options = {}) => {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    let isCancelled = false

    const fetchData = async () => {
      try {
        setLoading(true)
        setError(null)
        const response = await apiClient.get(url, options)
        
        if (!isCancelled) {
          setData(response.data)
        }
      } catch (err) {
        if (!isCancelled) {
          setError(err)
        }
      } finally {
        if (!isCancelled) {
          setLoading(false)
        }
      }
    }

    if (url) {
      fetchData()
    }

    return () => {
      isCancelled = true
    }
  }, [url])

  return { data, loading, error, refetch: () => {
    setLoading(true)
    apiClient.get(url, options)
      .then(response => {
        setData(response.data)
        setError(null)
      })
      .catch(err => setError(err))
      .finally(() => setLoading(false))
  } }
}


