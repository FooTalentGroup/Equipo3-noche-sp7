import { useState } from 'react';

export function useProductsFilter(initial = {}) {
  const [searchQuery, setSearchQuery] = useState(initial.searchQuery || '');
  const [filters, setFilters] = useState({
    category: initial.category || 'all',
    minPrice: initial.minPrice ?? '',
    maxPrice: initial.maxPrice ?? '',
    inStock: initial.inStock ?? false,
  });
  const [sort, setSort] = useState(initial.sort || 'name_asc');
  const [isFiltersOpen, setIsFiltersOpen] = useState(false);

  function openFilters() { setIsFiltersOpen(true); }
  function closeFilters() { setIsFiltersOpen(false); }
  function applyFilters() {
    // currently state is already applied; placeholder for analytics or syncing to URL
  }
  function clearFilters() {
    setFilters({
      category: 'all',
      minPrice: '',
      maxPrice: '',
      inStock: false,
    });
    setSort('name_asc');
  }

  return {
    searchQuery,
    setSearchQuery,
    filters,
    setFilters,
    sort,
    setSort,
    isFiltersOpen,
    openFilters,
    closeFilters,
    applyFilters,
    clearFilters,
  };
}