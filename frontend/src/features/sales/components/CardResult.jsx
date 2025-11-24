import React from "react";
import { Button } from "@/shared/components/ui/button";

export const ProductCard = ({ name, stock, price, imageUrl }) => {
  return (
    <div className="bg-white rounded-lg shadow-sm border border-gray-300 overflow-hidden">
      <div className="w-full p-4">
        <div className="w-full h-36 overflow-hidden">
          <img
            src={imageUrl}
            alt={name}
            className="w-full h-full object-cover"
          />
        </div>
      </div>

      <div className="px-4 pb-4 flex flex-col items-center">
        <h3 className="text-sm font-medium text-gray-800 text-center mb-3 leading-tight">
          {name}
        </h3>

        <div className="w-full text-sm text-gray-700 mb-4 px-1 space-y-1">
          <div className="flex justify-between">
            <span className="text-gray-600">Stock:</span>
            <span className="font-semibold text-gray-800">{stock}</span>
          </div>

          <div className="flex justify-between">
            <span className="text-gray-600">Precio:</span>
            <span className="font-semibold text-gray-800">${price}</span>
          </div>
        </div>

        <Button
          className="
            w-full 
            bg-[#A7BBD2]
            hover:bg-[#94A9C3]
            text-white
            font-medium
            py-2 
            rounded-md 
            text-sm
            shadow-sm
          "
          onClick={() => console.log(`Agregando ${name} a la venta`)}
        >
          Agregar a la venta
        </Button>
      </div>
    </div>
  );
};
