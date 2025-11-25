import { useEffect, useState } from 'react';
import { CustomersFiltersBar } from '@/features/customers/components/CustomersFiltersBar.jsx';
import { CustomersTable } from '@/features/customers/components/CustomersTable.jsx';
import { RegisterCustomerPopup } from '@/features/customers/components/RegisterCustomerPopup.jsx';
import { getClients, createClient } from '../services/customerService.js';
import { getAuthToken } from '@/features/auth/utils/authStorage.js';

export default function CustomersPage() {
    const [searchQuery, setSearchQuery] = useState('');
    const [isRegisterOpen, setIsRegisterOpen] = useState(false);
    const [editingCustomer, setEditingCustomer] = useState(null);
    const [customers, setCustomers] = useState([]);

    useEffect(() => {
        async function fetchClients() {
            const token = getAuthToken();
            if (!token) return;
            try {
                const data = await getClients();
                // map API to table
                setCustomers((data || []).map(c => ({
                    id: c.id,
                    nombre: c.name,
                    email: c.email,
                    telefono: c.phone,
                    ultimaCompra: randomDate(),
                    joined: c.isFrequentClient ?? c.isFrequent ?? false
                })));
            } catch (e) {
                setCustomers([]);
            }
        }
        fetchClients();
    }, []);

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
                    isFrequentClient: true,
                };
                const newClient = await createClient(body);
                if (newClient) {
                    setCustomers(prev => [
                        {
                            id: newClient.id,
                            nombre: newClient.name,
                            email: newClient.email,
                            telefono: newClient.phone,
                            ultimaCompra: randomDate(),
                            joined: !!newClient.isFrequentClient,
                        },
                        ...prev,
                    ]);
                }
            } else {
                // local update for edits (backend endpoint pending)
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