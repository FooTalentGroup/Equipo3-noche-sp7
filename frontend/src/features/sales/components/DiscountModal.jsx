import React, { useState } from "react";
import { X } from "lucide-react";
import { Button } from "@/shared/components/ui/button";

const DiscountModal = ({
  isOpen,
  onClose,
  onApplyDiscount,
  saleTotalValue = 0,
}) => {
  if (!isOpen) return null;

  const [discountType, setDiscountType] = useState("");
  const [discountValue, setDiscountValue] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const symbol =
    discountType === "percentage" ? "%" : discountType === "fixed" ? "$" : "";

  const maxDiscountValue = saleTotalValue * 0.2;

  const handleApply = () => {
    const value = parseFloat(discountValue);

    if (!discountType) {
      setErrorMessage("Por favor, selecciona un tipo de descuento.");
      return;
    }
    if (isNaN(value) || value <= 0) {
      setErrorMessage("Por favor, introduce un valor de descuento válido.");
      return;
    }

    let isValidDiscount = true;
    let limitAlert = "";

    if (discountType === "percentage") {
      if (value > 20) {
        isValidDiscount = false;
        limitAlert = "El porcentaje no puede superar el 20%.";
      }
    } else if (discountType === "fixed") {
      if (value > maxDiscountValue) {
        isValidDiscount = false;

        const formattedMax = maxDiscountValue.toLocaleString("es-CL", {
          style: "currency",
          currency: "CLP",
          minimumFractionDigits: 0,
        });
        limitAlert = `El monto fijo no puede superar el 20% del valor de la venta (${formattedMax}).`;
      }
    }

    if (!isValidDiscount) {
      setErrorMessage(limitAlert);
      return;
    }

    setErrorMessage("");
    onApplyDiscount({
      type: discountType,
      value: value,
    });

    setDiscountValue("");
    setDiscountType("");
    onClose();
  };

  const handleTypeChange = (e) => {
    setDiscountType(e.target.value);
    setErrorMessage("");
  };

  const handleValueChange = (e) => {
    setDiscountValue(e.target.value);
    setErrorMessage("");
  };

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-[1000] p-4">
      <div className="bg-white rounded-lg shadow-2xl w-full max-w-md mx-auto p-6 relative">
        <div className="flex justify-between items-center border-b pb-3 mb-4">
          <h2 className="text-xl font-semibold text-gray-900">
            Agregar descuento
          </h2>
          <Button
            variant="ghost"
            onClick={onClose}
            className="p-1 h-8 w-8 text-gray-500 hover:text-gray-900"
          >
            <X className="h-5 w-5" />
          </Button>
        </div>

        <div className="space-y-6">
          <div>
            <label
              htmlFor="discountType"
              className="block text-sm font-medium text-gray-700 mb-1"
            >
              Tipo de descuento *
            </label>
            <select
              id="discountType"
              value={discountType}
              onChange={handleTypeChange}
              className="block w-full rounded-md border-gray-300 shadow-sm py-2 px-3 text-sm border focus:border-[#436086] focus:ring-[#436086]"
            >
              <option value="" disabled>
                Selecciona cómo se calculará el descuento
              </option>
              <option value="percentage">Porcentaje (%)</option>
              <option value="fixed">Monto Fijo ($)</option>
            </select>
          </div>

          <div>
            <label
              htmlFor="discountValue"
              className="block text-sm font-medium text-gray-700 mb-1"
            >
              Valor del descuento *
            </label>
            <div className="relative">
              {symbol && (
                <span className="absolute left-0 top-0 bottom-0 flex items-center pl-3 text-gray-500 pointer-events-none text-sm font-medium">
                  {symbol}
                </span>
              )}
              <input
                id="discountValue"
                type="number"
                value={discountValue}
                onChange={handleValueChange}
                placeholder="Indica el valor"
                className={`block w-full rounded-md border-gray-300 shadow-sm pr-3 py-2 text-sm border focus:border-[#436086] focus:ring-[#436086] ${
                  symbol ? "pl-8" : "pl-3"
                }`}
              />
            </div>

            {errorMessage ? (
              <p className="mt-1 text-sm text-red-600">{errorMessage}</p>
            ) : (
              <p className="mt-1 text-xs text-gray-500">
                El valor ingresado no puede superar el 20% del total de la
                venta.
              </p>
            )}
          </div>
        </div>

        <div className="flex justify-end space-x-3 mt-8 pt-4 border-t border-gray-100">
          <Button
            variant="outline"
            onClick={onClose}
            className="text-sm border-gray-300 text-gray-700"
          >
            Cancelar
          </Button>
          <Button
            onClick={handleApply}
            disabled={
              !discountType ||
              !discountValue ||
              isNaN(parseFloat(discountValue)) ||
              parseFloat(discountValue) <= 0
            }
            className="bg-[#3b4c65] hover:bg-[#2c3a50] text-white text-sm font-medium disabled:opacity-50"
          >
            Implementar descuento
          </Button>
        </div>
      </div>
    </div>
  );
};

export default DiscountModal;
