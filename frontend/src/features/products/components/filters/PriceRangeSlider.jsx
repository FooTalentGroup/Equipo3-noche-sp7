import { useState, useEffect } from 'react';
import { Label } from '@/shared/components/ui/label';
import { Input } from '@/shared/components/ui/input';

export function PriceRangeSlider({ minPrice = 0, maxPrice = 100, value, onRangeChange }) {
  const [min, setMin] = useState(value?.min ?? minPrice);
  const [max, setMax] = useState(value?.max ?? maxPrice);

  useEffect(() => {
    if (value) {
      setMin(value.min ?? minPrice);
      setMax(value.max ?? maxPrice);
    }
  }, [value, minPrice, maxPrice]);

  useEffect(() => {
    onRangeChange?.({ min, max });
  }, [min, max, onRangeChange]);

  return (
    <div className="space-y-4">
      <Label>Rango de Precios</Label>
      <div className="flex gap-4 items-center">
        <div className="flex-1">
          <Label htmlFor="min-price" className="text-xs text-gray-500">
            Mínimo
          </Label>
          <Input
            id="min-price"
            type="number"
            min={minPrice}
            max={maxPrice}
            step="0.01"
            value={min}
            onChange={(e) => {
              const value = parseFloat(e.target.value) || minPrice;
              setMin(Math.max(minPrice, Math.min(value, max)));
            }}
          />
        </div>
        <span className="text-gray-400 mt-6">-</span>
        <div className="flex-1">
          <Label htmlFor="max-price" className="text-xs text-gray-500">
            Máximo
          </Label>
          <Input
            id="max-price"
            type="number"
            min={minPrice}
            max={maxPrice}
            step="0.01"
            value={max}
            onChange={(e) => {
              const value = parseFloat(e.target.value) || maxPrice;
              setMax(Math.min(maxPrice, Math.max(value, min)));
            }}
          />
        </div>
      </div>
      <div className="text-xs text-gray-500 text-center">
        ${min.toFixed(2)} - ${max.toFixed(2)}
      </div>
    </div>
  );
}

