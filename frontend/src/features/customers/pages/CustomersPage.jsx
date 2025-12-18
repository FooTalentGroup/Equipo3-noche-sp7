import { useCallback, useEffect, useState } from 'react';
import { CustomersFiltersBar } from '@/features/customers/components/CustomersFiltersBar.jsx';
import { CustomersTable } from '@/features/customers/components/CustomersTable.jsx';
import RegisterCustomerPopup from '../components/RegisterCustomerPopup.jsx';
import { getCustomers, createCustomer, updateCustomer, deleteCustomer } from '../services/customerService.js';
import { getAuthToken } from '@/features/auth/utils/authStorage.js';
import { useCustomersFilter } from '../hooks/useCustomersFilter';
import { ConfirmDialog } from '@/features/products/components/ConfirmDialog';

const PAGE_SIZE = 10;

export default function CustomersPage() {
    const { searchQuery, debouncedSearch, setSearchQuery } = useCustomersFilter(500);
    const [isRegisterOpen, setIsRegisterOpen] = useState(false);
    const [editingCustomer, setEditingCustomer] = useState(null);
    const [customers, setCustomers] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [currentPage, setCurrentPage] = useState(0);
    const [pagination, setPagination] = useState({ totalPages: 0, totalElements: 0, pageSize: PAGE_SIZE });
    const [isConfirmOpen, setIsConfirmOpen] = useState(false);
    const [toDeleteId, setToDeleteId] = useState(null);

    const fetchCustomers = useCallback(async (page = 0, name = '') => {
        const token = getAuthToken();
        if (!token) {
            console.warn('[CustomersPage] No auth token found; attempting to fetch customers anyway.');
        }
        setIsLoading(true);
        try {
            const data = await getCustomers({ page, size: PAGE_SIZE, name });

            console.debug('[CustomersPage] getCustomers response:', data);


            const payload = data?.data ?? data;

            const customersArray = Array.isArray(payload?.customers)
                ? payload.customers
                : Array.isArray(payload?.customers?.content)
                    ? payload.customers.content
                    : Array.isArray(payload?.content)
                        ? payload.content
                        : Array.isArray(payload)
                            ? payload
                            : [];

            const mappedAll = customersArray.map((u) => ({
                raw: u,
                id: u.id,
                name: u.name ?? u.nombre ?? '',
                email: u.email ?? '',
                phone: u.phone ?? u.telefono ?? '',
                isFrequent: u.isFrequent ?? u.esFrecuente ?? false,
                clientStatus: String(u.clientStatus ?? u.status ?? 'ACTIVE').trim().toUpperCase(),
            }));

            const activeClients = mappedAll.filter((c) => c.clientStatus === 'ACTIVE');

            const mapped = activeClients.map((c) => ({
                id: c.id,
                nombre: c.name,
                email: c.email,
                telefono: c.phone,
                esFrecuente: c.isFrequent
            }));

            setCustomers(mapped);
            setPagination({
                totalPages: payload?.totalPages ?? payload?.page?.totalPages ?? payload?.customers?.totalPages ?? 1,
                totalElements: payload?.totalElements ?? payload?.page?.totalElements ?? (activeClients.length ?? 0),
                pageSize: payload?.pageSize ?? PAGE_SIZE
            });
        } catch (err) {
            console.error('[CustomersPage] fetchCustomers error:', err);
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
                closePopup();
            } else {
                await createCustomer({
                    name: payload.nombre.trim(),
                    email: payload.email.trim(),
                    phone: payload.telefono.trim(),
                    isFrequent: payload.esFrecuente
                });
            }

            await fetchCustomers(0, debouncedSearch);
        } catch (error) {
            const detailMsg = error?.data?.details?.[0] || error?.response?.data?.details?.[0] || error?.message || String(error);
            console.error('Error saving customer:', detailMsg);

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

            throw error;
        }
    };

    const handleDelete = (id) => {
        setToDeleteId(id);
        setIsConfirmOpen(true);
    };

    const confirmDelete = async () => {
        if (!toDeleteId) return;
        try {
            await deleteCustomer(toDeleteId);
            await fetchCustomers(currentPage, debouncedSearch);
        } catch (err) {
            console.error('Error deleting customer', err);
        } finally {
            setIsConfirmOpen(false);
            setToDeleteId(null);
        }
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
            <ConfirmDialog
                isOpen={isConfirmOpen}
                handleOpenChange={setIsConfirmOpen}
                dialogTitle="¿Desea eliminar el cliente?"
                dialogDescription="Esta acción eliminará al cliente de forma permanente."
                cancelTitle="Cancelar"
                acceptTitle="Sí, eliminar"
                onAccept={confirmDelete}
                variant="destructive"
            />
        </div>
    );
}