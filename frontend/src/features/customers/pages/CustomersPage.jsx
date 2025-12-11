// src/features/customers/pages/CustomersPage.jsx
import { useCallback, useEffect, useState } from 'react';
import { CustomersFiltersBar } from '@/features/customers/components/CustomersFiltersBar.jsx';
import { CustomersTable } from '@/features/customers/components/CustomersTable.jsx';
import RegisterCustomerPopup from '../components/RegisterCustomerPopup.jsx';
import { SuccessModal } from '@/shared/components/ui/SuccessModal.jsx';
import { getCustomers, createCustomer, updateCustomer } from '../services/customerService.js';
import { getAuthToken } from '@/features/auth/utils/authStorage.js';
import { useCustomersFilter } from '../hooks/useCustomersFilter';

const PAGE_SIZE = 10;

const mapCustomer = (c) => ({
    id: c.id,
    nombre: c.name,
    email: c.email,
    telefono: c.phone,
    esFrecuente: c.isFrequent ?? false
});

export default function CustomersPage() {
    const { searchQuery, debouncedSearch, setSearchQuery } = useCustomersFilter(500);
    const [isRegisterOpen, setIsRegisterOpen] = useState(false);
    const [editingCustomer, setEditingCustomer] = useState(null);
    const [customers, setCustomers] = useState([]);
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [currentPage, setCurrentPage] = useState(0);
    const [pagination, setPagination] = useState({ totalPages: 0, totalElements: 0, pageSize: PAGE_SIZE });

    const fetchCustomers = useCallback(async (page = 0, name = '') => {
        const token = getAuthToken();
        if (!token) return;
        setIsLoading(true);
        try {
            const data = await getCustomers({ page, size: PAGE_SIZE, name });
            console.log("Data from customer page:\n", data);

            const mapped = (data.customers.content || []).map(mapCustomer);

            console.log("Mapped:\n", customers);

            setCustomers(mapped);
            setPagination({
                totalPages: data.totalPages,
                totalElements: data.totalElements,
                pageSize: data.pageSize
            });
            console.log('Pagination set to:', { totalPages: data.totalPages, totalElements: data.totalElements, pageSize: data.pageSize });
        } catch {
            setCustomers([]);
        } finally {
            setIsLoading(false);
        }
    }, []);

    useEffect(() => {
        setCurrentPage(0);
    }, [debouncedSearch]);

    useEffect(() => {
        fetchCustomers(currentPage, debouncedSearch);
    }, [fetchCustomers, currentPage, debouncedSearch]);

    const openRegister = () => {
        setEditingCustomer(null);
        setIsRegisterOpen(true);
    };

    const openEdit = (c) => {
        setEditingCustomer({
            id: c.id,
            nombre: c.nombre,
            email: c.email,
            telefono: c.telefono,
            esFrecuente: c.isFrequent
        });
        setIsRegisterOpen(true);
    };

    const closePopup = () => {
        setIsRegisterOpen(false);
        setTimeout(() => setEditingCustomer(null), 200);
    };

    const handleSave = async (payload) => {
        try {
            if (payload.id) {
                await updateCustomer(payload.id, {
                    name: payload.nombre.trim(),
                    email: payload.email.trim(),
                    phone: payload.telefono.trim(),
                    isFrequent: payload.esFrecuente
                });
            } else {
                await createCustomer({
                    name: payload.nombre.trim(),
                    email: payload.email.trim(),
                    phone: payload.telefono.trim(),
                    isFrequent: payload.esFrecuente
                });
                setShowSuccessModal(true);
            }
            closePopup();
            await fetchCustomers(0, debouncedSearch);
        } catch (error) {
            console.error('Error saving customer:', error);
            throw error;
        }
    };

    const handleDelete = (id) => {
        setCustomers(prev => prev.filter(c => c.id !== id));
    };

    const handleExport = () => {
        const rows = [
            ['Tipo', 'Nombre', 'Email', 'Telefono'],
            ...customers.map(c => [c.isFrequent, c.nombre, c.email, c.telefono])
        ];
        const csv = rows.map(r => r.map(v => `"${String(v).replace(/"/g, '""')}"`).join(',')).join('\n');
        const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `clientes-${new Date().toISOString().slice(0, 10)}.csv`;
        a.click();
        URL.revokeObjectURL(url);
    };

    console.log("Mapped:\n", customers);
    return (
        <div className="p-6 w-full">
            <CustomersFiltersBar
                searchQuery={searchQuery}
                onSearchChange={setSearchQuery}
                onRegister={openRegister}
                onPrint={() => window.print()}
                onExport={handleExport}
            />
            <CustomersTable
                customers={customers}
                onEdit={openEdit}
                onDelete={handleDelete}
                isLoading={isLoading}
                currentPage={currentPage}
                totalPages={pagination.totalPages}
                onPageChange={setCurrentPage}
                searchQuery={searchQuery}
            />
            <RegisterCustomerPopup
                open={isRegisterOpen}
                onClose={closePopup}
                onSave={handleSave}
                initialData={editingCustomer}
            />
        </div>
    );
}