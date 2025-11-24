import { useState } from 'react';
import { CustomersFiltersBar } from '@/features/customers/components/CustomersFiltersBar.jsx';
import { CustomersTable } from '@/features/customers/components/CustomersTable.jsx';
import { RegisterCustomerPopup } from '@/features/customers/components/RegisterCustomerPopup.jsx';

export default function CustomersPage() {
    const [searchQuery, setSearchQuery] = useState('');
    const [isRegisterOpen, setIsRegisterOpen] = useState(false);
    const [editingCustomer, setEditingCustomer] = useState(null);

    // Initial dummy (domi) data
    const [customers, setCustomers] = useState([
        { id: 1, nombre: 'Ana Pérez', email: 'ana.perez@example.com', telefono: '+5491122334455', ultimaCompra: '2025-10-02', joined: true },
        { id: 2, nombre: 'Luis Gómez', email: 'luis.gomez@example.com', telefono: '+5491166677788', ultimaCompra: '2025-09-18', joined: false },
        { id: 3, nombre: 'María Ruiz', email: 'maria.ruiz@example.com', telefono: '+5491177776666', ultimaCompra: '2025-11-01', joined: true },
    ]);

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

    const handleSave = (payload) => {
        if (payload.id) {
            // update
            setCustomers(prev => prev.map(c => (c.id === payload.id ? { ...c, ...payload } : c)));
        } else {
            // new
            const nextId = Math.max(0, ...customers.map(c => c.id)) + 1;
            setCustomers(prev => [{ id: nextId, ...payload }, ...prev]);
        }
        closePopup();
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
                    // simple CSV export
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