import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FiPhone, FiLock, FiLogOut } from 'react-icons/fi';
import ChangePasswordModal from '../../components/ChangePasswordModal/ChangePasswordModal';
import { changePassword, getUserProfile } from '../../api/userApi';
import { useAuth } from '../../context/AuthContext';
import userAvatar from '../../assets/user_avatar.jpg';
import styles from './Profile.module.css';

const Profile = () => {
  const { user, setUser, logout } = useAuth();
  const [profile, setProfile] = useState(user);
  const [loading, setLoading] = useState(!user);
  const [error, setError] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [modalError, setModalError] = useState('');
  const [saving, setSaving] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const loadProfile = async () => {
      setLoading(true);
      setError('');
      try {
        const payload = await getUserProfile();
        setProfile(payload);
        setUser(payload);
        localStorage.setItem('user', JSON.stringify(payload));
      } catch (apiError) {
        setError(apiError.message);
      } finally {
        setLoading(false);
      }
    };
    loadProfile();
  }, [setUser]);

  // Use fullName from registration — never fall back to "User Profile"
  const name = profile?.fullName || profile?.name || `${profile?.firstName || ''} ${profile?.lastName || ''}`.trim() || '';
  const email = profile?.email || profile?.emailOrPhone || 'No email added';
  const phone = profile?.phone || profile?.phoneNumber || 'No phone added';

  const handleLogout = () => {
    logout();
    navigate('/login', { replace: true });
  };

  const handlePasswordChange = async (values) => {
    setSaving(true);
    setModalError('');
    try {
      await changePassword(values);
      setModalOpen(false);
    } catch (apiError) {
      setModalError(apiError.message);
    } finally {
      setSaving(false);
    }
  };

  return (
      <main className="page">
        {loading ? <div className="loading-state">Loading profile...</div> : null}
        {error ? <div className="error-state">{error}</div> : null}
        {!loading && !error ? (
            <div className={styles.wrapper}>
              <aside className={styles.leftCard}>
                <div className={styles.heroGradient}>
                  <h1 className={styles.heroTitle}>My Profile</h1>
                  <p className={styles.heroSub}>Manage your account information</p>
                </div>

                <div className={styles.avatarWrap}>
                  <img src={userAvatar} alt="User avatar" className={styles.avatar} />
                </div>

                <div className={styles.leftBody}>
                  {/* Show the name from registration */}
                  {name ? <h2 className={styles.name}>{name}</h2> : null}
                  <p className={styles.emailSub}>{email}</p>

                  <div className={styles.infoRow}>
                    <span className={styles.infoIcon}><FiPhone /></span>
                    <span className={styles.infoLabel}>Phone</span>
                    <span className={styles.infoValue}>{phone}</span>
                  </div>

                  <div className={styles.leftButtons}>
                    <button className={styles.btnPassword} type="button" onClick={() => setModalOpen(true)}>
                      <FiLock /> Change Password
                    </button>
                    <button className={styles.btnLogout} type="button" onClick={handleLogout}>
                      <FiLogOut /> Logout
                    </button>
                  </div>
                </div>
              </aside>
            </div>
        ) : null}

        {modalOpen ? (
            <ChangePasswordModal saving={saving} error={modalError} onClose={() => setModalOpen(false)} onSubmit={handlePasswordChange} />
        ) : null}
      </main>
  );
};

export default Profile;