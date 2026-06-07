import { useMemo, useState } from 'react';
import ContactCard from '../../components/ContactCard/ContactCard';
import ContactModal from '../../components/ContactModal/ContactModal';
import DeleteModal from '../../components/DeleteModal/DeleteModal';
import Pagination from '../../components/Pagination/Pagination';
import SearchBar from '../../components/SearchBar/SearchBar';
import {
  buildContactPayload,
  buildDetailPayload,
  filterContacts,
  normalizeContact,
  paginate,
  sortContacts,
} from '../../utils/contactUtils';
import {
  createContact,
  createContactDetail,
  deleteContact,
  deleteContactDetail,
  getContactDetails,
  updateContact,
  updateContactDetail,
} from '../../api/contactApi';
import { useContacts } from '../../hooks/useContacts';
import styles from './Contacts.module.css';

const pageSize = 8;

const mergeDetails = (values) =>
    [...values.emails.map((item) => ({ ...item, type: 'EMAIL' })), ...values.phones.map((item) => ({ ...item, type: 'PHONE' }))].filter(
        (item) => item.value?.trim(),
    );

const Contacts = () => {
  const { contacts, loading, error, reload } = useContacts();
  const [query, setQuery] = useState('');
  const [sortBy, setSortBy] = useState('nameAsc');
  const [page, setPage] = useState(1);
  const [modalState, setModalState] = useState({ mode: null, contact: null });
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [actionError, setActionError] = useState('');
  const [saving, setSaving] = useState(false);

  const processedContacts = useMemo(() => {
    const filtered = filterContacts(contacts, query);
    return sortContacts(filtered, sortBy);
  }, [contacts, query, sortBy]);

  const totalPages = Math.max(1, Math.ceil(processedContacts.length / pageSize));
  const visibleContacts = paginate(processedContacts, Math.min(page, totalPages), pageSize);

  const openModal = async (mode, contact = null) => {
    setActionError('');
    if (!contact) {
      setModalState({ mode, contact: null });
      return;
    }
    try {
      const details = await getContactDetails(contact.id);
      setModalState({ mode, contact: normalizeContact({ ...contact, details }) });
    } catch {
      setModalState({ mode, contact });
    }
  };

  const closeModal = () => {
    setModalState({ mode: null, contact: null });
    setActionError('');
  };

  const saveDetails = async (contactId, originalDetails, nextDetails) => {
    const nextIds = nextDetails.filter((d) => d.id).map((d) => d.id);
    const removed = originalDetails.filter((d) => d.id && !nextIds.includes(d.id));
    await Promise.all(
        nextDetails.map((detail) =>
            detail.id
                ? updateContactDetail(contactId, detail.id, buildDetailPayload(detail))
                : createContactDetail(contactId, buildDetailPayload(detail)),
        ),
    );
    await Promise.all(removed.map((detail) => deleteContactDetail(contactId, detail.id)));
  };

  const handleSave = async (values) => {
    setSaving(true);
    setActionError('');
    try {
      const payload = buildContactPayload(values);
      const nextDetails = mergeDetails(values);
      const current = modalState.contact;

      if (modalState.mode === 'create') {
        const created = await createContact(payload);
        const createdId = created.id || created.contactId || created.data?.id;
        if (createdId) await saveDetails(createdId, [], nextDetails);
      } else {
        await updateContact(current.id, payload);
        await saveDetails(current.id, current.details || [], nextDetails);
      }

      await reload();
      closeModal();
    } catch (apiError) {
      setActionError(apiError.message);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    setSaving(true);
    setActionError('');
    try {
      await deleteContact(deleteTarget.id);
      setDeleteTarget(null);
      await reload();
    } catch (apiError) {
      setActionError(apiError.message);
    } finally {
      setSaving(false);
    }
  };

  return (
      <main className="page">
        <div className="page-header">
          <div>
            <h1 className="page-title">Contacts</h1>
            <p className="page-subtitle">
              {processedContacts.length} contact{processedContacts.length !== 1 ? 's' : ''} — search, sort, and manage them below.
            </p>
          </div>
          <div className={styles.actions}>
            <button className="btn btn-primary" type="button" onClick={() => openModal('create')}>
              + Add Contact
            </button>
          </div>
        </div>

        <SearchBar
            query={query}
            onQueryChange={(value) => { setQuery(value); setPage(1); }}
            sortBy={sortBy}
            onSortChange={(value) => { setSortBy(value); setPage(1); }}
        />

        {loading ? <div className="loading-state">Loading contacts...</div> : null}
        {error ? <div className="error-state">{error}</div> : null}
        {!loading && !error && visibleContacts.length === 0 ? (
            <div className="empty-state">No contacts match your search.</div>
        ) : null}

        <section className={styles.grid}>
          {visibleContacts.map((contact) => (
              <ContactCard
                  key={contact.id}
                  contact={contact}
                  onView={() => openModal('view', contact)}
                  onEdit={() => openModal('edit', contact)}
                  onDelete={() => { setActionError(''); setDeleteTarget(contact); }}
              />
          ))}
        </section>
        <Pagination page={Math.min(page, totalPages)} totalPages={totalPages} onPageChange={setPage} />

        {modalState.mode ? (
            <ContactModal
                mode={modalState.mode}
                contact={modalState.contact}
                saving={saving}
                apiError={actionError}
                onClose={closeModal}
                onSubmit={handleSave}
            />
        ) : null}

        {deleteTarget ? (
            <DeleteModal contact={deleteTarget} deleting={saving} error={actionError} onCancel={() => setDeleteTarget(null)} onConfirm={handleDelete} />
        ) : null}
      </main>
  );
};

export default Contacts;