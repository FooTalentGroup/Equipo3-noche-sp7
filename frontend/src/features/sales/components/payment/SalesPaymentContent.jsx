import React from 'react';
import CashPaymentDetails from "./CashPaymentDetails";
import { useConfirmSale } from "../../hooks/useConfirmSale";
import ProcessingPaymentComponent from "./ProcessiongPaymentComponent";
import SuccessPaymentComponent from "./SuccessPaymentComponent";

function PaymentContent({ saleResponse, totalAmount, formatCurrency, handleClose }) {
  const { mutateAsync: confirmPayment, isPending: isConfirmingSale, error: confirmationError, status, reset } = useConfirmSale(saleResponse.id);

  const handleConfirmPayment = async () => {
    try {
      await confirmPayment();
    } catch (error) {
      alert(error?.message || "Intente de nuevo.");
    }
  };

  const renderDynamicContent = () => {

    if (status === 'pending') {
      return <ProcessingPaymentComponent />;
    }

    if (status === 'error') {
      return (
        <div className="flex flex-col items-center justify-center text-center p-8 bg-red-50 border border-red-200 rounded-lg">
          <h4 className="text-2xl font-bold text-red-600 mb-3">❌ Error en el Pago</h4>
          <p className="text-lg text-red-800 mb-4">
            No se pudo confirmar la venta.
          </p>
          <p className="text-sm text-red-500">{confirmationError?.message || "Intente de nuevo."}</p>
          <button
            onClick={() => reset()}
            className="mt-4 p-2 bg-red-100 text-red-700 rounded hover:bg-red-200 transition"
          >
            Volver a intentar
          </button>
        </div>
      );
    }
    if (status === 'success') {
      return <SuccessPaymentComponent handleClose={handleClose} orderId={saleResponse.id} />;
    }
    return (
      <div className="flex-1 flex flex-col justify-center items-center h-full">
        <div className="flex flex-col gap-10 w-10/12 max-w-md">

          <div className="p-6 border border-stokia-neutral-300 bg-stokia-neutral-50 rounded-lg shadow-lg">
            <p className="text-lg text-stokia-neutral-600">Total a pagar:</p>
            <p className="text-3xl font-bold text-stokia-neutral-950">
              {formatCurrency(totalAmount)}
            </p>
          </div>

          <div className="flex flex-col justify-center flex-1 p-4 rounded-lg w-full">
            <CashPaymentDetails
              totalAmount={totalAmount}
              handleConfirm={handleConfirmPayment}
              isConfirming={isConfirmingSale}
            />
          </div>
        </div>
      </div>
    );
  };

  const isStatusView = status !== 'idle';

  return (
    <div className="w-full h-full flex justify-center">
      <section
        id="method-details"
        className="flex flex-col gap-4 p-9 w-full border border-stokia-neutral-200 rounded-lg"
      >
        <h3 className={`text-3xl font-semibold text-stokia-neutral-950`}>Cobro en Efectivo</h3>

        <div
          className={`flex-1 ${isStatusView ? 'flex items-center justify-center p-0' : 'p-0'}`}
        >
          {renderDynamicContent()}
        </div>
      </section>
    </div>
  );
}

export default PaymentContent;