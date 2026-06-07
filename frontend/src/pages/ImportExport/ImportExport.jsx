import { useState } from 'react';
import { createContact, createContactDetail } from '../../api/contactApi';
import { useContacts } from '../../hooks/useContacts';
import styles from './ImportExport.module.css';

const headers = ['firstName', 'lastName', 'jobTitle', 'category', 'email', 'phone'];

const escapeCsv = (value) => `"${String(value || '').replace(/"/g, '""')}"`;

const parseCsvLine = (line) => {
  const values = [];
  let current = '';
  let quoted = false;

  for (let index = 0; index < line.length; index += 1) {
    const char = line[index];
    if (char === '"' && line[index + 1] === '"') {
      current += '"';
      index += 1;
    } else if (char === '"') {
      quoted = !quoted;
    } else if (char === ',' && !quoted) {
      values.push(current);
      current = '';
    } else {
      current += char;
    }
  }

  values.push(current);
  return values;
};

const ImportExport = () => {
  const { contacts, loading, error, reload } = useContacts();
  const [message, setMessage] = useState('');
  const [importing, setImporting] = useState(false);

  const handleExport = () => {
    const rows = contacts.map((contact) =>
      headers.map((header) => escapeCsv(header === 'email' ? contact.email : header === 'phone' ? contact.phone : contact[header])).join(','),
    );
    const csv = [headers.join(','), ...rows].join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = 'contacts.csv';
    link.click();
    URL.revokeObjectURL(url);
    setMessage('Contacts exported successfully.');
  };

  const handleImport = async (event) => {
    const file = event.target.files?.[0];
    if (!file) return;

    setImporting(true);
    setMessage('');
    try {
      const text = await file.text();
      const [headerLine, ...lines] = text.split(/\r?\n/).filter(Boolean);
      const fileHeaders = parseCsvLine(headerLine);
      let count = 0;

      for (const line of lines) {
        const values = parseCsvLine(line);
        const row = Object.fromEntries(fileHeaders.map((header, index) => [header, values[index] || '']));
        if (!row.firstName?.trim() && !row.lastName?.trim()) continue;

        const created = await createContact({
          firstName: row.firstName || row.fullName || 'Imported',
          lastName: row.lastName || '',
          jobTitle: row.jobTitle || '',
          category: row.category || 'Personal',
        });
        const id = created.id || created.contactId || created.data?.id;
        if (id && row.email) await createContactDetail(id, { type: 'EMAIL', label: 'Imported Email', value: row.email });
        if (id && row.phone) await createContactDetail(id, { type: 'PHONE', label: 'Imported Phone', value: row.phone });
        count += 1;
      }

      await reload();
      setMessage(`${count} contacts imported successfully.`);
    } catch (apiError) {
      setMessage(apiError.message);
    } finally {
      setImporting(false);
      event.target.value = '';
    }
  };

  return (
    <main className="page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Import / Export</h1>
          <p className="page-subtitle">Move contact data in and out using CSV files.</p>
        </div>
      </div>

      {error ? <div className="error-state">{error}</div> : null}
      {message ? <div className={message.toLowerCase().includes('success') ? 'empty-state' : 'error-state'}>{message}</div> : null}

      <section className={styles.grid}>
        <article className={`card ${styles.panel}`}>
          <h2>Export Contacts</h2>
          <p>Download all contacts currently available from the backend as a CSV file with name, job title, category, email, and phone columns.</p>
          <div className={styles.actions}>
            <button className="btn btn-primary" type="button" onClick={handleExport} disabled={loading || contacts.length === 0}>
              {loading ? 'Loading...' : 'Export CSV'}
            </button>
          </div>
        </article>
        <article className={`card ${styles.panel}`}>
          <h2>Import Contacts</h2>
          <p>Upload a CSV with columns: firstName, lastName, jobTitle, category, email, phone. Imported rows are created through the backend APIs.</p>
          <input className={styles.file} type="file" accept=".csv,text/csv" onChange={handleImport} disabled={importing} />
          <div className={styles.actions}>
            <span>{importing ? 'Importing contacts...' : 'Ready for CSV upload.'}</span>
          </div>
        </article>
      </section>
    </main>
  );
};

export default ImportExport;
