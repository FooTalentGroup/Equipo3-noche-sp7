import { useEffect, useState } from 'react';

export function useCustomersFilter(delay = 500) {
  const [searchQuery, setSearchQuery] = useState('');
  const [debouncedSearch, setDebouncedSearch] = useState('');

  useEffect(() => {
    const id = setTimeout(() => {
      setDebouncedSearch(searchQuery.trim());
    }, delay);
    return () => clearTimeout(id);
  }, [searchQuery, delay]);

  return {
    searchQuery,
    setSearchQuery,
    debouncedSearch
  };
}