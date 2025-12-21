import { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { getInventoryMovements } from '../services/inventoryService.js';
import { MovementFiltersPopup } from '../components/MovementFiltersPopup.jsx';
import { MovementHistoryFiltersBar } from '../components/MovementHistoryFiltersBar.jsx';
import { MovementHistoryTable } from '../components/MovementHistoryTable.jsx';
import { useMovementsFilter } from '../hooks/useMovementsFilter.jsx';
import { getAuthToken } from '@/features/auth/utils/authStorage.js';

const PAGE_SIZE = 10;

export default function MovementHistoryPage() {
    const { productId } = useParams();
    const { searchQuery, debouncedSearch, setSearchQuery } = useMovementsFilter(500);
    const [movements, setMovements] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(0);
    const [isFiltersOpen, setIsFiltersOpen] = useState(false);
    const [filters, setFilters] = useState({
        startDate: '',
        endDate: '',
        userName: '',
        movementType: 'all',
    });
    const [pagination, setPagination] = useState({
        totalPages: 0,
        totalElements: 0,
        pageSize: PAGE_SIZE
    });

    const fetchMovements = useCallback(async (page = 0, search = '', currentFilters = filters) => {
        const token = getAuthToken();
        if (!token) return;

        setIsLoading(true);
        try {
            const params = {
                page,
                size: PAGE_SIZE,
                userName: currentFilters.userName,
                movementType: currentFilters.movementType,
                startDate: currentFilters.startDate,
                endDate: currentFilters.endDate,
            };

            // Only send productId OR productName, not both
            if (productId) {
                params.productId = productId;
            } else if (search) {
                params.productName = search;
            }

            const data = await getInventoryMovements(params);

            const movementsArray = Array.isArray(data.movements) ? data.movements : [];
            setMovements(movementsArray);
            setPagination({
                totalPages: data.totalPages || 0,
                totalElements: data.totalElements || 0,
                pageSize: data.pageSize || PAGE_SIZE
            });
        } catch (error) {
            console.error('Error fetching movements:', error);
            setMovements([]);
            setPagination({ totalPages: 0, totalElements: 0, pageSize: PAGE_SIZE });
        } finally {
            setIsLoading(false);
        }
    }, [filters, productId]);

    // Reset page when search or filters change
    useEffect(() => {
        setCurrentPage(0);
    }, [debouncedSearch, filters]);

    // Fetch movements when page, search, or filters change
    useEffect(() => {
        fetchMovements(currentPage, debouncedSearch, filters);
    }, [fetchMovements, currentPage, debouncedSearch, filters]);

    const formatDate = (dateString) => {
        if (!dateString) return '—';
        const date = new Date(dateString);
        return date.toLocaleString('es-ES', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
        });
    };

    const handleExport = () => {
        const rows = [
            ['Producto', 'Tipo', 'Motivo', 'Cantidad', 'Fecha/Hora', 'Usuario'],
            ...movements.map(m => [
                m.productName || '',
                m.movementType === 'IN' ? 'Entrada' : 'Salida',
                m.reason || '',
                m.quantity,
                formatDate(m.createdAt),
                m.userName || ''
            ])
        ];
        const csv = rows.map(r => r.map(v => `"${String(v).replace(/"/g, '""')}"`).join(',')).join('\n');
        const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `movimientos-inventario-${new Date().toISOString().slice(0, 10)}.csv`;
        a.click();
        URL.revokeObjectURL(url);
    };

    const activeFiltersCount = [
        filters.startDate,
        filters.endDate,
        filters.userName,
        filters.movementType !== 'all'
    ].filter(Boolean).length;

    return (
        <div className="p-6 w-full">
            <MovementHistoryFiltersBar
                searchQuery={searchQuery}
                onSearchChange={setSearchQuery}
                onFilterClick={() => setIsFiltersOpen(true)}
                onExport={handleExport}
                activeFiltersCount={activeFiltersCount}
            />

            <MovementHistoryTable
                movements={movements}
                isLoading={isLoading}
                currentPage={currentPage}
                totalPages={pagination.totalPages}
                totalElements={pagination.totalElements}
                onPageChange={setCurrentPage}
            />

            <MovementFiltersPopup
                open={isFiltersOpen}
                onClose={() => setIsFiltersOpen(false)}
                filters={filters}
                onApplyFilters={setFilters}
            />
        </div>
    );
}