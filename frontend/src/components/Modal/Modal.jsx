import { FiX } from 'react-icons/fi';
import styles from './Modal.module.css';

const Modal = ({ title, children, footer, onClose, size = 'md' }) => (
    <div className={styles.backdrop} role="presentation" onMouseDown={onClose}>
        <section
            className={`${styles.modal} ${styles[size]}`}
            role="dialog"
            aria-modal="true"
            aria-label={title}
            onMouseDown={(event) => event.stopPropagation()}
        >
            <header className={styles.header}>
                <h2 className={styles.title}>{title}</h2>
                <button className={styles.close} type="button" onClick={onClose} aria-label="Close modal">
                    <FiX />
                </button>
            </header>
            <div className={styles.body}>{children}</div>
            {footer ? <footer className={styles.footer}>{footer}</footer> : null}
        </section>
    </div>
);

export default Modal;