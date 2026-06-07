import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link, Navigate, useNavigate } from 'react-router-dom';
import { FiEye, FiEyeOff, FiMail, FiLock, FiUsers } from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';
import { validateEmailOrPhone } from '../../utils/validation';
import styles from './Login.module.css';

const Login = () => {
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const { login, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: {
      emailOrPhone: '',
      password: '',
      remember: true,
    },
  });

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }

  const onSubmit = async (values) => {
    setError('');
    setSubmitting(true);
    try {
      await login({ emailOrPhone: values.emailOrPhone, password: values.password });
      navigate('/dashboard', { replace: true });
    } catch (apiError) {
      setError(apiError.message || 'Login failed.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
      <div className={styles.page}>
        {/* Left panel */}
        <div className={styles.leftPanel}>
          <div className={styles.leftContent}>
            <div className={styles.brandMark}>
              <FiUsers className={styles.brandIcon} />
            </div>
            <h1 className={styles.brandName}>Contact Manager</h1>
            <p className={styles.brandTagline}>Stay Connected,<br />Stay Organized.</p>
            <p className={styles.brandDesc}>Your contacts, managed beautifully.</p>

            <div className={styles.floatingCard}>
              <div className={styles.floatingDot} style={{ background: '#a78bfa', top: '10%', left: '15%' }} />
              <div className={styles.floatingDot} style={{ background: '#60a5fa', top: '60%', left: '80%', width: '10px', height: '10px' }} />
              <div className={styles.floatingDot} style={{ background: '#f472b6', top: '80%', left: '20%', width: '8px', height: '8px' }} />
              <div className={styles.floatingDot} style={{ background: '#34d399', top: '30%', left: '70%', width: '12px', height: '12px' }} />
            </div>
          </div>
        </div>

        {/* Right panel */}
        <div className={styles.rightPanel}>
          <div className={styles.formCard}>
            <div className={styles.formHeader}>
              <h2 className={styles.formTitle}>Welcome Back! 👋</h2>
              <p className={styles.formSubtitle}>Sign in to continue to your account.</p>
            </div>

            {error && (
                <div className={styles.errorBanner}>
                  <span>⚠</span> {error}
                </div>
            )}

            <form className={styles.form} onSubmit={handleSubmit(onSubmit)}>
              <div className={styles.fieldGroup}>
                <label className={styles.fieldLabel}>Email or Phone Number</label>
                <div className={styles.inputWrapper}>
                  <FiMail className={styles.inputIcon} />
                  <input
                      className={`${styles.input} ${errors.emailOrPhone ? styles.inputError : ''}`}
                      placeholder="Enter your email or phone"
                      {...register('emailOrPhone', { validate: validateEmailOrPhone })}
                  />
                </div>
                {errors.emailOrPhone && (
                    <span className={styles.fieldError}>{errors.emailOrPhone.message}</span>
                )}
              </div>

              <div className={styles.fieldGroup}>
                <label className={styles.fieldLabel}>Password</label>
                <div className={styles.inputWrapper}>
                  <FiLock className={styles.inputIcon} />
                  <input
                      className={`${styles.input} ${errors.password ? styles.inputError : ''}`}
                      type={showPassword ? 'text' : 'password'}
                      placeholder="Enter your password"
                      {...register('password', { required: 'Password is required.' })}
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
                {errors.password && (
                    <span className={styles.fieldError}>{errors.password.message}</span>
                )}
              </div>

              <div className={styles.formRow}>
                <label className={styles.rememberLabel}>
                  <input type="checkbox" className={styles.checkbox} {...register('remember')} />
                  <span>Remember me</span>
                </label>
              </div>

              <button
                  className={styles.submitBtn}
                  type="submit"
                  disabled={submitting}
              >
                {submitting ? (
                    <span className={styles.loadingSpinner} />
                ) : null}
                {submitting ? 'Signing in...' : 'Sign In'}
              </button>

            </form>

            <p className={styles.switchText}>
              Don&apos;t have an account?{' '}
              <Link className={styles.switchLink} to="/register">
                Create one
              </Link>
            </p>
          </div>
        </div>
      </div>
  );
};

export default Login;