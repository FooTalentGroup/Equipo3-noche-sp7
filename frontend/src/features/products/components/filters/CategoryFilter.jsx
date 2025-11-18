import { Label } from '@/shared/components/ui/label';
import { Input } from '@/shared/components/ui/input';

const CATEGORIES = [
  'Bebidas Calientes',
  'Bebidas Frías',
  'Platos Fuertes',
  'Ensaladas',
  'Postres',
  'Aperitivos',
];

export function CategoryFilter({ selectedCategories = [], onChange }) {
  const handleCategoryChange = (category) => {
    const newSelection = selectedCategories.includes(category)
      ? selectedCategories.filter((c) => c !== category)
      : [...selectedCategories, category];
    onChange(newSelection);
  };

  return (
    <div className="space-y-2">
      <Label>Categorías</Label>
      <div className="space-y-2 max-h-48 overflow-y-auto">
        {CATEGORIES.map((category) => {
          const isSelected = selectedCategories.includes(category);
          return (
            <label
              key={category}
              className="flex items-center gap-2 cursor-pointer hover:bg-gray-50 p-2 rounded"
            >
              <input
                type="checkbox"
                checked={isSelected}
                onChange={() => handleCategoryChange(category)}
                className="rounded border-gray-300"
              />
              <span className="text-sm">{category}</span>
            </label>
          );
        })}
      </div>
    </div>
  );
}

