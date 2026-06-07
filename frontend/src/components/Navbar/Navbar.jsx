import { useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { FiMenu, FiX, FiHome, FiUsers, FiUser, FiUploadCloud, FiLogOut } from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';
import styles from './Navbar.module.css';

const navItems = [
  { label: 'Dashboard', path: '/dashboard', icon: <FiHome /> },
  { label: 'Contacts', path: '/contacts', icon: <FiUsers /> },
  { label: 'My Profile', path: '/profile', icon: <FiUser /> },
  { label: 'Import / Export', path: '/import-export', icon: <FiUploadCloud /> },
];

const Navbar = () => {
  const [open, setOpen] = useState(false);
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login', { replace: true });
  };

  return (
      <header className={styles.navWrap}>
        <nav className={styles.nav} aria-label="Primary navigation">
          <NavLink className={styles.brand} to="/dashboard">
          <span className={styles.logo}>
            <FiUsers size={17} />
          </span>
            <span>Contact Manager</span>
          </NavLink>

          <button className={styles.menuButton} type="button" onClick={() => setOpen((v) => !v)} aria-label="Toggle menu">
            {open ? <FiX /> : <FiMenu />}
          </button>

          <div className={`${styles.links} ${open ? styles.open : ''}`}>
            {navItems.map((item) => (
                <NavLink
                    key={item.path}
                    className={({ isActive }) => `${styles.link} ${isActive ? styles.active : ''}`}
                    to={item.path}
                    onClick={() => setOpen(false)}
                >
                  <span className={styles.navIcon}>{item.icon}</span>
                  {item.label}
                </NavLink>
            ))}

            <span className={styles.divider} />

            <button className={styles.logout} type="button" onClick={handleLogout}>
              <span className={styles.navIcon}><FiLogOut /></span>
              Logout
            </button>
          </div>
        </nav>
      </header>
  );
};

export default Navbar;