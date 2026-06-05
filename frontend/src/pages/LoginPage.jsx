import { useState } from 'react';
import { authApi } from '../services/api';
import { useAuth } from '../context/AuthContext';

export default function LoginPage({ onSwitchToRegister }) {
    const { login } = useAuth();
    const [form, setForm] = useState({ email: '', pNumber: '', password: '' });
    const [usePhone, setUsePhone] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handle = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const submit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            const payload = usePhone
                ? { pNumber: form.pNumber, password: form.password }
                : { email: form.email, password: form.password };
            const data = await authApi.login(payload);
            login(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-card">
            <div className="auth-card__icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                    <circle cx="9" cy="7" r="4"/>
                    <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
                    <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
                </svg>
            </div>
            <h2 className="auth-card__title">Welcome Back! 👋</h2>
            <p className="auth-card__subtitle">Sign in to continue to your account</p>

            <div className="toggle-row">
                <button className={`toggle-btn${!usePhone ? ' active' : ''}`} onClick={() => setUsePhone(false)} type="button">Email</button>
                <button className={`toggle-btn${usePhone ? ' active' : ''}`} onClick={() => setUsePhone(true)} type="button">Phone</button>
            </div>

            <form onSubmit={submit} className="auth-form">
                {!usePhone ? (
                    <div className="field">
            <span className="field__icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>
            </span>
                        <input name="email" type="email" placeholder="Email or Phone Number" value={form.email} onChange={handle} required />
                    </div>
                ) : (
                    <div className="field">
            <span className="field__icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M22 16.92v3a2 2 0 01-2.18 2 19.79 19.79 0 01-8.63-3.07A19.5 19.5 0 013.07 12.82a19.79 19.79 0 01-3.07-8.67A2 2 0 012 2h3a2 2 0 012 1.72c.127.96.361 1.903.7 2.81a2 2 0 01-.45 2.11L6.91 9.91a16 16 0 006.29 6.29l1.27-1.27a2 2 0 012.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0122 16.92z"/></svg>
            </span>
                        <input name="pNumber" type="tel" placeholder="Phone Number" value={form.pNumber} onChange={handle} required />
                    </div>
                )}

                <div className="field">
          <span className="field__icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
          </span>
                    <input name="password" type={showPassword ? 'text' : 'password'} placeholder="Password" value={form.password} onChange={handle} required />
                    <button type="button" className="field__eye" onClick={() => setShowPassword(!showPassword)}>
                        {showPassword
                            ? <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19m-6.72-1.07a3 3 0 11-4.24-4.24"/><line x1="1" y1="1" x2="23" y2="23"/></svg>
                            : <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                        }
                    </button>
                </div>

                <div className="forgot-row">
                    <label className="remember"><input type="checkbox" /> Remember me</label>
                    <a href="#" className="link">Forgot Password?</a>
                </div>

                {error && <div className="error-msg">{error}</div>}

                <button type="submit" className="btn-primary" disabled={loading}>
                    {loading ? <span className="spinner" /> : 'Sign In'}
                </button>
            </form>

            <p className="auth-switch">
                Don't have an account? <button type="button" className="link-btn" onClick={onSwitchToRegister}>Create one</button>
            </p>
        </div>
    );
}
