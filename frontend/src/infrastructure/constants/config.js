const getApiBaseUrl = () => {
  const url = import.meta.env.VITE_API_BASE_URL;

  if (!url) {
    throw new Error(
      '❌ VITE_API_BASE_URL no está definida.\n' +
        'Por favor, crea un archivo .env en la raíz del proyecto frontend con:\n' +
        'VITE_API_BASE_URL=http://localhost:8080\n\n' +
        'Para producción, configura la variable de entorno correspondiente.'
    );
  }

  return url;
};

export const API_BASE_URL = getApiBaseUrl();

export const API_TIMEOUT = 30000;

export const DEFAULT_HEADERS = {
  'Content-Type': 'application/json',
  Accept: 'application/json'
};
