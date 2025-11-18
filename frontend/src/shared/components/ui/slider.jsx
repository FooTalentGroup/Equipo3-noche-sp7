import * as React from 'react';
import { cn } from '@/lib/utils';

const Slider = React.forwardRef(
  ({ className, value, onChange, min = 0, max = 100, step = 1, ...props }, ref) => {
    const handleChange = (e) => {
      const newValue = parseFloat(e.target.value);
      onChange?.(newValue);
    };

    return (
      <div className={cn('relative flex w-full items-center', className)}>
        <input
          type="range"
          min={min}
          max={max}
          step={step}
          value={value}
          onChange={handleChange}
          className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer accent-primary"
          ref={ref}
          {...props}
        />
        <div className="flex justify-between text-xs text-gray-500 mt-1">
          <span>{min}</span>
          <span>{max}</span>
        </div>
      </div>
    );
  }
);
Slider.displayName = 'Slider';

export { Slider };

