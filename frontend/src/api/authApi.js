import { apiRequest } from './apiClient';

const isEmail = (value) => value.trim().includes('@');

// Strips null/undefined fields so they are never sent in the JSON body
const compact = (obj) =>
    Object.fromEntries(Object.entries(obj).filter(([, v]) => v != null && v !== ''));

export const loginUser = (credentials) => {
    const identifier = credentials.emailOrPhone.trim();
    return apiRequest('/auth/login', {
        method: 'POST',
        body: JSON.stringify(compact({
            email:   isEmail(identifier) ? identifier : undefined,
            pNumber: !isEmail(identifier) ? identifier : undefined,  // matches @JsonProperty("pNumber") in LoginRequestDto
            password: credentials.password,
        })),
    });
};

export const registerUser = (payload) => {
    const identifier = payload.emailOrPhone.trim();
    return apiRequest('/auth/register', {
        method: 'POST',
        body: JSON.stringify(compact({
            name:    payload.fullName,
            email:   isEmail(identifier) ? identifier : undefined,
            pnumber: !isEmail(identifier) ? identifier : undefined,  // matches RegisterRequestDto.pnumber (all lowercase)
            password: payload.password,
        })),
    });
};