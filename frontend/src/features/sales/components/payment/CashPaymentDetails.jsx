import { Button } from "@/shared/components/ui/button";
import { InputGroup, InputGroupInput, InputGroupAddon } from "@/shared/components/ui/input-group";
import { Label } from "@/shared/components/ui/label";
import { useState, useMemo } from "react";

const formatCurrency = (amount) => {
  if (typeof amount !== 'number') return '$ 0.00';
  return new Intl.NumberFormat('es-AR', {
    style: 'currency',
    currency: 'ARS',
    minimumFractionDigits: 0
  }).format(amount);
};

const calculateQuickAmounts = (total) => {
  if (total <= 0) return [];

  const amounts = [];
  let multiplier = 1;
  if (total > 1000) {
    multiplier = Math.pow(10, Math.floor(Math.log10(total)) - 1);
  } else if (total > 100) {
    multiplier = 100;
  } else {
    multiplier = 10;
  }

  const initialRoundUp = Math.ceil(total / multiplier) * multiplier;

  if (initialRoundUp > total) {
    amounts.push(initialRoundUp);
  }
  const increment = total > 5000 ? 5000 : total > 500 ? 500 : 100;

  let nextAmount = amounts.length > 0 ? amounts[0] + increment : Math.ceil(total / increment) * increment;
  while (amounts.length < 3) {
    if (!amounts.includes(nextAmount) && nextAmount > total) {
      amounts.push(nextAmount);
    }
    nextAmount += increment;
  }
  if (total < 1000) {
    return [1000, 2000, 5000].filter(a => a > total);
  }

  return amounts.slice(0, 3);
};


function CashPaymentDetails({ totalAmount, handleConfirm }) {
  const [receivedAmount, setReceivedAmount] = useState("");

  const amountReceived = parseFloat(receivedAmount) || 0;
  const changeDue = amountReceived > totalAmount ? amountReceived - totalAmount : 0;
  const isPaymentComplete = amountReceived >= totalAmount;

  const quickAmounts = useMemo(() => calculateQuickAmounts(totalAmount), [totalAmount]);
  const handleQuickAmount = (amount) => {
    setReceivedAmount(String(amount));
  };

  return (
    <div className="flex flex-col gap-10 self-center">

      <Label className="flex flex-col gap-2 items-start w-full text-xl">
        Monto recibido
        <InputGroup className="border-0 shadow-none py-9 border-b-2 border-stokia-neutral-600 rounded-none">
          <InputGroupInput
            type="number"
            placeholder="0"
            min={0}
            value={receivedAmount}
            className="py-2 h-24 text-5xl font-bold shadow-none border-stokia-neutral-200"
            onChange={(e) => setReceivedAmount(e.target.value)}
          />
          <InputGroupAddon className="text-4xl font-semibold text-stokia-neutral-600">
            $
          </InputGroupAddon>
        </InputGroup>
      </Label>

      {quickAmounts.length > 0 && (
        <div className="flex gap-4 justify-center">
          {quickAmounts.map((amount) => (
            <Button
              key={amount}
              variant={'outline'}
              size={'lg'}
              onClick={() => handleQuickAmount(amount)}
              className={`text-xl`}
            >
              {formatCurrency(amount)}
            </Button>
          ))}
        </div>
      )}

      <div className={`flex justify-between items-center p-2.5 ${isPaymentComplete ? 'bg-green-50' : 'bg-red-50'} rounded-lg border ${isPaymentComplete ? 'border-green-300' : 'border-red-300'}`}>
        <span className={`text-xl ${isPaymentComplete ? 'text-green-700' : 'text-red-700'}`}>Su vuelto:</span>
        <span className={`text-2xl ${isPaymentComplete ? 'text-green-700' : 'text-red-700'}`}>
          {formatCurrency(changeDue)}
        </span>
      </div>

      <Button variant={'stokia'} size={'lg'} disabled={!isPaymentComplete} onClick={handleConfirm}>Confirmar cobro</Button>
    </div>

  )
}

export default CashPaymentDetails;