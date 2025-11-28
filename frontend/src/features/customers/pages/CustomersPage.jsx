// src/features/customers/pages/CustomersPage.jsx
import { useCallback, useEffect, useState } from 'react';
import { CustomersFiltersBar } from '@/features/customers/components/CustomersFiltersBar.jsx';
import { CustomersTable } from '@/features/customers/components/CustomersTable.jsx';
import { RegisterCustomerPopup } from '@/features/customers/components/RegisterCustomerPopup.jsx';
import { SuccessModal } from '@/shared/components/ui/SuccessModal.jsx';
import { getCustomers, createCustomer, updateCustomer } from '../services/customerService.js';
import { getAuthToken } from '@/features/auth/utils/authStorage.js';

const PAGE_SIZE = 10;

const mapCustomer = (customer, fallbackDate) => ({
    id: customer.id,
    nombre: customer.name,
    email: customer.email,
    telefono: customer.phone,
    ultimaCompra: customer.lastPurchaseDate ?? fallbackDate(),
    joined: customer.isFrequent ?? false,
});

const randomDate = () => {
    const start = new Date(2024, 0, 1);
    const end = new Date();
    const date = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
    return date.toISOString().slice(0, 10);
};

export default function CustomersPage() {
    const [searchQuery, setSearchQuery] = useState('');
    const [isRegisterOpen, setIsRegisterOpen] = useState(false);
    const [editingCustomer, setEditingCustomer] = useState(null); // Will hold mapped data
    const [customers, setCustomers] = useState([]);
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [currentPage, setCurrentPage] = useState(0);
    const [pagination, setPagination] = useState({
        totalPages: 0,
        totalElements: 0,
        pageSize: PAGE_SIZE,
    });

    const fetchCustomers = useCallback(async (page = 0, query = '') => {
        const token = getAuthToken();
        if (!token) {
            console.warn('Token missing. Skipping customers fetch.');
            return;
        }

        setIsLoading(true);
        try {
            const data = await getCustomers({ page, size: PAGE_SIZE, search: query });
            const mapped = (data?.customers ?? []).map((c) => mapCustomer(c, randomDate));
            setCustomers(mapped);
            setPagination({
                totalPages: data?.totalPages ?? 0,
                totalElements: data?.totalElements ?? mapped.length,
                pageSize: data?.pageSize ?? PAGE_SIZE,
            });
        } catch (error) {
            console.error('Error fetching customers:', error);
            setCustomers([]);
        } finally {
            setIsLoading(false);
        }
    }, []);

    // Reset page when searching
    useEffect(() => {
        setCurrentPage(0);
    }, [searchQuery]);

    // Fetch on page/query change
    useEffect(() => {
        fetchCustomers(currentPage, searchQuery);
    }, [fetchCustomers, currentPage, searchQuery]);

    // Open popup for new customer
    const openRegister = () => {
        setEditingCustomer(null);
        setIsRegisterOpen(true);
    };

    // Open popup for editing — FIX: pass correctly shaped data + open immediately
    const openEdit = (customer) => {
        setEditingCustomer({
            id: customer.id,
            nombre: customer.nombre || '',
            email: customer.email || '',
            telefono: customer.telefono || '',
            joined: customer.joined ?? false,
        });
        setIsRegisterOpen(true); // Open immediately — no race condition
    };

    // Close popup
    const closePopup = () => {
        setIsRegisterOpen(false);
        // Small delay to prevent flash when closing
        setTimeout(() => setEditingCustomer(null), 300);
    };

    const handleSave = async (payload) => {
        try {
            if (payload.id) {
                // Update existing
                const updatePayload = {
                    name: payload.nombre?.trim() || undefined,
                    email: payload.email?.trim() || undefined,
                    phone: payload.telefono?.trim() || undefined,
                    isFrequent: payload.joined,
                };

                await updateCustomer(payload.id, updatePayload);
                await fetchCustomers(currentPage, searchQuery);
            } else {
                // Create new
                const createPayload = {
                    name: payload.nombre.trim(),
                    email: payload.email.trim(),
                    phone: payload.telefono.trim(),
                    isFrequent: payload.joined ?? false,
                };

                await createCustomer(createPayload);
                setShowSuccessModal(true);
                await fetchCustomers(0, searchQuery);
            }
            closePopup();
        } catch (error) {
            console.error('Error saving customer:', error);
        }
    };

    const handleDelete = (id) => {
        setCustomers((prev) => prev.filter((c) => c.id !== id));
    };

    return (
        <div className="p-6 w-full">
            <CustomersFiltersBar
                searchQuery={searchQuery}
                onSearchChange={setSearchQuery}
                onRegister={openRegister}
                onPrint={() => window.print()}
                onExport={() => {
                    const rows = [
                        ['Nombre', 'Email', 'Telefono', 'Ultima compra'],
                        ...customers.map((c) => [c.nombre, c.email, c.telefono, c.ultimaCompra ?? '']),
                    ];
                    const csv = rows
                        .map((r) => r.map((v) => `"${String(v).replace(/"/g, '""')}"`).join(','))
                        .join('\n');
                    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
                    const url = URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.href = url;
                    a.download = `clientes-${new Date().toISOString().slice(0, 10)}.csv`;
                    a.click();
                    URL.revokeObjectURL(url);
                }}
            />

            <CustomersTable
                customers={customers}
                onEdit={openEdit}
                onDelete={handleDelete}
                isLoading={isLoading}
                currentPage={currentPage}
                totalPages={pagination.totalPages}
                onPageChange={setCurrentPage}
            />

            <RegisterCustomerPopup
                open={isRegisterOpen}
                onClose={closePopup}
                onSave={handleSave}
                initialData={editingCustomer}
            />

            {showSuccessModal && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
                    <div className="bg-white rounded-xl shadow-xl max-w-md w-full p-6">
                        <SuccessModal
                            title="Cliente registrado"
                            description="El cliente fue registrado correctamente."
                            primaryButtonText="Registrar otro"
                            secondaryButtonText="Cerrar"
                            onPrimaryClick={() => {
                                setShowSuccessModal(false);
                                openRegister();
                            }}
                            onSecondaryClick={() => setShowSuccessModal(false)}
                        />
                    </div>
                </div>
            )}
        </div>
    );
}