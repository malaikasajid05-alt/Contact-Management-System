import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { FiEye, FiEyeOff, FiCheckCircle, FiCircle } from 'react-icons/fi';
import Modal from '../Modal/Modal';
import { isStrongPassword, passwordRules } from '../../utils/validation';
import styles from './ChangePasswordModal.module.css';

const PasswordField = ({ label, registration, error, placeholder }) => {
  const [show, setShow] = useState(false);
  return (
      <label className="form-field full">
        <span className="form-label">{label}</span>
        <div className={styles.inputWrap}>
          <input
              className={`form-input ${styles.passwordInput}`}
              type={show ? 'text' : 'password'}
              placeholder={placeholder}
              {...registration}
          />
          <button
              type="button"
              className={styles.eyeBtn}
              onClick={() => setShow((v) => !v)}
              tabIndex={-1}
          >
            {show ? <FiEyeOff /> : <FiEye />}
          </button>
        </div>
        {error ? <span className="form-error">{error}</span> : null}
      </label>
  );
};

const ChangePasswordModal = ({ saving, error, onClose, onSubmit }) => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm({ defaultValues: { currentPassword: '', newPassword: '', confirmPassword: '' } });

  const newPassword = watch('newPassword') || '';

  return (
      <Modal
          title="Change Password"
          onClose={onClose}
          footer={
            <>
              <button className="btn btn-secondary" type="button" onClick={onClose}>
                Cancel
              </button>
              <button className="btn btn-primary" type="submit" form="password-form" disabled={saving}>
                {saving ? 'Resetting...' : 'Reset Password'}
              </button>
            </>
          }
      >
        {error ? <div className="error-state">{error}</div> : null}
        <form id="password-form" className="form-grid" onSubmit={handleSubmit(onSubmit)}>
          <PasswordField
              label="Current Password"
              placeholder="Enter current password"
              registration={register('currentPassword', { required: 'Current password is required.' })}
              error={errors.currentPassword?.message}
          />
          <PasswordField
              label="New Password"
              placeholder="Enter new password"
              registration={register('newPassword', {
                required: 'New password is required.',
                validate: (value) => isStrongPassword(value) || 'Password does not meet all requirements.',
              })}
              error={errors.newPassword?.message}
          />

          <ul className={styles.rules}>
            {passwordRules.map((rule) => {
              const met = rule.test(newPassword);
              return (
                  <li className={`${styles.rule} ${met ? styles.valid : ''}`} key={rule.id}>
                    {met ? <FiCheckCircle className={styles.ruleIcon} /> : <FiCircle className={styles.ruleIcon} />}
                    {rule.label}
                  </li>
              );
            })}
          </ul>

          <PasswordField
              label="Confirm New Password"
              placeholder="Confirm new password"
              registration={register('confirmPassword', {
                required: 'Please confirm your password.',
                validate: (value) => value === newPassword || 'Passwords do not match.',
              })}
              error={errors.confirmPassword?.message}
          />
        </form>
      </Modal>
  );
};

export default ChangePasswordModal;