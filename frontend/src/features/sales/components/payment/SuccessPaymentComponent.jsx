import { Button } from "@/shared/components/ui/button";
import { CheckCircle2, Loader2 } from "lucide-react";
import { useDownloadSaleReceipt } from "../../hooks/useSaleTicket";

function SuccessPaymentComponent({ handleClose, orderId }) {
  const { downloadReceipt, isDownloading } = useDownloadSaleReceipt();

  const handleDownload = () => {
    if (orderId) {
      downloadReceipt(orderId);
    }
  };

  return (
    <div className="flex flex-col justify-center items-center h-full space-y-8 p-4">
      <div className="flex flex-col gap-2 items-center justify-center p-6 rounded-lg w-full flex-1">
        <div className="flex flex-col gap-2 items-center justify-center h-24 w-24 bg-stokia-green/50 rounded-full">
          <CheckCircle2 className="h-16 w-16 text-stokia-green-600" />
        </div>
        <h3 className="text-xl font-bold text-stokia-neutral-950">¡Pago registrado!</h3>
      </div>
      <div className="flex gap-4 w-full justify-center">
        <Button
          variant={'ghost'}
          size={'lg'}
          onClick={handleDownload}
          disabled={isDownloading}
        >
          {isDownloading ? (
            <>
              <Loader2 className="h-5 w-5 mr-2 animate-spin" />
              Descargando...
            </>
          ) : (
            <>
              <svg className="h-5 w-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 13h6m-3-3v6m5 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path></svg>
              Generar comprobante de pago
            </>
          )}
        </Button>
        <Button
          variant={'stokia'}
          size={'lg'}
          onClick={handleClose}
        >
          Volver al panel de pendientes
        </Button>
      </div>
    </div>
  );
}

export default SuccessPaymentComponent;