const BASE_URL = 'http://localhost:8080/api';

const getHeaders = () => {
    const token = localStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
    };
};

const request = async (method, path, body) => {
    const res = await fetch(`${BASE_URL}${path}`, {
        method,
        headers: getHeaders(),
        ...(body ? { body: JSON.stringify(body) } : {}),
    });
    if (!res.ok) {
        let errMsg = `HTTP ${res.status}`;
        try {
            const err = await res.json();
            errMsg = err.message || errMsg;
        } catch (_) {}
        throw new Error(errMsg);
    }
    const text = await res.text();
    return text ? JSON.parse(text) : null;
};

// Auth
export const authApi = {
    register: (data) => request('POST', '/auth/register', data),
    login: (data) => request('POST', '/auth/login', data),
};

// Contacts
export const contactsApi = {
    getAll: () => request('GET', '/contacts'),
    getById: (id) => request('GET', `/contacts/${id}`),
    create: (data) => request('POST', '/contacts', data),
    update: (id, data) => request('PUT', `/contacts/${id}`, data),
    delete: (id) => request('DELETE', `/contacts/${id}`),
};

// Contact Details
export const detailsApi = {
    getAll: (contactId) => request('GET', `/contacts/${contactId}/details`),
    add: (contactId, data) => request('POST', `/contacts/${contactId}/details`, data),
    update: (contactId, detailId, data) => request('PUT', `/contacts/${contactId}/details/${detailId}`, data),
    delete: (contactId, detailId) => request('DELETE', `/contacts/${contactId}/details/${detailId}`),
};

// User
export const userApi = {
    getProfile: () => request('GET', '/users/profile'),
    changePassword: (data) => request('PUT', '/users/change-password', data),
};