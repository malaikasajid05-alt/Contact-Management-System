export const normalizeContact = (contact = {}) => {
    const firstName = contact.firstName || contact.first_name || '';
    const lastName = contact.lastName || contact.last_name || '';
    const fullName = contact.fullName || contact.name || `${firstName} ${lastName}`.trim() || 'Unnamed Contact';
    const details = contact.details || contact.contactDetails || [];
    const emails = details.filter((detail) => (detail.type || '').toLowerCase() === 'email');
    const phones = details.filter((detail) => (detail.type || '').toLowerCase() === 'phone');

    const rawTitle =
        contact.jobTitle ||
        contact.job_title ||
        contact.title ||
        contact.position ||
        contact.role ||
        '';

    return {
        ...contact,
        id: contact.id || contact.contactId || contact._id,
        firstName: firstName || fullName.split(' ')[0] || '',
        lastName: lastName || fullName.split(' ').slice(1).join(' '),
        fullName,
        // Store as jobTitle internally; only show 'Contact' if truly nothing
        jobTitle: rawTitle.trim() || '',
        category: contact.category || contact.contactType || 'Personal',
        email: contact.email || emails[0]?.value || emails[0]?.email || '',
        phone: contact.phone || phones[0]?.value || phones[0]?.phone || '',
        details,
    };
};

export const splitDetails = (details = []) => ({
    emails: details
        .filter((detail) => (detail.type || detail.detailType || '').toLowerCase() === 'email')
        .map((detail) => ({
            id: detail.id || detail.detailId,
            label: detail.label || detail.name || 'Work',
            value: detail.value || detail.email || '',
            type: 'EMAIL',
        })),
    phones: details
        .filter((detail) => (detail.type || detail.detailType || '').toLowerCase() === 'phone')
        .map((detail) => ({
            id: detail.id || detail.detailId,
            label: detail.label || detail.name || 'Mobile',
            value: detail.value || detail.phone || '',
            type: 'PHONE',
        })),
});

export const buildContactPayload = (values) => {
    const titleValue = (values.title || values.jobTitle || '').trim();
    return {
        firstName: values.firstName.trim(),
        lastName: values.lastName.trim(),
        jobTitle: titleValue,
        title: titleValue,
    };
};

export const buildDetailPayload = (detail) => ({
    type: detail.type,
    label: detail.label,
    value: detail.value,
});

const WORK_LABELS = ['work', 'business'];

export const isWorkContact = (contact) => {
    const details = contact.details || [];
    const phones = details.filter((d) => (d.type || d.detailType || '').toLowerCase() === 'phone');
    const emails = details.filter((d) => (d.type || d.detailType || '').toLowerCase() === 'email');
    const checkLabels = (items) => items.some((item) => WORK_LABELS.includes((item.label || '').toLowerCase()));
    if (phones.length > 0) return checkLabels(phones);
    if (emails.length > 0) return checkLabels(emails);
    return false;
};

export const filterContacts = (contacts, query) => {
    const q = query.trim().toLowerCase();
    if (!q) return contacts;
    return contacts.filter((contact) =>
        `${contact.firstName} ${contact.lastName} ${contact.fullName} ${contact.email} ${contact.phone} ${contact.jobTitle}`
            .toLowerCase()
            .includes(q)
    );
};

export const sortContacts = (contacts, sortBy) => {
    const arr = [...contacts];
    switch (sortBy) {
        case 'nameAsc':
            return arr.sort((a, b) => String(a.fullName || '').localeCompare(String(b.fullName || '')));
        case 'nameDesc':
            return arr.sort((a, b) => String(b.fullName || '').localeCompare(String(a.fullName || '')));
        case 'emailAsc':
            return arr.sort((a, b) => String(a.email || '').localeCompare(String(b.email || '')));
        case 'emailDesc':
            return arr.sort((a, b) => String(b.email || '').localeCompare(String(a.email || '')));
        case 'titleAsc':
            return arr.sort((a, b) => String(a.jobTitle || '').localeCompare(String(b.jobTitle || '')));
        case 'titleDesc':
            return arr.sort((a, b) => String(b.jobTitle || '').localeCompare(String(a.jobTitle || '')));
        case 'recent':
        default:
            return arr.sort((a, b) => (b.id || 0) - (a.id || 0));
    }
};

export const paginate = (items, page, pageSize) => {
    const start = (page - 1) * pageSize;
    return items.slice(start, start + pageSize);
};