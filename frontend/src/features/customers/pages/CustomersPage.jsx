import { useCallback, useEffect, useState } from 'react';
import { CustomersFiltersBar } from '@/features/customers/components/CustomersFiltersBar.jsx';
import { CustomersTable } from '@/features/customers/components/CustomersTable.jsx';
import { RegisterCustomerPopup } from '@/features/customers/components/RegisterCustomerPopup.jsx';
import { SuccessModal } from '@/shared/components/ui/SuccessModal.jsx';
import { getClients, createClient } from '../services/customerService.js';
import { getAuthToken } from '@/features/auth/utils/authStorage.js';

export default function CustomersPage() {
    const [searchQuery, setSearchQuery] = useState('');
    const [isRegisterOpen, setIsRegisterOpen] = useState(false);
    const [editingCustomer, setEditingCustomer] = useState(null);
    const [customers, setCustomers] = useState([]);
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [currentPage, setCurrentPage] = useState(0);
    const [pagination, setPagination] = useState({
        totalPages: 0,
        totalElements: 0,
        pageSize: 10,
    });

    const fetchClients = useCallback(async (page = 0, search = '') => {
        const token = getAuthToken();
        if (!token) return;
        setIsLoading(true);
        try {
            const response = await getClients({
                page,
                size: 10,
                name: search || undefined,
            });
            
            const content = response?.content || [];
            setCustomers(content.map(c => ({
                id: c.id,
                nombre: c.name,
                email: c.email,
                telefono: c.phone,
                ultimaCompra: randomDate(),
                joined: c.isFrequentClient ?? c.isFrequent ?? false,
            })));
            
            setPagination({
                totalPages: response?.totalPages ?? 0,
                totalElements: response?.totalElements ?? content.length,
                pageSize: response?.size ?? 10,
            });
        } catch (e) {
            console.error('Error fetching customers:', e);
            setCustomers([]);
        } finally {
            setIsLoading(false);
        }
    }, []);

    // Reset to page 0 when search changes
    useEffect(() => {
        setCurrentPage(0);
    }, [searchQuery]);

    useEffect(() => {
        fetchClients(currentPage, searchQuery);
    }, [fetchClients, currentPage, searchQuery]);

    function randomDate() {
        const start = new Date(2024, 0, 1);
        const end = new Date();
        const date = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
        return date.toISOString().slice(0, 10);
    }

    const openRegister = () => {
        setEditingCustomer(null);
        setIsRegisterOpen(true);
    };

    const openEdit = (customer) => {
        setEditingCustomer(customer);
        setIsRegisterOpen(true);
    };

    const closePopup = () => {
        setEditingCustomer(null);
        setIsRegisterOpen(false);
    };

    const handleSave = async (payload) => {
        try {
            if (!payload.id) {
                const body = {
                    name: payload.nombre.trim(),
                    email: payload.email.trim(),
                    phone: payload.telefono.trim(),
                    isFrequentClient: false,
                };
                await createClient(body);
                await fetchClients(currentPage, searchQuery);
                setShowSuccessModal(true);
            } else {
                setCustomers(prev => prev.map(c => c.id === payload.id ? { ...c, ...payload } : c));
            }
            closePopup();
        } catch (error) {
            console.error('Error creating customer:', error);
        }
    };
    const handleDelete = (id) => {
        setCustomers(prev => prev.filter(c => c.id !== id));
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
                        ...customers.map(c => [c.nombre, c.email, c.telefono, c.ultimaCompra ?? '']),
                    ];
                    const csv = rows.map(r => r.map(v => `"${String(v).replace(/"/g, '""')}"`).join(',')).join('\n');
                    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
                    const url = URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.href = url;
                    a.download = `customers-${new Date().toISOString().slice(0, 10)}.csv`;
                    a.click();
                    URL.revokeObjectURL(url);
                }}
            />
            <CustomersTable
                customers={customers}
                searchQuery={searchQuery}
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
                    <div className="bg-white rounded-xl shadow-xl max-w-md w-full">
                        <SuccessModal
                            title="Cliente registrado"
                            description="El cliente fue registrado correctamente."
                            primaryButtonText="Registrar otro"
                            secondaryButtonText="Cerrar"
                            onPrimaryClick={() => {
                                setShowSuccessModal(false);
                                setIsRegisterOpen(true);
                            }}
                            onSecondaryClick={() => setShowSuccessModal(false)}
                        />
                    </div>
                </div>
            )}
        </div>
    );
}