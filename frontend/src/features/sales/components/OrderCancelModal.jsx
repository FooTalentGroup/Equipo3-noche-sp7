import React from "react";
import { Button } from "@/shared/components/ui/button";
import { Trash } from "lucide-react";

export const OrderCancelModal = ({ onConfirm, onCancel }) => {
  return (
    <div className="fixed inset-0 bg-black/10 bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 max-w-[480px] w-full mx-4 shadow-xl">
        <h2 className="text-xl font-semibold text-foreground mb-4">
          ¿Cancelar pedido?
        </h2>
        <p className="text-muted-foreground mb-6">
          ¿Estás seguro de cancelar este pedido pendiente?
          <br />
          <br />
          Si cancelas el pedido, el stock del mismo volverá a estar disponible
          para una nueva venta y quedará registrado en los pedidos cancelados.
        </p>
        <div className="flex justify-end gap-3">
          <Button onClick={onCancel} variant="outline" >
            Cancelar
          </Button>
          <Button 
          onClick={onConfirm} 
          variant="destructive"
           className="w-[120px]" 
          >
            <Trash className="h-4 w-4"/>
            Confirmar
          </Button>
        </div>
      </div>
    </div>
  );
};