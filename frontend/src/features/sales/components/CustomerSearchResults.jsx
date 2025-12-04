import { Loader } from 'lucide-react';

export function CustomerSearchResults({ customers, loading, onSelect, show }) {
  if (!show) return null;
  const containerClasses = "absolute top-full mt-1 w-[432px] bg-background border border-border rounded-md shadow-lg z-50"

  if (loading) {
    return (
      <div className={`${containerClasses} p-4`}>
        <div className="flex items-center justify-center">
          <Loader className="h-5 w-5 animate-spin text-muted-foreground" />
        </div>
      </div>
    );
  }

  if (customers.length === 0) {
    return (
      <div className={`${containerClasses} p-4`}>
        <p className="text-sm text-muted-foreground">No se encontraron clientes</p>
      </div>
    );
  }

  return (
    <div className={`${containerClasses} max-h-60 overflow-y-auto`}>
      {customers.map((customer) => (
        <button
          key={customer.id}
          onClick={() => onSelect(customer)}
          className="w-full text-left px-4 py-3 text-foreground hover:bg-bg-accent transition-colors border-b border-gray-200 last:border-b-0"
        >
          <p className="font-normal text-sm ">{customer.name}</p>
        </button>
      ))}
    </div>
  );
}