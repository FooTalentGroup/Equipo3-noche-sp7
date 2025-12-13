import { Button } from "@/shared/components/ui/button";
import { useDownloadSaleReceipt } from "../../hooks/useSaleTicket";

function ConfirmedOrderCard({ order }) {
  const { downloadReceipt, isDownloading } = useDownloadSaleReceipt();

  return (
    <div className="flex flex-col p-4 border border-border rounded-lg max-w-60 max-h-36 gap-4 bg-stokia-neutral-50 shadow-md">
      <div className="flex flex-col gap-2">
        <h3 className="text-xl self-center">{order.orderNumber}</h3>
        <p className="text-sm text-stokia-neutral-600">{order.paymentNote}</p>
      </div>
      <Button
        variant="ghost"
        className={`hover:bg-stokia-neutral-100 ${isDownloading ? 'cursor-not-allowed opacity-50' : ''}`}
        disabled={isDownloading}
        onClick={() => downloadReceipt(order.id)}
      >
        {isDownloading ? 'Descargando...' : 'Ver comprobante'}
      </Button>
    </div>
  );
}

export default ConfirmedOrderCard;