import axios from 'axios';

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export function storeAuth(authResponse) {
  localStorage.setItem('authToken', authResponse.token);
  localStorage.setItem('authUser', JSON.stringify(authResponse.user));
}

export function getAuthUser() {
  const value = localStorage.getItem('authUser');
  return value ? JSON.parse(value) : null;
}

export function clearAuth() {
  localStorage.removeItem('authToken');
  localStorage.removeItem('authUser');
}
