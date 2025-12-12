import React, { useState, useEffect } from "react";
import { X, CheckCircle2, Loader2 } from "lucide-react";
import { Button } from "@/shared/components/ui/button";
import apiClient from "@/shared/services/apiClient";

export const PaymentModal = ({ isOpen, order, onClose, onConfirm }) => {
    const [receivedAmount, setReceivedAmount] = useState(0);
    const [change, setChange] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [isSuccess, setIsSuccess] = useState(false);
    const [error, setError] = useState(null);
    const [createdOrderId, setCreatedOrderId] = useState(null);

    useEffect(() => {
        if (order) {
            const calculatedChange = receivedAmount - order.total;
            setChange(calculatedChange);
        }
    }, [receivedAmount, order]);

    const handleQuickAmount = (amount) => {
        setReceivedAmount(amount);
    };

    const handleConfirm = async () => {
        if (receivedAmount >= order.total) {
            setIsLoading(true);
            setError(null);

            try {
                const createdOrder = await onConfirm(order, receivedAmount, change);
                const orderId = createdOrder?.id || createdOrder?.data?.id;
                setCreatedOrderId(orderId);
                setIsSuccess(true);
            } catch (err) {
                setError(err.message || "Error al procesar el pago");
                setIsLoading(false);
            }
        }
    };

    const handleDownloadPDF = async () => {
        if (!createdOrderId) {
            alert("No se pudo obtener el ID de la orden");
            return;
        }

        try {
            const response = await apiClient.get(`/api/orders/${createdOrderId}/pdf`, {
                responseType: 'blob'
            });

            const blob = new Blob([response.data], { type: 'application/pdf' });
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.download = `comprobante-pago-${createdOrderId}.pdf`;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Error al descargar el PDF:", error);
            alert("Error al generar el comprobante de pago");
        }
    };

    const handleClose = () => {
        setReceivedAmount(0);
        setChange(0);
        setIsLoading(false);
        setIsSuccess(false);
        setError(null);
        setCreatedOrderId(null);
        onClose();
    };

    if (!isOpen || !order) return null;

    const isChangePositive = change > 0;
    const isPaymentComplete = receivedAmount >= order.total;

    if (isSuccess) {
        return (
            <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
                <div className="bg-background rounded-2xl shadow-lg p-8 w-full max-w-xl relative">
                    <button
                        onClick={handleClose}
                        className="absolute top-6 right-6 text-muted-foreground hover:text-foreground"
                    >
                        <X className="w-6 h-6" />
                    </button>

                    <h2 className="text-2xl font-semibold text-foreground mb-10">
                        Cobro en efectivo
                    </h2>
                    <div className="flex flex-col items-center justify-center py-6 mb-8">
                        <div className="bg-green-100 rounded-full p-6 mb-6">
                            <CheckCircle2 className="w-20 h-20 text-green-600" />
                        </div>
                        <h3 className="text-2xl font-semibold text-foreground">
                            ¡Pago registrado!
                        </h3>
                    </div>

                    <div className="flex gap-4">
                        <Button
                            onClick={handleDownloadPDF}
                            variant="outline"
                            className="flex-1 h-11 text-xs font-medium border-border hover:bg-gray-50 whitespace-nowrap"
                        >
                            Generar comprobante de pago
                        </Button>
                        <Button
                            onClick={handleClose}
                            className="flex-1 h-11 text-xs font-medium bg-btn-primary hover:bg-btn-primary/90 text-white whitespace-nowrap"
                        >
                            Volver al panel de pendientes
                        </Button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
            <div className="bg-background rounded-2xl shadow-lg p-6 w-full max-w-md relative">
                <button
                    onClick={handleClose}
                    className="absolute top-4 right-4 text-muted-foreground hover:text-foreground"
                    disabled={isLoading}
                >
                    <X className="w-6 h-6" />
                </button>

                <h2 className="text-2xl font-semibold text-foreground mb-6">
                    Cobro en efectivo
                </h2>

                <div className="bg-stokia-neutral-50 border border-border rounded-lg p-4 mb-6">
                    <p className="text-sm text-muted-foreground mb-1">Total a pagar</p>
                    <p className="text-3xl font-semibold text-foreground">
                        ${order.total.toLocaleString("es-CL")}
                    </p>
                </div>

                <div className="mb-4">
                    <label className="text-sm text-foreground font-medium mb-2 block">
                        Monto recibido:
                    </label>
                    <div className="relative">
                        <span className="absolute left-4 top-1/2 -translate-y-1/2 text-2xl text-muted-foreground">
                            $
                        </span>
                        <input
                            type="number"
                            value={receivedAmount || ""}
                            onChange={(e) => setReceivedAmount(Number(e.target.value))}
                            className="w-full bg-background border border-border rounded-lg pl-10 pr-4 py-3 text-2xl font-medium text-foreground focus:outline-none focus:ring-2 focus:ring-btn-primary"
                            placeholder="0"
                            disabled={isLoading}
                        />
                    </div>
                </div>

                <div
                    className={`rounded-lg p-4 mb-6 transition-colors ${isChangePositive
                        ? "bg-green-50 border-2 border-green-500"
                        : "bg-red-50 border-2 border-red-300"
                        }`}
                >
                    <div className="flex items-center justify-between">
                        <span
                            className={`text-sm font-medium ${isChangePositive ? "text-green-700" : "text-red-700"
                                }`}
                        >
                            Su vuelto:
                        </span>
                        <span
                            className={`text-xl font-semibold ${isChangePositive ? "text-green-700" : "text-red-700"
                                }`}
                        >
                            ${change >= 0 ? change.toLocaleString("es-CL") : "0,00"}
                        </span>
                    </div>
                </div>

                {error && (
                    <div className="mb-4 p-4 bg-red-50 border border-red-300 rounded-lg">
                        <p className="text-sm text-red-700 font-medium">{error}</p>
                    </div>
                )}

                <div className="flex gap-3 mb-6">
                    <Button
                        onClick={() => handleQuickAmount(30000)}
                        variant="outline"
                        className="flex-1 h-12 text-sm font-medium"
                        disabled={isLoading}
                    >
                        $30.000
                    </Button>
                    <Button
                        onClick={() => handleQuickAmount(35000)}
                        variant="outline"
                        className="flex-1 h-12 text-sm font-medium"
                        disabled={isLoading}
                    >
                        $35.000
                    </Button>
                    <Button
                        onClick={() => handleQuickAmount(40000)}
                        variant="outline"
                        className="flex-1 h-12 text-sm font-medium"
                        disabled={isLoading}
                    >
                        $40.000
                    </Button>
                </div>

                <Button
                    onClick={handleConfirm}
                    disabled={!isPaymentComplete || isLoading}
                    className={`w-full h-12 text-base font-medium ${isPaymentComplete && !isLoading
                        ? "bg-btn-primary hover:bg-btn-primary/90 text-white"
                        : "bg-stokia-neutral-50 text-muted-foreground cursor-not-allowed"
                        }`}
                >
                    {isLoading ? (
                        <span className="flex items-center justify-center gap-2">
                            <Loader2 className="w-5 h-5 animate-spin" />
                            Procesando...
                        </span>
                    ) : (
                        "Confirmar cobro"
                    )}
                </Button>
            </div>
        </div>
    );
};
