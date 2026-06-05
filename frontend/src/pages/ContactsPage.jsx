import { useState, useEffect, useMemo } from 'react';
import { contactsApi } from '../services/api';
import { CreateContactModal, UpdateContactModal, DeleteContactModal } from '../ContactModals';

const PAGE_SIZE = 7;

const getInitials = (first, last) => {
    return ((first?.[0] || '') + (last?.[0] || '')).toUpperCase() || '?';
};

const AVATAR_COLORS = ['#6366f1','#8b5cf6','#ec4899','#f97316','#14b8a6','#0ea5e9','#22c55e','#eab308'];
const avatarColor = (name) => AVATAR_COLORS[(name?.charCodeAt(0) || 0) % AVATAR_COLORS.length];

export default function ContactsPage() {
    const [contacts, setContacts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState('');
    const [page, setPage] = useState(1);
    const [activeFilter, setActiveFilter] = useState('All');
    const [modal, setModal] = useState(null); // { type: 'create'|'update'|'delete', contact? }

    const load = async () => {
        setLoading(true);
        try {
            const data = await contactsApi.getAll();
            setContacts(data || []);
        } catch (e) {
            console.error(e);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { load(); }, []);

    const filtered = useMemo(() => {
        return contacts.filter(c => {
            const q = search.toLowerCase();
            return (
                c.firstName?.toLowerCase().includes(q) ||
                c.lastName?.toLowerCase().includes(q) ||
                c.title?.toLowerCase().includes(q) ||
                (c.details || []).some(d => d.value?.toLowerCase().includes(q))
            );
        });
    }, [contacts, search]);

    const totalPages = Math.max(1, Math.ceil(filtered.length / PAGE_SIZE));
    const paginated = filtered.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

    const handleCreate = async (data) => {
        const created = await contactsApi.create(data);
        setContacts(prev => [...prev, created]);
    };

    const handleUpdate = async (id, data) => {
        const updated = await contactsApi.update(id, data);
        setContacts(prev => prev.map(c => c.id === id ? updated : c));
    };

    const handleDelete = async (id) => {
        await contactsApi.delete(id);
        setContacts(prev => prev.filter(c => c.id !== id));
    };

    const getEmail = (contact) => (contact.details || []).find(d => d.type === 'EMAIL')?.value || '-';
    const getPhone = (contact) => (contact.details || []).find(d => d.type === 'PHONE')?.value || '-';

    return (
        <div className="contacts-page">
            <div className="contacts-header">
                <div>
                    <h2>All Contacts</h2>
                    <p className="contacts-count">Total {filtered.length} contacts</p>
                </div>
                <button className="btn-primary btn-add" onClick={() => setModal({ type: 'create' })}>
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
                    Add Contact
                </button>
            </div>

            <div className="contacts-toolbar">
                <div className="filter-tabs">
                    {['All', 'Work', 'Personal', 'Family', 'Friends'].map(f => (
                        <button key={f} className={`filter-tab${activeFilter === f ? ' active' : ''}`} onClick={() => setActiveFilter(f)}>{f}</button>
                    ))}
                </div>
                <button className="btn-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><line x1="4" y1="6" x2="20" y2="6"/><line x1="8" y1="12" x2="20" y2="12"/><line x1="12" y1="18" x2="20" y2="18"/></svg>
                    Filters
                </button>
            </div>

            {loading ? (
                <div className="loading-state">
                    <div className="spinner spinner--lg" />
                    <p>Loading contacts...</p>
                </div>
            ) : (
                <>
                    <div className="contacts-table-wrap">
                        <table className="contacts-table">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Contact</th>
                                <th>Title</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {paginated.length === 0 ? (
                                <tr><td colSpan={6} className="empty-row">No contacts found{search ? ` for "${search}"` : ''}.</td></tr>
                            ) : paginated.map((c, i) => (
                                <tr key={c.id} className="contact-row">
                                    <td className="row-num">{(page - 1) * PAGE_SIZE + i + 1}</td>
                                    <td>
                                        <div className="contact-cell">
                                            <div className="avatar" style={{ background: avatarColor(c.firstName) }}>
                                                {getInitials(c.firstName, c.lastName)}
                                            </div>
                                            <span className="contact-name">{c.firstName} {c.lastName}</span>
                                        </div>
                                    </td>
                                    <td className="cell-muted">{c.title || '-'}</td>
                                    <td className="cell-muted">{getEmail(c)}</td>
                                    <td className="cell-muted">{getPhone(c)}</td>
                                    <td>
                                        <div className="action-btns">
                                            <button className="action-btn action-btn--edit" title="Edit" onClick={() => setModal({ type: 'update', contact: c })}>
                                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                                            </button>
                                            <button className="action-btn action-btn--delete" title="Delete" onClick={() => setModal({ type: 'delete', contact: c })}>
                                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6"/><line x1="10" y1="11" x2="10" y2="17"/><line x1="14" y1="11" x2="14" y2="17"/></svg>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>

                    <div className="pagination">
                        <span className="pagination__info">Showing {Math.min((page-1)*PAGE_SIZE+1, filtered.length)} to {Math.min(page*PAGE_SIZE, filtered.length)} of {filtered.length} contacts</span>
                        <div className="pagination__controls">
                            <button className="page-btn" onClick={() => setPage(p => Math.max(1, p-1))} disabled={page === 1}>‹</button>
                            {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                                let p = i + 1;
                                if (totalPages > 5) {
                                    if (page > 3) p = page - 2 + i;
                                    if (p > totalPages) p = totalPages - (4 - i);
                                }
                                return (
                                    <button key={p} className={`page-btn${page === p ? ' active' : ''}`} onClick={() => setPage(p)}>{p}</button>
                                );
                            })}
                            {totalPages > 5 && <span className="page-ellipsis">...</span>}
                            {totalPages > 5 && <button className={`page-btn${page === totalPages ? ' active' : ''}`} onClick={() => setPage(totalPages)}>{totalPages}</button>}
                            <button className="page-btn" onClick={() => setPage(p => Math.min(totalPages, p+1))} disabled={page === totalPages}>›</button>
                        </div>
                    </div>
                </>
            )}

            {modal?.type === 'create' && <CreateContactModal onClose={() => setModal(null)} onCreate={handleCreate} />}
            {modal?.type === 'update' && <UpdateContactModal contact={modal.contact} onClose={() => setModal(null)} onUpdate={handleUpdate} />}
            {modal?.type === 'delete' && <DeleteContactModal contact={modal.contact} onClose={() => setModal(null)} onDelete={handleDelete} />}
        </div>
    );
}
