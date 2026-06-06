import Modal from '../Modal/Modal';

const DeleteModal = ({ contact, deleting, error, onCancel, onConfirm }) => (
    <Modal
        title="Delete Contact"
        onClose={onCancel}
        size="sm"
        footer={
            <>
                <button className="btn btn-secondary" type="button" onClick={onCancel}>
                    Cancel
                </button>
                <button className="btn btn-danger" type="button" onClick={onConfirm} disabled={deleting}>
                    {deleting ? 'Deleting...' : 'Delete'}
                </button>
            </>
        }
    >
        {error ? <div className="error-state">{error}</div> : null}
        <p>Are you sure you want to delete this contact?</p>
        <strong>{contact?.fullName}</strong>
    </Modal>
);

export default DeleteModal;