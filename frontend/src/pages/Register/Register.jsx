import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link, Navigate, useNavigate } from 'react-router-dom';
import { FiEye, FiEyeOff, FiMail, FiLock, FiUser, FiCheckCircle, FiCircle } from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';
import { validateEmailOrPhone, passwordRules, isStrongPassword } from '../../utils/validation';
import styles from './Register.module.css';

const Register = () => {
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);

  const { register: registerAccount, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm({
    defaultValues: { fullName: '', emailOrPhone: '', password: '', confirmPassword: '' },
  });

  if (isAuthenticated) return <Navigate to="/dashboard" replace />;

  const password = watch('password') || '';

  const onSubmit = async (values) => {
    setError('');
    setSubmitting(true);
    try {
      await registerAccount({
        fullName: values.fullName,
        emailOrPhone: values.emailOrPhone,
        password: values.password,
      });
      navigate('/dashboard', { replace: true });
    } catch (apiError) {
      setError(apiError.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
      <div className={styles.page}>
        {/* Left panel */}
        <div className={styles.leftPanel}>
          <div className={styles.leftContent}>
            <div className={styles.illustration}>
              <div className={styles.laptopIcon}>💻</div>
              <div className={styles.floatingBadge} style={{ top: '15%', right: '10%' }}>
                <FiUser size={14} />
                <span>Contact</span>
              </div>
              <div className={styles.floatingBadge} style={{ bottom: '20%', left: '5%' }}>
                <FiMail size={14} />
                <span>Email</span>
              </div>
            </div>
            <h2 className={styles.leftTitle}>Create your account</h2>
            <p className={styles.leftDesc}>Join us and start managing your contacts easily.</p>

            <div className={styles.dots}>
              <div className={styles.dot} style={{ background: '#a78bfa' }} />
              <div className={styles.dot} style={{ background: '#60a5fa' }} />
              <div className={styles.dot} style={{ background: '#f472b6' }} />
            </div>
          </div>
        </div>

        {/* Right panel */}
        <div className={styles.rightPanel}>
          <div className={styles.formCard}>
            <div className={styles.formHeader}>
              <h2 className={styles.formTitle}>Create Account</h2>
              <p className={styles.formSubtitle}>Fill in the details to get started</p>
            </div>

            {error && (
                <div className={styles.errorBanner}>
                  <span>⚠</span> {error}
                </div>
            )}

            <form className={styles.form} onSubmit={handleSubmit(onSubmit)}>
              {/* Full Name */}
              <div className={styles.fieldGroup}>
                <label className={styles.fieldLabel}>Full Name</label>
                <div className={styles.inputWrapper}>
                  <FiUser className={styles.inputIcon} />
                  <input
                      className={`${styles.input} ${errors.fullName ? styles.inputError : ''}`}
                      placeholder="Enter your full name"
                      {...register('fullName', { required: 'Full name is required.' })}
                  />
                </div>
                {errors.fullName && <span className={styles.fieldError}>{errors.fullName.message}</span>}
              </div>

              {/* Email or Phone */}
              <div className={styles.fieldGroup}>
                <label className={styles.fieldLabel}>Email or Phone Number</label>
                <div className={styles.inputWrapper}>
                  <FiMail className={styles.inputIcon} />
                  <input
                      className={`${styles.input} ${errors.emailOrPhone ? styles.inputError : ''}`}
                      placeholder="Enter your email or phone number"
                      {...register('emailOrPhone', { validate: validateEmailOrPhone })}
                  />
                </div>
                {errors.emailOrPhone && <span className={styles.fieldError}>{errors.emailOrPhone.message}</span>}
              </div>

              {/* Password */}
              <div className={styles.fieldGroup}>
                <label className={styles.fieldLabel}>Password</label>
                <div className={styles.inputWrapper}>
                  <FiLock className={styles.inputIcon} />
                  <input
                      className={`${styles.input} ${errors.password ? styles.inputError : ''}`}
                      type={showPassword ? 'text' : 'password'}
                      placeholder="Create a password"
                      {...register('password', {
                        required: 'Password is required.',
                        validate: (value) => isStrongPassword(value) || 'Password does not meet all requirements.',
                      })}
                  />
                  <button
                      type="button"
                      className={styles.eyeBtn}
                      onClick={() => setShowPassword((v) => !v)}
                      tabIndex={-1}
                  >
                    {showPassword ? <FiEyeOff /> : <FiEye />}
                  </button>
                </div>
                {errors.password && <span className={styles.fieldError}>{errors.password.message}</span>}

                {/* Password strength rules */}
                {password.length > 0 && (
                    <ul className={styles.rules}>
                      {passwordRules.map((rule) => {
                        const met = rule.test(password);
                        return (
                            <li key={rule.id} className={`${styles.rule} ${met ? styles.valid : ''}`}>
                              {met
                                  ? <FiCheckCircle className={styles.ruleIcon} />
                                  : <FiCircle className={styles.ruleIcon} />
                              }
                              {rule.label}
                            </li>
                        );
                      })}
                    </ul>
                )}
              </div>

              {/* Confirm Password */}
              <div className={styles.fieldGroup}>
                <label className={styles.fieldLabel}>Confirm Password</label>
                <div className={styles.inputWrapper}>
                  <FiLock className={styles.inputIcon} />
                  <input
                      className={`${styles.input} ${errors.confirmPassword ? styles.inputError : ''}`}
                      type={showConfirm ? 'text' : 'password'}
                      placeholder="Confirm your password"
                      {...register('confirmPassword', {
                        required: 'Please confirm your password.',
                        validate: (value) => value === password || 'Passwords do not match.',
                      })}
                  />
                  <button
                      type="button"
                      className={styles.eyeBtn}
                      onClick={() => setShowConfirm((v) => !v)}
                      tabIndex={-1}
                  >
                    {showConfirm ? <FiEyeOff /> : <FiEye />}
                  </button>
                </div>
                {errors.confirmPassword && <span className={styles.fieldError}>{errors.confirmPassword.message}</span>}
              </div>

              {/* Terms */}
              <label className={styles.termsLabel}>
                <input
                    type="checkbox"
                    className={styles.checkbox}
                    {...register('terms', { required: 'You must agree to the terms.' })}
                />
                <span>
                I agree to the{' '}
                  <a href="#" className={styles.termsLink}>Terms &amp; Conditions</a>
              </span>
              </label>
              {errors.terms && <span className={styles.fieldError}>{errors.terms.message}</span>}

              <button className={styles.submitBtn} type="submit" disabled={submitting}>
                {submitting ? <span className={styles.loadingSpinner} /> : null}
                {submitting ? 'Creating account...' : 'Create Account'}
              </button>
            </form>

            <p className={styles.switchText}>
              Already have an account?{' '}
              <Link className={styles.switchLink} to="/login">
                Sign in
              </Link>
            </p>
          </div>
        </div>
      </div>
  );
};

export default Register;