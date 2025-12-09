import React from "react";
import { Plus, Trash2 } from "lucide-react";
import { Button } from "@/shared/components/ui/button";

const CartItem = ({ item, onRemove }) => {
  const formatCurrency = (amount) => `$${amount.toFixed(2)}`;

  return (
    <div className="flex justify-between items-start text-sm border-b border-gray-100 py-2 last:border-b-0 min-w-0">
      <div className="flex-1 min-w-0 pr-2 flex-shrink">
        <span className="text-gray-800 font-medium truncate block">
          {item.name}
        </span>

        <span className="text-gray-500 text-xs">
          {formatCurrency(item.price)} x{" "}
          <span className="font-bold">{item.quantity}</span>
        </span>
      </div>

      <div className="flex items-center space-x-2 flex-shrink-0">
        <span className="font-bold text-gray-800 min-w-[60px] text-right">
          {formatCurrency(item.price * item.quantity)}
        </span>
        <Button
          variant="ghost"
          onClick={() => onRemove(item.id)}
          className="p-1 h-6 w-6 text-red-500 hover:bg-red-100 flex-shrink-0"
        >
          <Trash2 className="h-4 w-4" />
        </Button>
      </div>
    </div>
  );
};

export const SalesSummary = ({
  cartItems = [],
  subtotal = 0,
  total = 0,
  onRemoveItem,
}) => {
  const formatCurrency = (amount) => `$${amount.toFixed(2)}`;

  return (
    <div className="bg-white border border-gray-200 rounded-lg shadow-md p-6 w-full h-[439px] flex flex-col">
      <div className="space-y-4 flex flex-col flex-grow min-h-0">
        <h3 className="font-medium text-gray-700 flex-shrink-0">
          Productos agregados
        </h3>

        <div className="min-h-[100px] flex-grow overflow-y-auto pr-2 custom-scroll border-b border-gray-200">
          {cartItems.length > 0 ? (
            cartItems.map((item) => (
              <CartItem key={item.id} item={item} onRemove={onRemoveItem} />
            ))
          ) : (
            <p className="text-sm text-gray-500 italic text-center py-4">
              No hay productos seleccionados.
            </p>
          )}
        </div>
      </div>

      <div className="mt-6 space-y-3 flex-shrink-0">
        <div className="flex justify-between text-gray-700">
          <span>Subtotal</span>
          <span>{formatCurrency(subtotal)}</span>
        </div>

        <div>
          <p className="text-sm text-gray-500 mb-2">
            ¿Desea agregar descuento a la venta?
          </p>
          <Button
            variant="outline"
            className="w-full text-sm text-[#436086] border-[#436086] hover:bg-gray-50 flex items-center justify-center space-x-2"
          >
            <Plus className="h-4 w-4" />
            <span>Agregar descuento</span>
          </Button>
        </div>

        <div className="pt-2 border-t border-gray-200 flex justify-between text-[16px] font-bold text-gray-800">
          <span>Total</span>
          <span>{formatCurrency(total)}</span>
        </div>
      </div>
    </div>
  );
};
