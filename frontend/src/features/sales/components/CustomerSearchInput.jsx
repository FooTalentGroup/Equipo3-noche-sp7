import { Search, X } from 'lucide-react';

export function CustomerSearchInput({ 
  value, 
  onChange, 
  onClear,
  placeholder = "Buscar clientes",
  className = ""
}) {
    const handleClear = () => {
    if (onClear) {
      onClear(); 
    } else {
      onChange('');
    }
  };
  return (
    <div className={`relative ${className}`}>
      <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
      <input
        type="text"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        className="w-full border border border-border bg-background rounded-md pl-10 pr-10 py-2 text-sm transition"
      />
      {value && (
        <button
          onClick={handleClear}
          className="absolute right-3 top-1/2 -translate-y-1/2"
        >
          <X className="h-5 w-5" />
        </button>
      )}
    </div>
  );
}