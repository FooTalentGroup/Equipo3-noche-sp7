import React from "react";
import { Plus } from "lucide-react";
import { Button } from "@/shared/components/ui/button";

export const SalesSummary = () => {
  return (
    <div className="bg-white border border-gray-200 rounded-lg shadow-md p-6 sticky top-8">
      <h2 className="text-xl font-semibold text-gray-800 mb-4">
        Resumen de compra
      </h2>
      <div className="space-y-4">
        <h3 className="font-medium text-gray-700">Productos agregados</h3>
        <div className="min-h-[100px] border-b border-gray-200">
          <p className="text-sm text-gray-500 italic">
            No hay productos seleccionados.
          </p>
        </div>
      </div>

      <div className="mt-6 space-y-3">
        <div className="flex justify-between text-gray-700">
          <span>Subtotal</span>
          <span>$0.00</span>
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

        <div className="pt-4 border-t border-gray-200 flex justify-between text-lg font-bold text-gray-800">
          <span>Total</span>
          <span>$0.00</span>
        </div>
      </div>
    </div>
  );
};
