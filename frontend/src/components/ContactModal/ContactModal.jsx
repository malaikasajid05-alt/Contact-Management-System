import { useEffect } from 'react';
import { useFieldArray, useForm } from 'react-hook-form';
import Modal from '../Modal/Modal';
import { splitDetails } from '../../utils/contactUtils';
import { emailPattern, phonePattern } from '../../utils/validation';
import styles from './ContactModal.module.css';

const EMAIL_LABELS = ['Work', 'Personal', 'Business', 'School / University', 'Friend', 'Other'];
const PHONE_LABELS = ['Mobile', 'Work', 'Home', 'Personal', 'Business', 'WhatsApp', 'Emergency', 'Other'];

const emptyEmail = { label: 'Work', value: '', type: 'EMAIL' };
const emptyPhone = { label: 'Mobile', value: '', type: 'PHONE' };

const createDefaults = (contact) => {
  const details = splitDetails(contact?.details || []);

  return {
    firstName: contact?.firstName || '',
    lastName: contact?.lastName || '',
    title: contact?.jobTitle || '',
    emails: details.emails.length ? details.emails : [emptyEmail],
    phones: details.phones.length ? details.phones : [emptyPhone],
  };
};

const LabelSelect = ({ options, value, onChange, disabled }) => {
  const selectValue = options.includes(value) ? value : options[0];

  return (
      <select
          className="form-input"
          disabled={disabled}
          value={selectValue}
          onChange={(e) => onChange(e.target.value)}
      >
        {options.map((opt) => (
            <option key={opt} value={opt}>{opt}</option>
        ))}
      </select>
  );
};

const ContactModal = ({ mode, contact, saving, apiError, onClose, onSubmit }) => {
  const readOnly = mode === 'view';
  const title = mode === 'create' ? 'Add Contact' : mode === 'edit' ? 'Edit Contact' : 'Contact Details';
  const {
    register,
    control,
    handleSubmit,
    reset,
    watch,
    setValue,
    formState: { errors },
  } = useForm({ defaultValues: createDefaults(contact) });

  const emails = useFieldArray({ control, name: 'emails' });
  const phones = useFieldArray({ control, name: 'phones' });

  const watchedEmails = watch('emails');
  const watchedPhones = watch('phones');

  useEffect(() => {
    reset(createDefaults(contact));
  }, [contact, reset]);

  const handleSubmitWithTitle = (values) => {
    onSubmit({ ...values, jobTitle: values.title });
  };

  const footer = readOnly ? (
      <button className="btn btn-primary" type="button" onClick={onClose}>
        Close
      </button>
  ) : (
      <>
        <button className="btn btn-secondary" type="button" onClick={onClose}>
          Cancel
        </button>
        <button className="btn btn-primary" type="submit" form="contact-form" disabled={saving}>
          {saving ? 'Saving...' : 'Save Details'}
        </button>
      </>
  );

  return (
      <Modal title={title} onClose={onClose} footer={footer}>
        {apiError ? <div className="error-state">{apiError}</div> : null}
        <form id="contact-form" onSubmit={handleSubmit(handleSubmitWithTitle)}>
          <section className={styles.section}>
            <h3 className={styles.sectionTitle}>Personal Information</h3>
            <div className="form-grid">
              <label className="form-field">
                <span className="form-label">First Name *</span>
                <input className="form-input" disabled={readOnly} {...register('firstName', { required: 'First name is required.' })} />
                {errors.firstName ? <span className="form-error">{errors.firstName.message}</span> : null}
              </label>
              <label className="form-field">
                <span className="form-label">Last Name</span>
                <input className="form-input" disabled={readOnly} {...register('lastName')} />
              </label>
              <label className="form-field">
                <span className="form-label">Title</span>
                <input className="form-input" disabled={readOnly} {...register('title')} />
              </label>
            </div>
          </section>

          <section className={styles.section}>
            <h3 className={styles.sectionTitle}>Emails</h3>
            {emails.fields.map((field, index) => (
                <div className={styles.dynamicRow} key={field.id}>
                  <LabelSelect
                      options={EMAIL_LABELS}
                      value={watchedEmails?.[index]?.label ?? 'Work'}
                      onChange={(val) => setValue(`emails.${index}.label`, val)}
                      disabled={readOnly}
                  />
                  <input
                      className="form-input"
                      disabled={readOnly}
                      placeholder="name@example.com"
                      {...register(`emails.${index}.value`, {
                        validate: (value) => !value || emailPattern.test(value) || 'Enter a valid email.',
                      })}
                  />
                  {!readOnly ? (
                      <button className="btn btn-secondary" type="button" onClick={() => emails.remove(index)}>
                        Remove
                      </button>
                  ) : null}
                  {errors.emails?.[index]?.value ? <span className="form-error">{errors.emails[index].value.message}</span> : null}
                </div>
            ))}
            {!readOnly ? (
                <button className="btn btn-ghost" type="button" onClick={() => emails.append(emptyEmail)}>
                  Add Email
                </button>
            ) : null}
          </section>

          <section className={styles.section}>
            <h3 className={styles.sectionTitle}>Phone Numbers</h3>
            {phones.fields.map((field, index) => (
                <div className={styles.dynamicRow} key={field.id}>
                  <LabelSelect
                      options={PHONE_LABELS}
                      value={watchedPhones?.[index]?.label ?? 'Mobile'}
                      onChange={(val) => setValue(`phones.${index}.label`, val)}
                      disabled={readOnly}
                  />
                  <input
                      className="form-input"
                      disabled={readOnly}
                      placeholder="+1 555 0123"
                      {...register(`phones.${index}.value`, {
                        validate: (value) => !value || phonePattern.test(value) || 'Enter a valid phone number.',
                      })}
                  />
                  {!readOnly ? (
                      <button className="btn btn-secondary" type="button" onClick={() => phones.remove(index)}>
                        Remove
                      </button>
                  ) : null}
                  {errors.phones?.[index]?.value ? <span className="form-error">{errors.phones[index].value.message}</span> : null}
                </div>
            ))}
            {!readOnly ? (
                <button className="btn btn-ghost" type="button" onClick={() => phones.append(emptyPhone)}>
                  Add Phone
                </button>
            ) : null}
          </section>
        </form>
      </Modal>
  );
};

export default ContactModal;