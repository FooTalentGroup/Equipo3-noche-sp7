import { Label } from '@/shared/components/ui/label';
import { Select } from '@/shared/components/ui/select';

const SORT_OPTIONS = [
  { value: '', label: 'Sin ordenar' },
  { value: 'price,asc', label: 'Precio: Más barato a más caro' },
  { value: 'price,desc', label: 'Precio: Más caro a más barato' },
  { value: 'name,asc', label: 'Nombre: A-Z' },
  { value: 'name,desc', label: 'Nombre: Z-A' },
];

export function SortSelector({ value, onChange }) {
  return (
    <div className="space-y-2">
      <Label>Ordenar por</Label>
      <Select value={value} onChange={(e) => onChange(e.target.value)}>
        {SORT_OPTIONS.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </Select>
    </div>
  );
}

