import { useState } from 'react';
import { authApi } from '../services/api';
import { useAuth } from '../context/AuthContext';

export default function RegisterPage({ onSwitchToLogin }) {
    const { login } = useAuth();
    const [form, setForm] = useState({ name: '', email: '', pnumber: '', password: '', confirm: '' });
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirm, setShowConfirm] = useState(false);
    const [agreed, setAgreed] = useState(false);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handle = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const submit = async (e) => {
        e.preventDefault();
        setError('');
        if (form.password !== form.confirm) { setError('Passwords do not match'); return; }
        if (!agreed) { setError('Please agree to the Terms & Conditions'); return; }
        if (!form.email && !form.pnumber) { setError('Email or phone number is required'); return; }
        setLoading(true);
        try {
            const data = await authApi.register({
                name: form.name,
                email: form.email || null,
                pnumber: form.pnumber || null,
                password: form.password,
            });
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
                    <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                    <circle cx="8.5" cy="7" r="4"/>
                    <line x1="20" y1="8" x2="20" y2="14"/>
                    <line x1="23" y1="11" x2="17" y2="11"/>
                </svg>
            </div>
            <h2 className="auth-card__title">Create Account</h2>
            <p className="auth-card__subtitle">Fill in the details to get started</p>

            <form onSubmit={submit} className="auth-form">
                <div className="field">
                    <span className="field__icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></span>
                    <input name="name" type="text" placeholder="Full Name" value={form.name} onChange={handle} required />
                </div>
                <div className="field">
                    <span className="field__icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg></span>
                    <input name="email" type="email" placeholder="Enter email or phone number" value={form.email} onChange={handle} />
                </div>
                <div className="field">
                    <span className="field__icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M22 16.92v3a2 2 0 01-2.18 2 19.79 19.79 0 01-8.63-3.07A19.5 19.5 0 013.07 12.82a19.79 19.79 0 01-3.07-8.67A2 2 0 012 2h3a2 2 0 012 1.72c.127.96.361 1.903.7 2.81a2 2 0 01-.45 2.11L6.91 9.91a16 16 0 006.29 6.29l1.27-1.27a2 2 0 012.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0122 16.92z"/></svg></span>
                    <input name="pnumber" type="tel" placeholder="Phone number (optional)" value={form.pnumber} onChange={handle} />
                </div>
                <div className="field">
                    <span className="field__icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg></span>
                    <input name="password" type={showPassword ? 'text' : 'password'} placeholder="Create a password" value={form.password} onChange={handle} required minLength={8} />
                    <button type="button" className="field__eye" onClick={() => setShowPassword(!showPassword)}>
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                    </button>
                </div>
                <div className="field">
                    <span className="field__icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg></span>
                    <input name="confirm" type={showConfirm ? 'text' : 'password'} placeholder="Confirm your password" value={form.confirm} onChange={handle} required />
                    <button type="button" className="field__eye" onClick={() => setShowConfirm(!showConfirm)}>
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                    </button>
                </div>

                <label className="terms-label">
                    <input type="checkbox" checked={agreed} onChange={e => setAgreed(e.target.checked)} />
                    <span>I agree to the <a href="#" className="link">Terms &amp; Conditions</a></span>
                </label>

                {error && <div className="error-msg">{error}</div>}

                <button type="submit" className="btn-primary" disabled={loading}>
                    {loading ? <span className="spinner" /> : 'Create Account'}
                </button>
            </form>

            <p className="auth-switch">
                Already have an account? <button type="button" className="link-btn" onClick={onSwitchToLogin}>Sign in</button>
            </p>
        </div>
    );
}
