const decodeJWT = (token) => {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    );
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error("Error al decodificar JWT:", error);
    return null;
  }
};

export const getAuthToken = () => {
  return localStorage.getItem("token") || null;
};

export const getUsername = () => {
  return localStorage.getItem("name") || null;
};

export const getUserRole = () => {
  return localStorage.getItem("role") || null;
};

export const setAuthToken = (token) => {
  if (token) {
    localStorage.setItem("token", token);
    const decoded = decodeJWT(token);

    if (decoded) {
      if (decoded.name) {
        localStorage.setItem("name", decoded.name);
      }
      if (decoded.role) {
        localStorage.setItem("role", decoded.role);
      } else {
        console.warn("No se pudo decodificar el JWT");
      }
    } else {
      localStorage.removeItem("token");
      localStorage.removeItem("name");
      localStorage.removeItem("role");
    }
  }
};

export const clearAuthData = () => {
  try {
    localStorage.removeItem("token");
    localStorage.removeItem("name");
    localStorage.removeItem("role");
    localStorage.clear();
  } catch (error) {
    console.error("Error al limpiar localStorage:", error);
  }
};

export const isAuthenticated = () => getAuthToken() !== null;
