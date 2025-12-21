import { ChevronLeft, ChevronRight, LoaderCircle, MoveDown, MoveUp } from 'lucide-react';
import { useEffect, useState } from "react";

export function MovementHistoryTable({
    movements = [],
    isLoading = false,
    currentPage = 0,
    totalPages = 0,
    onPageChange,
    pageSize = 10,
}) {
    const isControlled = typeof onPageChange === 'function';
    const [internalPage, setInternalPage] = useState(currentPage || 0);

    useEffect(() => {
        const totalItems = movements?.length || 0;
        const calcTotal = totalItems === 0 ? 0 : Math.ceil(totalItems / pageSize);
        if (!isControlled) {
            if (calcTotal === 0) setInternalPage(0);
            else if (internalPage > calcTotal - 1) setInternalPage(calcTotal - 1);
        }
    }, [movements, pageSize, internalPage, isControlled]);

    const page = isControlled ? currentPage : internalPage;
    const totalItems = movements?.length || 0;
    const computedTotalPages = totalItems === 0 ? 0 : Math.ceil(totalItems / pageSize);
    const usedTotalPages = totalPages > 0 ? totalPages : computedTotalPages;

    const setPage = (p) => {
        if (isControlled) {
            onPageChange?.(p);
        } else {
            setInternalPage(p);
        }
    };

    const handlePrev = () => {
        if (page === 0) return;
        setPage(page - 1);
    };

    const handleNext = () => {
        if (page >= usedTotalPages - 1) return;
        setPage(page + 1);
    };

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

    const getMovementTypeBadge = (type) => {
        if (type === 'IN') {
            return (
                <span className="inline-flex items-center gap-11 px-3 py-1 rounded-full text-[14px] text-[#171717] font-normal">
                    Entrada
                    <MoveUp className="w-4 h-4 text-[#525252]" />
                </span>
            );
        }
        return (
            <span className="inline-flex items-center gap-[3.35rem] px-3 py-1 rounded-full text-[14px] text-[#171717] font-normal">
                Salida
                <MoveDown className="w-4 h-4 text-[#525252]" />
            </span>
        );
    };

    if (isLoading) {
        return (
            <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden max-w-[1086px] max-h-[673px] flex items-center justify-center">
                <div className="flex flex-col items-center justify-center gap-3 my-4">
                    <LoaderCircle className="h-8 w-8 text-slate-600 animate-spin" />
                    <span className="text-sm text-gray-600">Cargando movimientos...</span>
                </div>
            </div>
        );
    }

    return (
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden max-w-[1086px] max-h-[700px]">
            <div className="overflow-x-auto">
                <table className="w-full text-sm text-left">
                    <thead className="text-[14px] bg-slate-200 text-[#404040] font-semibold h-[46px]">
                        <tr>
                            <th className="px-6 py-3">Producto</th>
                            <th className="px-6 py-3">Entradas / Salidas</th>
                            <th className="px-6 py-3">Motivo / Observaciones</th>
                            <th className="px-6 py-3 text-center">Cantidad</th>
                            <th className="px-6 py-3">Fecha / Hora</th>
                            <th className="px-6 py-3">Usuario</th>
                        </tr>
                    </thead>

                    <tbody className="divide-y divide-gray-200">
                        {(movements || []).slice(page * pageSize, (page + 1) * pageSize).map((movement) => (
                            <tr key={movement.id} className="hover:bg-gray-50 transition-colors">
                                <td className="px-6 py-4 text-[#171717] text-[14px] font-medium">
                                    {movement.productName || '—'}
                                </td>
                                <td className="px-6 py-4">
                                    {getMovementTypeBadge(movement.movementType)}
                                </td>
                                <td className="px-6 py-4 text-[#525252] text-[14px] max-w-xs truncate">
                                    {movement.reason || '—'}
                                </td>
                                <td className="px-6 py-4 text-[#171717] text-[14px] text-center font-normal">
                                    {movement.quantity}
                                </td>
                                <td className="px-6 py-4 text-[#525252] text-[14px]">
                                    {formatDate(movement.createdAt)}
                                </td>
                                <td className="px-6 py-4 text-[#525252] text-[14px]">
                                    {movement.userName || '—'}
                                </td>
                            </tr>
                        ))}

                        {(movements || []).length === 0 && (
                            <tr>
                                <td colSpan={6} className="px-6 py-6 text-center text-gray-500 text-sm">
                                    No hay movimientos para mostrar.
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            {(usedTotalPages >= 1 && totalItems > 0) && (
                <div className="flex items-center justify-center px-6 py-3 bg-gray-50 border-t">
                    <div className="flex items-center gap-2">
                        <button
                            onClick={handlePrev}
                            disabled={page === 0}
                            className="border-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-200 rounded-md disabled:opacity-50 flex items-center gap-1"
                        >
                            <ChevronLeft className="w-4 h-4" /> Anterior
                        </button>

                        <div className="flex gap-1 justify-center">
                            {Array.from({ length: usedTotalPages }, (_, i) => i).map((p) => (
                                <button
                                    key={p}
                                    onClick={() => setPage(p)}
                                    className={`px-3 py-1.5 text-sm rounded-md ${page === p ? 'bg-slate-600 text-white' : 'text-gray-700 hover:bg-gray-200'}`}>
                                    {p + 1}
                                </button>
                            ))}
                        </div>

                        <button
                            onClick={handleNext}
                            disabled={usedTotalPages === 0 || page >= usedTotalPages - 1}
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
