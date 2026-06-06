import { useCallback, useEffect, useState } from 'react';
import { getContacts } from '../api/contactApi';
import { normalizeContact } from '../utils/contactUtils';

export const useContacts = () => {
  const [contacts, setContacts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadContacts = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const payload = await getContacts();
      setContacts(payload.map(normalizeContact));
    } catch (apiError) {
      setError(apiError.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadContacts();
  }, [loadContacts]);

  return { contacts, loading, error, reload: loadContacts, setContacts };
};
