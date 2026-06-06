import { FiMail, FiPhone, FiEye, FiEdit2, FiTrash2 } from 'react-icons/fi';
import styles from './ContactCard.module.css';

const initials = (name) =>
    name
        .split(' ')
        .filter(Boolean)
        .slice(0, 2)
        .map((part) => part[0])
        .join('')
        .toUpperCase() || 'C';

// Deterministic colorful gradient per contact name
const AVATAR_COLORS = [
    ['#7C3AED', '#6366F1'], // purple → indigo
    ['#2563EB', '#06B6D4'], // blue → cyan
    ['#059669', '#10B981'], // emerald
    ['#D97706', '#F59E0B'], // amber
    ['#DC2626', '#F87171'], // red
    ['#7C3AED', '#EC4899'], // purple → pink
    ['#0891B2', '#6366F1'], // cyan → indigo
    ['#16A34A', '#84CC16'], // green → lime
    ['#EA580C', '#FBBF24'], // orange → yellow
    ['#9333EA', '#F472B6'], // violet → pink
];

const getAvatarColors = (name) => {
    let hash = 0;
    for (let i = 0; i < name.length; i++) hash = name.charCodeAt(i) + ((hash << 5) - hash);
    return AVATAR_COLORS[Math.abs(hash) % AVATAR_COLORS.length];
};

const ContactCard = ({ contact, onView, onEdit, onDelete }) => {
    const [c1, c2] = getAvatarColors(contact.fullName || 'C');

    return (
        <article className={styles.card}>
            <div
                className={styles.avatar}
                style={{ background: `linear-gradient(135deg, ${c1}, ${c2})` }}
            >
                {initials(contact.fullName)}
            </div>
            <h3 className={styles.name}>{contact.fullName}</h3>
            <p className={styles.title}>{contact.jobTitle || ''}</p>
            <p className={styles.detail}>
                <FiMail /> {contact.email || 'No email added'}
            </p>
            <p className={styles.detail}>
                <FiPhone /> {contact.phone || 'No phone added'}
            </p>
            <div className={styles.actions}>
                <button className={styles.btnView} type="button" onClick={() => onView(contact)}>
                    <FiEye /> View
                </button>
                <button className={styles.btnEdit} type="button" onClick={() => onEdit(contact)}>
                    <FiEdit2 /> Edit
                </button>
                <button className={styles.btnDelete} type="button" onClick={() => onDelete(contact)}>
                    <FiTrash2 /> Delete
                </button>
            </div>
        </article>
    );
};

export default ContactCard;