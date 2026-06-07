import { useMemo } from 'react';
import { FiBriefcase, FiClock, FiUsers, FiUser, FiArrowRight } from 'react-icons/fi';
import { useNavigate } from 'react-router-dom';
import ContactCard from '../../components/ContactCard/ContactCard';
import { useContacts } from '../../hooks/useContacts';
import { isWorkContact } from '../../utils/contactUtils';
import styles from './Dashboard.module.css';

const RECENT_LIMIT = 6;

const Dashboard = () => {
  const { contacts, loading, error } = useContacts();
  const navigate = useNavigate();

  // Always show the 6 most recently added contacts on the dashboard
  const recentContacts = useMemo(
      () => [...contacts].sort((a, b) => (b.id || 0) - (a.id || 0)).slice(0, RECENT_LIMIT),
      [contacts]
  );

  const workCount = contacts.filter(isWorkContact).length;
  const personalCount = contacts.length - workCount;

  return (
      <main className="page">
        <section className={styles.hero}>
          <h1>Manage every contact here.</h1>
          <p>Search, filter, update and organize personal and work contacts from one responsive dashboard.</p>
        </section>

        <section className={styles.statsGrid} aria-label="Contact statistics">
          <article className={`card ${styles.stat}`}>
            <span className={styles.statIcon}><FiUsers /></span>
            <div className={styles.statValue}>{contacts.length}</div>
            <div className={styles.statLabel}>Total Contacts</div>
          </article>
          <article className={`card ${styles.stat}`}>
            <span className={styles.statIcon}><FiBriefcase /></span>
            <div className={styles.statValue}>{workCount}</div>
            <div className={styles.statLabel}>Work Contacts</div>
          </article>
          <article className={`card ${styles.stat}`}>
            <span className={styles.statIcon}><FiUser /></span>
            <div className={styles.statValue}>{personalCount}</div>
            <div className={styles.statLabel}>Personal Contacts</div>
          </article>
          <article className={`card ${styles.stat}`}>
            <span className={styles.statIcon}><FiClock /></span>
            <div className={styles.statValue}>{Math.min(contacts.length, RECENT_LIMIT)}</div>
            <div className={styles.statLabel}>Recently Added</div>
          </article>
        </section>

        {/* Section header */}
        <div className={styles.sectionHeader}>
          <div>
            <h2 className="page-title">Recent Contacts</h2>
            <p className={styles.sectionMeta}>
              Showing your <strong>{Math.min(contacts.length, RECENT_LIMIT)} most recently added</strong> contacts.{' '}
              <button className={styles.allLink} type="button" onClick={() => navigate('/contacts')}>
                View all contacts <FiArrowRight />
              </button>
            </p>
          </div>
        </div>

        {loading ? <div className="loading-state">Loading contacts...</div> : null}
        {error ? <div className="error-state">{error}</div> : null}
        {!loading && !error && recentContacts.length === 0 ? (
            <div className="empty-state">No contacts yet. <button className={styles.allLink} type="button" onClick={() => navigate('/contacts')}>Add your first contact →</button></div>
        ) : null}

        <section className={styles.grid}>
          {recentContacts.map((contact) => (
              <ContactCard
                  key={contact.id}
                  contact={contact}
                  onView={() => navigate('/contacts')}
                  onEdit={() => navigate('/contacts')}
                  onDelete={() => navigate('/contacts')}
              />
          ))}
        </section>
      </main>
  );
};

export default Dashboard;