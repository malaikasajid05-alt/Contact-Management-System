import { useState, useEffect } from 'react';
import { userApi } from '../services/api';
import { useAuth } from '../context/AuthContext';

function ChangePasswordModal({ onClose }) {
    const [form, setForm] = useState({ oldPassword: '', newPassword: '', confirm: '' });
    const [show, setShow] = useState({ old: false, new: false, confirm: false });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const checks = {
        length: form.newPassword.length >= 8,
        upper: /[A-Z]/.test(form.newPassword) && /[a-z]/.test(form.newPassword),
        special: /[0-9!@#$%^&*]/.test(form.newPassword),
    };

    const submit = async (e) => {
        e.preventDefault();
        setError('');
        if (form.newPassword !== form.confirm) { setError('Passwords do not match'); return; }
        if (!Object.values(checks).every(Boolean)) { setError('Password does not meet requirements'); return; }
        setLoading(true);
        try {
            await userApi.changePassword({ oldPassword: form.oldPassword, newPassword: form.newPassword });
            setSuccess('Password changed successfully!');
            setTimeout(onClose, 1500);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const toggle = (field) => setShow(s => ({ ...s, [field]: !s[field] }));

    return (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && onClose()}>
            <div className="modal modal--sm">
                <div className="modal__header">
                    <h3>Change Password</h3>
                    <button className="modal__close" onClick={onClose}>✕</button>
                </div>
                <form onSubmit={submit} className="modal__body">
                    {['old', 'new', 'confirm'].map((key, i) => (
                        <div className="field" key={key}>
                            <input
                                type={show[key] ? 'text' : 'password'}
                                placeholder={['Current Password', 'New Password', 'Confirm New Password'][i]}
                                value={form[key === 'confirm' ? 'confirm' : key === 'old' ? 'oldPassword' : 'newPassword']}
                                onChange={e => setForm({ ...form, [key === 'confirm' ? 'confirm' : key === 'old' ? 'oldPassword' : 'newPassword']: e.target.value })}
                                required
                            />
                            <button type="button" className="field__eye" onClick={() => toggle(key)}>
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                            </button>
                        </div>
                    ))}

                    <div className="password-checks">
                        {[
                            [checks.length, 'Minimum 8 characters'],
                            [checks.upper, 'Include uppercase & lowercase'],
                            [checks.special, 'Include number & special character'],
                        ].map(([ok, label]) => (
                            <div key={label} className={`pw-check${ok ? ' ok' : ''}`}>
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5"><polyline points="20 6 9 17 4 12"/></svg>
                                {label}
                            </div>
                        ))}
                    </div>

                    {error && <div className="error-msg">{error}</div>}
                    {success && <div className="success-msg">{success}</div>}

                    <div className="modal__footer">
                        <button type="button" className="btn-secondary" onClick={onClose}>Cancel</button>
                        <button type="submit" className="btn-primary" disabled={loading}>{loading ? <span className="spinner" /> : 'Reset Password'}</button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default function ProfilePage() {
    const { user: authUser, logout } = useAuth();
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [showPasswordModal, setShowPasswordModal] = useState(false);

    useEffect(() => {
        userApi.getProfile()
            .then(setProfile)
            .catch(() => setProfile(authUser))
            .finally(() => setLoading(false));
    }, []);

    const info = profile || authUser || {};
    const getInitials = (name) => name ? name.split(' ').map(w => w[0]).join('').toUpperCase().slice(0, 2) : '?';

    return (
        <div className="profile-page">
            <div className="profile-card">
                <div className="profile-card__header">
                    <h2>My Profile</h2>
                    <p>Manage your account information</p>
                </div>

                {loading ? (
                    <div className="loading-state"><div className="spinner spinner--lg" /></div>
                ) : (
                    <>
                        <div className="profile-avatar-section">
                            <div className="profile-avatar">{getInitials(info.name)}</div>
                            <h3 className="profile-name">{info.name || 'User'}</h3>
                            <p className="profile-email">{info.email || info.pnumber || ''}</p>
                        </div>

                        <div className="profile-info">
                            {info.pnumber && (
                                <div className="profile-info-row">
                                    <span className="profile-info-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M22 16.92v3a2 2 0 01-2.18 2 19.79 19.79 0 01-8.63-3.07A19.5 19.5 0 013.07 12.82a19.79 19.79 0 01-3.07-8.67A2 2 0 012 2h3a2 2 0 012 1.72c.127.96.361 1.903.7 2.81a2 2 0 01-.45 2.11L6.91 9.91a16 16 0 006.29 6.29l1.27-1.27a2 2 0 012.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0122 16.92z"/></svg></span>
                                    <span className="profile-info-label">Phone</span>
                                    <span className="profile-info-value">{info.pnumber}</span>
                                </div>
                            )}
                            <div className="profile-info-row">
                                <span className="profile-info-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg></span>
                                <span className="profile-info-label">Member Since</span>
                                <span className="profile-info-value">2024</span>
                            </div>
                            <div className="profile-info-row">
                                <span className="profile-info-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></span>
                                <span className="profile-info-label">Role</span>
                                <span className="profile-info-value">User</span>
                            </div>
                        </div>

                        <div className="profile-actions">
                            <button className="btn-primary btn-profile-action" onClick={() => setShowPasswordModal(true)}>
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0110 0v4"/></svg>
                                Change Password
                            </button>
                            <button className="btn-logout" onClick={logout}>
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                                Logout
                            </button>
                        </div>
                    </>
                )}
            </div>

            {showPasswordModal && <ChangePasswordModal onClose={() => setShowPasswordModal(false)} />}
        </div>
    );
}
