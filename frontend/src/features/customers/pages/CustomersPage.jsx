// src/features/customers/pages/CustomersPage.jsx
import { useCallback, useEffect, useState } from 'react';
import { CustomersFiltersBar } from '@/features/customers/components/CustomersFiltersBar.jsx';
import { CustomersTable } from '@/features/customers/components/CustomersTable.jsx';
import RegisterCustomerPopup from '../components/RegisterCustomerPopup.jsx';
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
    const [isLoading, setIsLoading] = useState(false);
    const [currentPage, setCurrentPage] = useState(0);
    const [pagination, setPagination] = useState({ totalPages: 0, totalElements: 0, pageSize: PAGE_SIZE });

    const fetchCustomers = useCallback(async (page = 0, name = '') => {
        const token = getAuthToken();
        if (!token) return;
        setIsLoading(true);
        try {
            const data = await getCustomers({ page, size: PAGE_SIZE, name });

            const mapped = (data.customers.content || []).map(mapCustomer);
            setCustomers(mapped);
            setPagination({
                totalPages: data.totalPages,
                totalElements: data.totalElements,
                pageSize: data.pageSize
            });
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
                // For edits close the popup so the popup can unmount
                closePopup();
            } else {
                await createCustomer({
                    name: payload.nombre.trim(),
                    email: payload.email.trim(),
                    phone: payload.telefono.trim(),
                    isFrequent: payload.esFrecuente
                });
                // For creation, do NOT close the popup here — the popup will show its SuccessModal
            }

            // Refresh customers after successful save
            await fetchCustomers(0, debouncedSearch);
        } catch (error) {
            // Prefer the backend details message from error.data.details[0] when available
            const detailMsg = error?.data?.details?.[0] || error?.response?.data?.details?.[0] || error?.message || String(error);
            console.error('Error saving customer:', detailMsg);

            // Map the detail message to a field-specific error using simple keyword checks
            const msgStr = String(detailMsg).toLowerCase();
            const fieldErrors = {};
            if (msgStr.includes('teléfono') || msgStr.includes('telefono') || msgStr.includes('phone')) {
                fieldErrors.telefono = detailMsg;
            }
            if (msgStr.includes('email') || msgStr.includes('correo') || msgStr.includes('e-mail')) {
                fieldErrors.email = detailMsg;
            }
            if (msgStr.includes('nombre') || msgStr.includes('name')) {
                fieldErrors.nombre = detailMsg;
            }

            if (Object.keys(fieldErrors).length > 0) {
                const customError = new Error(detailMsg);
                customError.response = { data: { fieldErrors } };
                throw customError;
            }

            // No field matched — rethrow original error so the popup can show a fallback message
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