export const handleApiError = (error) => {
  if (!error.response) {
    return {
      message: 'Error de conexión. Verifica tu internet.',
      type: 'network',
      status: null,
    }
  }

  const { status, data } = error.response

  let message = 'Ocurrió un error inesperado'

  if (data?.message) {
    message = data.message
  } else if (data?.error) {
    message = data.error
  } else if (status === 400) {
    message = 'Solicitud inválida'
  } else if (status === 401) {
    message = 'No autorizado. Por favor inicia sesión.'
  } else if (status === 403) {
    message = 'No tienes permiso para realizar esta acción'
  } else if (status === 404) {
    message = 'Recurso no encontrado'
  } else if (status === 500) {
    message = 'Error del servidor. Intenta más tarde.'
  } else if (status >= 500) {
    message = 'Error del servidor'
  }

  return {
    message,
    type: status >= 500 ? 'server' : 'client',
    status,
    data: data || null,
  }
}

export const isNetworkError = (error) => {
  return !error.response || error.code === 'ECONNABORTED'
}

export const isTimeoutError = (error) => {
  return error.code === 'ECONNABORTED' || error.message?.includes('timeout')
}


