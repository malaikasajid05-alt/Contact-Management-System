import { apiRequest } from './apiClient';

export const getUserProfile = () => apiRequest('/users/profile');

export const changePassword = (payload) =>
  apiRequest('/users/change-password', {
    method: 'PUT',
    body: JSON.stringify(payload),
  });
