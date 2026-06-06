import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { loginUser, registerUser } from '../api/authApi';
import { getUserProfile } from '../api/userApi';

const AuthContext = createContext(null);

const readStoredUser = () => {
  try {
    return JSON.parse(localStorage.getItem('user') || 'null');
  } catch {
    return null;
  }
};

const resolveToken = (payload) => payload?.token || payload?.accessToken || payload?.jwt || payload?.data?.token;

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => localStorage.getItem('token'));
  const [user, setUser] = useState(readStoredUser);
  const [loading, setLoading] = useState(Boolean(localStorage.getItem('token')));

  useEffect(() => {
    const loadProfile = async () => {
      if (!token) {
        setLoading(false);
        return;
      }

      try {
        const profile = await getUserProfile();
        setUser(profile);
        localStorage.setItem('user', JSON.stringify(profile));
      } catch {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setToken(null);
        setUser(null);
      } finally {
        setLoading(false);
      }
    };

    loadProfile();
  }, [token]);

  const login = async (values) => {
    const payload = await loginUser(values);
    const authToken = resolveToken(payload);

    if (!authToken) {
      throw new Error('Login succeeded but no token was returned.');
    }

    localStorage.setItem('token', authToken);
    setToken(authToken);
    const nextUser = payload.user || payload.data?.user || null;
    if (nextUser) {
      setUser(nextUser);
      localStorage.setItem('user', JSON.stringify(nextUser));
    }
  };

  const register = async (values) => {
    const payload = await registerUser(values);
    const authToken = resolveToken(payload);

    if (authToken) {
      localStorage.setItem('token', authToken);
      setToken(authToken);
    } else {
      await login({ emailOrPhone: values.emailOrPhone, password: values.password });
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setToken(null);
    setUser(null);
  };

  const value = useMemo(
    () => ({
      token,
      user,
      loading,
      isAuthenticated: Boolean(token),
      login,
      register,
      logout,
      setUser,
    }),
    [token, user, loading],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => useContext(AuthContext);
