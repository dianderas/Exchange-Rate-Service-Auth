import axios from 'axios';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_GATEWAY_SERVICE_ENDPOINT,
});

apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    console.log(config);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      console.warn('Sesión expirada, redirigiendo al inicio de sesión...');
      localStorage.removeItem('access_token'); // Opcional: Borrar el token
      window.location.href = '/'; // Redirigir al inicio de sesión
    }
    return Promise.reject(error);
  }
);

export default apiClient;
