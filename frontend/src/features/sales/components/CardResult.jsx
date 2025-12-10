import React, { useState } from "react";
import { Plus, Minus } from "lucide-react";
import { Button } from "@/shared/components/ui/button";
import { Input } from "@/shared/components/ui/input";

export const ProductCard = ({
  id,
  name,
  stock,
  price,
  imageUrl,
  onAddToCart,
}) => {
  const [quantity, setQuantity] = useState(1);

  const handleIncrement = () => {
    if (quantity < stock) {
      setQuantity((q) => q + 1);
    }
  };

  const handleDecrement = () => {
    if (quantity > 1) {
      setQuantity((q) => q - 1);
    }
  };

  const handleAddToCart = () => {
    if (stock > 0 && quantity > 0) {
      onAddToCart({
        id,
        name,
        price,
        currentStock: stock,
        photoUrl: imageUrl,
        quantity,
      });
      setQuantity(1);
    }
  };

  const isOutOfStock = stock === 0;

  return (
    <div className="bg-white border border-gray-200 rounded-lg shadow-md overflow-hidden flex flex-col">
      <div className="p-2 h-32 overflow-hidden">
        <img src={imageUrl} alt={name} className="w-full h-full object-cover" />
      </div>

      <div className="p-4 flex flex-col flex-grow">
        <h3 className="font-semibold text-lg text-gray-800 line-clamp-2">
          {name}
        </h3>
        <p className="text-sm text-gray-500 mb-2">Características</p>

        <div className="text-sm text-gray-700 space-y-1 mb-3">
          <p>
            Stock disponible: <span className="font-medium">{stock}</span>
          </p>
          <p>
            Precio de venta: <span className="font-medium">${price}</span>
          </p>
          <p>
            Descuento aplicado: <span className="font-medium">0%</span>
          </p>
        </div>

        <div className="flex flex-col gap-2 items-center justify-between mt-auto pt-3 border-t border-gray-100">
          <div className="flex items-center border border-gray-300 rounded-md">
            <Button
              variant="ghost"
              onClick={handleDecrement}
              disabled={quantity <= 1 || isOutOfStock}
              className="p-1 h-8 w-8 text-gray-600 hover:bg-gray-100"
            >
              <Minus className="h-4 w-4" />
            </Button>
            <Input
              value={quantity.toString()}
              readOnly
              className="w-10 text-center p-0 h-8 border-y-0 border-x border-gray-300 focus:outline-none focus:ring-0 text-sm"
            />
            <Button
              variant="ghost"
              onClick={handleIncrement}
              disabled={quantity >= stock || isOutOfStock}
              className="p-1 h-8 w-8 text-gray-600 hover:bg-gray-100"
            >
              <Plus className="h-4 w-4" />
            </Button>
          </div>

          <Button
            onClick={handleAddToCart}
            disabled={isOutOfStock}
            className="bg-[#436086] hover:bg-[#384d6b] text-white py-1 h-8 px-18 rounded-md shadow-sm text-sm"
          >
            {isOutOfStock ? "Sin Stock" : `Agregar`}
          </Button>
        </div>
      </div>
    </div>
  );
};
