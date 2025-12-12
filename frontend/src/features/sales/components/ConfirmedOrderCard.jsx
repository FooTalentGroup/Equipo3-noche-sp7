import React from "react";
import { Button } from "@/shared/components/ui/button";
import apiClient from "@/shared/services/apiClient";

export const ConfirmedOrderCard = ({ order }) => {
    const handleDownloadPDF = async () => {
        const orderId = order.id || order.backendId;

        if (!orderId) {
            alert("No se pudo obtener el ID de la orden");
            return;
        }

        try {
            const response = await apiClient.get(`/api/orders/${orderId}/pdf`, {
                responseType: 'blob'
            });

            const blob = new Blob([response.data], { type: 'application/pdf' });
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.download = `comprobante-${order.orderNumber || orderId}.pdf`;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Error al descargar el PDF:", error);
            alert("Error al generar el comprobante de pago");
        }
    };

    return (
        <div className="bg-stokia-neutral-50 border border-border rounded-lg p-4 shadow-sm flex flex-col">
            <div className="mb-3">
                <h3 className="text-lg text-foreground text-center">
                    Pedido #{order.orderNumber || order.id}
                </h3>
                {order.note && (
                    <p className="text-sm text-muted-foreground mt-2 text-center">
                        Nota: {order.note}
                    </p>
                )}
            </div>

            <div className="mt-auto pt-2">
                <Button
                    onClick={handleDownloadPDF}
                    variant="outline"
                    className="w-full h-10 text-sm font-medium"
                >
                    Ver comprobante
                </Button>
            </div>
        </div>
    );
};
