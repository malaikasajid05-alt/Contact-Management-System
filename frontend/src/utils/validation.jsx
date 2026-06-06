export const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

// Accepts: 03001234567 | +923001234567 | 923001234567
export const phonePattern = /^(\+92|92|0)?3\d{9}$/;

export const validateEmailOrPhone = (value) => {
  if (!value || !value.trim()) return 'Email or phone number is required.';
  const trimmed = value.trim();
  if (emailPattern.test(trimmed) || phonePattern.test(trimmed)) return true;
  if (/^\+?[0-9]+$/.test(trimmed)) {
    return 'Enter a valid phone number (e.g. 03001234567 or +923001234567).';
  }
  return 'Enter a valid email address or phone number.';
};

export const passwordRules = [
  { id: 'length', label: 'Minimum 8 characters', test: (value) => value.length >= 8 },
  { id: 'upper', label: 'Uppercase letter', test: (value) => /[A-Z]/.test(value) },
  { id: 'lower', label: 'Lowercase letter', test: (value) => /[a-z]/.test(value) },
  { id: 'number', label: 'Number', test: (value) => /\d/.test(value) },
  { id: 'special', label: 'Special character', test: (value) => /[^A-Za-z0-9]/.test(value) },
];

export const isStrongPassword = (value) => passwordRules.every((rule) => rule.test(value || ''));