import { useEffect, useState } from 'react';
import { useParams, useLocation } from 'react-router';
import { ChevronLeft, ChevronRight, FileUp, LoaderCircle } from 'lucide-react';
import { getPurchaseHistory } from '../services/customerService';
import { Button } from '@/shared/components/ui/button';
import apiClient from '@/shared/services/apiClient.js';

const PAGE_SIZE = 10;

export default function PurchaseHistoryPage() {
    const { id } = useParams();
    const location = useLocation();
    const [purchases, setPurchases] = useState([]);
    const [customerName, setCustomerName] = useState(location.state?.customerName || 'Cliente');
    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(0);
    const [pagination, setPagination] = useState({ totalPages: 0, totalElements: 0, pageSize: PAGE_SIZE });

    useEffect(() => {
        const fetchPurchaseHistory = async () => {
            setIsLoading(true);
            try {
                const data = await getPurchaseHistory(id, { page: currentPage, size: PAGE_SIZE });

                setPurchases(data.purchases || []);
                setPagination({
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    pageSize: data.pageSize
                });
            } catch (error) {
                console.error('Error fetching purchase history:', error);
                setPurchases([]);
            } finally {
                setIsLoading(false);
            }
        };

        fetchPurchaseHistory();
    }, [id, currentPage]);

    const handlePrev = () => {
        if (currentPage === 0) return;
        setCurrentPage(currentPage - 1);
    };

    const handleNext = () => {
        if (currentPage >= pagination.totalPages - 1) return;
        setCurrentPage(currentPage + 1);
    };

    const handleExport = () => {
        const rows = [
            ['Fecha', 'Cliente', 'Venta', 'Monto'],
            ...purchases.map(p => [
                new Date(p.orderDate).toLocaleString('es-ES'),
                p.customerName || customerName,
                `#${p.orderNumber || 'N/A'}`,
                `$${p.totalAmount || 0}`
            ])
        ];
        const csv = rows.map(r => r.map(v => `"${String(v).replace(/"/g, '""')}"`).join(',')).join('\n');
        const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `historial-compras-${customerName.replace(/\s+/g, '-')}-${new Date().toISOString().slice(0, 10)}.csv`;
        a.click();
        URL.revokeObjectURL(url);
    };

    const handleDownloadPdf = async (orderId) => {
        try {
            const response = await apiClient.get(`/api/orders/${orderId}/pdf`, {
                responseType: 'blob'
            });
            const blob = new Blob([response.data], { type: 'application/pdf' });
            const url = URL.createObjectURL(blob);
            window.open(url, '_blank');
            setTimeout(() => URL.revokeObjectURL(url), 100);
        } catch (error) {
            console.error('Error downloading PDF:', error);
            alert('Error al descargar el comprobante. Por favor, intenta de nuevo.');
        }
    };

    if (isLoading) {
        return (
            <div className="p-6 w-full">
                <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden max-w-1086 max-h-673 flex items-center justify-center">
                    <div className="flex flex-col items-center justify-center gap-3 my-4">
                        <LoaderCircle className="h-8 w-8 text-slate-600 animate-spin" />
                        <span className="text-sm text-gray-600">Cargando historial de compras...</span>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="p-6 w-full">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl font-semibold text-[#171717]">
                    Historial de compra de {customerName}
                </h1>
                <Button
                    onClick={handleExport}
                    className="bg-white text-neutral-950 hover:bg-gray-400 cursor-pointer shadow-sm"
                >
                    <FileUp className="h-4 w-4 mr-1" />
                    Exportar
                </Button>
            </div>

            <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden max-w-1086">
                <div className="overflow-x-auto">
                    <table className="w-full text-sm">
                        <thead className="text-[14px] bg-slate-200 text-[#404040] font-semibold h-11">
                            <tr>
                                <th className="px-6 py-3 text-center text-gray-700">FECHA</th>
                                <th className="px-6 py-3 text-center text-gray-700">CLIENTE</th>
                                <th className="px-6 py-3 text-center text-gray-700">VENTA</th>
                                <th className="px-6 py-3 text-center text-gray-700">MONTO</th>
                            </tr>
                        </thead>

                        <tbody className="divide-y divide-gray-100">
                            {purchases.map((purchase) => {
                                const date = new Date(purchase.orderDate);
                                const formattedDate = date.toISOString().split('T')[0];
                                const formattedTime = date.toLocaleTimeString('es-ES', { hour: 'numeric', minute: '2-digit', second: '2-digit', hour12: false });

                                return (
                                    <tr key={purchase.id} className="hover:bg-gray-50 transition-colors">
                                        <td className="px-6 py-4 font-normal text-[#525252] text-[14px] text-center">
                                            <div>{formattedDate}</div>
                                            <div className="text-xs">{formattedTime}</div>
                                        </td>
                                        <td className="px-6 py-4 font-normal text-[#171717] text-[14px] text-center">
                                            {purchase.customerName || customerName}
                                        </td>
                                        <td className="px-6 py-4 font-normal text-[#525252] text-[14px] text-center">
                                            <div className="flex flex-col items-center gap-2">
                                                <div>{purchase.orderNumber || 'N/A'}</div>
                                                {purchase.id && (
                                                    <button
                                                        onClick={() => handleDownloadPdf(purchase.id)}
                                                        className="px-3 py-1 text-xs bg-gray-200 hover:bg-gray-300 text-gray-700 rounded transition-colors cursor-pointer"
                                                    >
                                                        Ver comprobante
                                                    </button>
                                                )}
                                            </div>
                                        </td>
                                        <td className="px-6 py-4 font-normal text-[#525252] text-[14px] text-center">
                                            ${purchase.totalAmount?.toLocaleString('es-ES') || '0'}
                                        </td>
                                    </tr>
                                );
                            })}

                            {purchases.length === 0 && (
                                <tr>
                                    <td colSpan={4} className="px-6 py-6 text-center text-gray-500 text-sm">
                                        No hay historial de compras para mostrar.
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>

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
                            {Array.from({ length: pagination.totalPages || 1 }, (_, i) => i).map((page) => (
                                <button
                                    key={page}
                                    onClick={() => setCurrentPage(page)}
                                    className={`px-3 py-1.5 text-sm rounded-md ${currentPage === page ? 'bg-slate-600 text-white' : 'text-gray-700 hover:bg-gray-200'}`}
                                >
                                    {page + 1}
                                </button>
                            ))}
                        </div>

                        <button
                            onClick={handleNext}
                            disabled={currentPage >= pagination.totalPages - 1}
                            className="border-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-200 rounded-md disabled:opacity-50 flex items-center gap-1"
                        >
                            Siguiente <ChevronRight className="w-4 h-4" />
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
