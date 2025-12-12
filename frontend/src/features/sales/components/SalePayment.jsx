import { useNavigate, useParams } from "react-router";
import { useGetSaleById } from "../hooks/useGetSaleById";
import { Loader2 } from "lucide-react";
import PaymentContent from "./payment/SalesPaymentContent";

const formatCurrency = (amount) => {
  if (typeof amount !== 'number') return '$ 0.00';
  return new Intl.NumberFormat('es-AR', {
    style: 'currency',
    currency: 'ARS',
    minimumFractionDigits: 0
  }).format(amount);
};

function SalePayment() {
  const navigate = useNavigate();
  const { id } = useParams();
  const { data: saleResponse, isLoading, error } = useGetSaleById(id);

  const handleClose = () => {
    navigate('/sales/pending');
  };

  let content;

  if (isLoading) {
    content = (
      <div className="flex justify-center items-center h-48">
        <Loader2 className="h-8 w-8 animate-spin text-stokia-neutral-400" />
        <p className="ml-3 text-stokia-neutral-500">Cargando venta...</p>
      </div>
    );
  } else if (error || saleResponse.status?.toLowerCase() !== 'pending') {
    const message = error || !saleResponse.success
      ? `Venta con ID ${id} no encontrada.`
      : `Venta ${id} no está en estado 'Pending'. Estado actual: ${saleResponse.status}`;

    content = (
      <div className="p-4 text-center">
        <h3 className="text-xl font-semibold text-red-600">Error</h3>
        <p className="mt-2 text-stokia-neutral-600">{message}</p>
        <button
          onClick={handleClose}
          className="mt-4 p-2 bg-stokia-neutral-200 rounded"
        >
          Cerrar
        </button>
      </div>
    );
  } else {
    content = (
      <PaymentContent
        saleResponse={saleResponse}
        totalAmount={saleResponse.totalAmount}
        formatCurrency={formatCurrency}
        handleClose={handleClose}
      />
    );
  }

  return content;
}

export default SalePayment;