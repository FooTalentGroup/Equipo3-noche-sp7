import { Edit, Trash2, ChevronLeft, ChevronRight, LoaderCircle } from 'lucide-react';

export function CustomersTable({
    customers = [],
    onEdit,
    onDelete,
    isLoading = false,
    currentPage = 0,
    totalPages = 0,
    onPageChange,
}) {
    const handlePrev = () => {
        if (currentPage === 0) return;
        onPageChange?.(currentPage - 1);
    };

    const handleNext = () => {
        if (currentPage >= totalPages - 1) return;
        onPageChange?.(currentPage + 1);
    };

    if (isLoading) {
        return (
            <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden max-w-[1086px] max-h-[673px] flex items-center justify-center">
                <div className="flex flex-col items-center justify-center gap-3 my-4">
                    <LoaderCircle className="h-8 w-8 text-slate-600 animate-spin" />
                    <span className="text-sm text-gray-600">Cargando lista de clientes...</span>
                </div>
            </div>
        );
    }

    return (
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden max-w-[1086px] max-h-[673px]">
            <div className="overflow-x-auto">
                <table className="w-full text-sm">
                    <thead className="text-[14px] bg-slate-200 text-[#404040] font-semibold h-[46px]">
                        <tr>
                            <th className="px-6 py-3 text-left text-gray-700">Nombre</th>
                            <th className="px-6 py-3 text-left text-gray-700">Email</th>
                            <th className="px-6 py-3 text-left text-gray-700">Numero telefónico</th>
                            <th className="px-6 py-3 text-left text-gray-700">Última compra</th>
                            <th className="px-6 py-3 text-center text-gray-700">Acción</th>
                        </tr>
                    </thead>

                    <tbody className="divide-y divide-gray-100">
                        {customers.map((c) => (
                            <tr key={c.id} className="hover:bg-gray-50 transition-colors">
                                <td className="px-6 py-4 font-normal text-[#171717] text-[14px]">{c.nombre}</td>
                                <td className="px-6 py-4 font-normal text-[#525252] text-[14px]">{c.email}</td>
                                <td className="px-6 py-4 font-normal text-[#525252] text-[14px]">{c.telefono}</td>
                                <td className="px-6 py-4 font-normal text-[#525252] text-[14px]">{c.ultimaCompra ?? '—'}</td>
                                <td className="px-6 py-4">
                                    <div className="flex items-center justify-center gap-4">
                                        <button onClick={() => onEdit?.(c)} className="text-gray-500 hover:text-blue-600 transition">
                                            <Edit className="h-4 w-4" />
                                        </button>
                                        <button onClick={() => onDelete?.(c.id)} className="text-red-600 hover:text-red-700 transition">
                                            <Trash2 className="h-4 w-4" />
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}

                        {customers.length === 0 && (
                            <tr>
                                <td colSpan={5} className="px-6 py-6 text-center text-gray-500 text-sm">
                                    No hay clientes para mostrar.
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            {totalPages > 1 && (
                <div className="flex items-center justify-center px-6 py-3 bg-gray-50 border-t">
                    <div className="flex items-center gap-2">
                        <button
                            onClick={handlePrev}
                            disabled={currentPage === 0}
                            className="border-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-200 rounded-md disabled:opacity-50 flex items-center gap-1"
                        >
                            <ChevronLeft className="w-4 h-4" /> Anterior
                        </button>

                        <div className="flex gap-1 justify-center">
                            {Array.from({ length: totalPages }, (_, i) => i).map((page) => (
                                <button
                                    key={page}
                                    onClick={() => onPageChange?.(page)}
                                    className={`px-3 py-1.5 text-sm rounded-md ${currentPage === page ? 'bg-slate-600 text-white' : 'text-gray-700 hover:bg-gray-200'}`}
                                >
                                    {page + 1}
                                </button>
                            ))}
                        </div>

                        <button
                            onClick={handleNext}
                            disabled={currentPage >= totalPages - 1}
                            className="border-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-200 rounded-md disabled:opacity-50 flex items-center gap-1"
                        >
                            Siguiente <ChevronRight className="w-4 h-4" />
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}