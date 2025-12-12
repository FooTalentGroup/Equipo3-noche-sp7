import { useApiMutation } from "@/shared/hooks/useApi";
import { getSaleTicket } from "../services/salesService";

const downloadBlob = (blob, filename) => {
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  window.URL.revokeObjectURL(url);
};

export const useDownloadSaleReceipt = () => {
  const {
    mutateAsync: downloadReceipt,
    isLoading: isDownloading,
    error: downloadError
  } = useApiMutation(async (orderId) => {
    const pdfBlob = await getSaleTicket(orderId);
    downloadBlob(pdfBlob, `comprobante_venta_${orderId}.pdf`);
    return true;
  });

  return {
    downloadReceipt,
    isDownloading,
    downloadError
  };
};