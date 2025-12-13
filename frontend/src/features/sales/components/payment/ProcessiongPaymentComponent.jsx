import { Loader2 } from "lucide-react";

function ProcessingPaymentComponent() {
  return (
    <div className="flex flex-col justify-center items-center h-full space-y-4">
      <Loader2 className="h-12 w-12 animate-spin text-stokia-primary-500" />
      <p className="text-xl font-semibold text-stokia-neutral-700">Procesando orden...</p>
    </div>
  );
}

export default ProcessingPaymentComponent;
