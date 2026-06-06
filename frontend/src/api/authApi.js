import { apiRequest } from './apiClient';

export const loginUser = (credentials) =>
    apiRequest('/auth/login', {
        method: 'POST',
        body: JSON.stringify({
            email: credentials.emailOrPhone.includes('@')
                ? credentials.emailOrPhone
                : null,

            pNumber: !credentials.emailOrPhone.includes('@')
                ? credentials.emailOrPhone
                : null,

            password: credentials.password,
        }),
    });

export const registerUser = (payload) =>
    apiRequest('/auth/register', {
        method: 'POST',
        body: JSON.stringify({
            name: payload.name,

            email: payload.emailOrPhone.includes('@')
                ? payload.emailOrPhone
                : null,

            pnumber: !payload.emailOrPhone.includes('@')
                ? payload.emailOrPhone
                : null,

            password: payload.password,
        }),
    });
