const decodeJWT = token => {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error('Error al decodificar JWT:', error);
    return null;
  }
};

export const getAuthToken = () => {
  return localStorage.getItem('token') || null;
};

export const getUsername = () => {
  return localStorage.getItem('username') || null;
};

export const getUserRole = () => {
  return localStorage.getItem('role') || null;
};

export const setAuthToken = token => {
  console.log(
    'setAuthToken llamado con:',
    token ? 'token presente' : 'token null/undefined'
  );

  if (token) {
    console.log('Guardando token en localStorage...');
    localStorage.setItem('token', token);
    console.log(
      'Token guardado. Verificando localStorage:',
      localStorage.getItem('token') ? 'OK' : 'ERROR'
    );

    const decoded = decodeJWT(token);
    console.log('JWT decodificado:', decoded);

    if (decoded) {
      if (decoded.username) {
        localStorage.setItem('username', decoded.username);
        console.log('Username guardado:', decoded.username);
      }
      if (decoded.role) {
        localStorage.setItem('role', decoded.role);
        console.log('Role guardado:', decoded.role);
      }
    } else {
      console.warn('No se pudo decodificar el JWT');
    }
  } else {
    console.log('Token es null/undefined, limpiando localStorage...');
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
  }
};

export const clearAuthData = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('username');
  localStorage.removeItem('role');
};

