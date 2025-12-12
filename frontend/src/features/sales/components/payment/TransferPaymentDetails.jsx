import { Button } from "@/shared/components/ui/button";

function TransferPaymentDetails({ totalAmount }) {
  return (
    <div className="flex flex-col gap-10 items-center">
      <span className="text-center">
        <h5 className="text-xl font-semibold text-stokia-neutral-950">El cliente debe realizar la transferencia a la cuenta indicada</h5>
        <p className="text-stokia-neutral-500">Una vez registrado el pago el usuario debe confirmar la venta manualmente</p>
      </span>
      <Button variant={'stokia'} size={'lg'}>Confirmar cobro</Button>
    </div>
  )
}

export default TransferPaymentDetails;