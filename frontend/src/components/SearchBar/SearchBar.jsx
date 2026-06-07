import { FiSearch } from 'react-icons/fi';
import styles from './SearchBar.module.css';

const SearchBar = ({ query, onQueryChange, sortBy, onSortChange }) => (
    <div className={styles.toolbar}>
        <div className={styles.search}>
            <FiSearch className={styles.icon} />
            <input
                className={`form-input ${styles.input}`}
                style={{ paddingLeft: '2.75rem' }}
                type="search"
                value={query}
                onChange={(event) => onQueryChange(event.target.value)}
                placeholder="Search contacts by name, email or phone..."
            />
        </div>
        <select className="form-input" value={sortBy} onChange={(event) => onSortChange(event.target.value)}>
            <option value="recent">Recently Added</option>
            <option value="nameAsc">Name: A → Z</option>
            <option value="nameDesc">Name: Z → A</option>
            <option value="emailAsc">Email: A → Z</option>
            <option value="emailDesc">Email: Z → A</option>
            <option value="titleAsc">Title: A → Z</option>
            <option value="titleDesc">Title: Z → A</option>
        </select>
    </div>
);

export default SearchBar;