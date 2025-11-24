import { useMemo } from 'react';
import { Edit, Trash2 } from 'lucide-react';
import { Button } from '@/shared/components/ui/button';

export function CustomersTable({ customers = [], searchQuery = '', onEdit, onDelete }) {
    const filtered = useMemo(() => {
        const q = (searchQuery || '').trim().toLowerCase();
        if (!q) return customers;
        return customers.filter(c =>
            String(c.nombre || '').toLowerCase().includes(q) ||
            String(c.email || '').toLowerCase().includes(q) ||
            String(c.telefono || '').toLowerCase().includes(q)
        );
    }, [customers, searchQuery]);

    return (
        <div className="overflow-x-auto bg-white shadow-sm rounded-lg border border-gray-200">
            <table className="min-w-full text-sm">
                <thead className="text-[14px] bg-slate-200 text-[#404040] font-semibold">
                    <tr>
                        <th className="px-4 py-3 text-left font-semibold text-gray-700">Nombre</th>
                        <th className="px-4 py-3 text-left font-semibold text-gray-700">Email</th>
                        <th className="px-4 py-3 text-left font-semibold text-gray-700">Numero telefónico</th>
                        <th className="px-4 py-3 text-left font-semibold text-gray-700">Última compra</th>
                        <th className="px-4 py-3 text-left font-semibold text-gray-700">Accion</th>
                    </tr>
                </thead>

                <tbody className="divide-y divide-gray-100">
                    {filtered.map(c => (
                        <tr key={c.id} className="hover:bg-gray-50">
                            <td className="px-4 py-3 text-gray-900">{c.nombre}</td>
                            <td className="px-4 py-3 text-gray-700">{c.email}</td>
                            <td className="px-4 py-3 text-gray-700">{c.telefono}</td>
                            <td className="px-4 py-3 text-gray-700">{c.ultimaCompra ?? '—'}</td>
                            <td className="px-4 py-3">
                                <div className="flex items-center gap-2">
                                    <button className="h-8 w-8" onClick={() => onEdit && onEdit(c)}>
                                        <Edit className="h-4 w-4" />
                                    </button>
                                    <button className="h-8 w-8 text-red-600" onClick={() => onDelete && onDelete(c.id)}>
                                        <Trash2 className="h-4 w-4 text-red-600" />
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}

                    {filtered.length === 0 && (
                        <tr>
                            <td colSpan={5} className="px-4 py-6 text-center text-gray-500 text-sm">
                                No hay clientes para mostrar.
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
}