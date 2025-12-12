import React, { useEffect } from "react";
import { Check } from "lucide-react";

export const OrderSuccessModal = ({ isOpen, onClose, autoCloseDuration = 2000 }) => {
    useEffect(() => {
    if (isOpen) {
      const timer = setTimeout(() => {
        onClose();
      }, autoCloseDuration);

      return () => clearTimeout(timer);
    }
  }, [isOpen, onClose, autoCloseDuration]);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/20  flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl p-8 w-full max-w-md text-center">
        <div className="flex justify-center mb-2">
          <div className="w-14 h-14 rounded-full border-2 border-stokia-success-500 flex items-center justify-center mb-4">
            <Check className="w-8 h-8 text-stokia-success-500" />
          </div>
        </div>

       
        <h2 className="text-base font-normal text-foreground mb-4">
          El pedido se ha registrado exitosamente
        </h2>

     
        <div className="text-left mb-6">
          <div className="flex items-start">
            <span className="text-foreground">•</span>
            <p className="text-sm text-muted-foreground">
              El stock ha sido actualizado.
            </p>
          </div>
          <div className="flex items-start">
            <span className="text-foreground">•</span>
            <p className="text-sm text-muted-foreground">
              El movimiento de inventario fue registrado.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};