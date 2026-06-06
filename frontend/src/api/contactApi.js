import { apiRequest, extractArray } from './apiClient';

export const getContacts = async () => extractArray(await apiRequest('/contacts?size=1000'));

export const getContactById = (id) => apiRequest(`/contacts/${id}`);

export const createContact = (payload) =>
    apiRequest('/contacts', {
        method: 'POST',
        body: JSON.stringify(payload),
    });

export const updateContact = (id, payload) =>
    apiRequest(`/contacts/${id}`, {
        method: 'PUT',
        body: JSON.stringify(payload),
    });

export const deleteContact = (id) =>
    apiRequest(`/contacts/${id}`, {
        method: 'DELETE',
    });

export const getContactDetails = async (contactId) =>
    extractArray(await apiRequest(`/contacts/${contactId}/details`));

export const createContactDetail = (contactId, payload) =>
    apiRequest(`/contacts/${contactId}/details`, {
        method: 'POST',
        body: JSON.stringify(payload),
    });

export const updateContactDetail = (contactId, detailId, payload) =>
    apiRequest(`/contacts/${contactId}/details/${detailId}`, {
        method: 'PUT',
        body: JSON.stringify(payload),
    });

export const deleteContactDetail = (contactId, detailId) =>
    apiRequest(`/contacts/${contactId}/details/${detailId}`, {
        method: 'DELETE',
    });