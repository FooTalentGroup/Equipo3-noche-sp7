import React from "react";
import { Button } from "@/shared/components/ui/button";

export const OrderCard = ({ order, status, onCollect, onEdit, onCancel }) => {
  const showChargeButton = status === "pending";
  const showEditButton = status === "pending";
  const showCancelButton = status === "pending";

  return (
    <div className="bg-stokia-neutral-50 border border-border rounded-lg p-4 shadow-sm flex flex-col space-y-4">
      <div className="border-b border-muted pb-3">
        <h3 className="text-lg font-semibold text-foreground text-center">
          Pedido #{order.id}
        </h3>
        {order.note && (
          <p className="text-sm text-muted-foreground mt-1">
            Nota: {order.note}
          </p>
        )}
      </div>

      <div className="flex-grow space-y-2">
        <div className="flex justify-between items-center text-sm font-medium">
          <span className="text-foreground">Producto</span>
          <span className="text-foreground">Cant.</span>
        </div>
        {order.products.map((product, index) => (
          <div
            key={index}
            className="flex justify-between items-center text-sm"
          >
            <span className="text-muted-foreground truncate flex-1 pr-2">
              {product.name}
            </span>
            <span className="text-foreground font-medium min-w-[30px] text-right">
              {product.quantity}
            </span>
          </div>
        ))}
      </div>


      <div className="space-y-2 pt-2">
        {showChargeButton && (
          <Button
            onClick={() => onCollect(order)}
            className="w-full bg-btn-primary hover:bg-btn-primary/90 text-white h-10"
          >
            Cobrar
          </Button>
        )}
        {showEditButton && (
          <Button
            onClick={() => onEdit(order)}
            variant="outline"
            className="w-full h-10"
          >
            Editar pedido
          </Button>
        )}
        {showCancelButton && (
          <Button
            onClick={() => onCancel(order.id)}
            variant="destructive"
            className="w-full h-10"
          >
            Cancelar pedido
          </Button>
        )}

        {status === "cancelled" && (
          <div className="rounded-md p-3 text-center ">
            {order.cancelledAt && (
              <p className="text-xs mt-1">
                {new Date(order.cancelledAt).toLocaleDateString('es-ES', {
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric'
                })}
              </p>
            )}
          </div>
        )}
      </div>
    </div>

  );
};