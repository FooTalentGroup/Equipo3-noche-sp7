import { UserPlus, UserCheck } from "lucide-react";
import { Button } from "@/shared/components/ui/button";
import RegisterCustomerPopup from "@/features/customers/components/RegisterCustomerPopup";
import { CustomerSearchInput } from "./CustomerSearchInput";
import { CustomerSearchResults } from "./CustomerSearchResults";
import { createCustomer } from "@/features/customers/services/customerService";
import { useSalesCustomerSearch } from "../hooks/useSalesCustomerSearch";
import { useState } from "react";

export function CustomerSection({ onCustomerSelected }) {
  const [showRegisterPopup, setShowRegisterPopup] = useState(false);

  const {
    searchQuery: customerQuery,
    setSearchQuery: setCustomerQuery,
    customers,
    loading: loadingCustomers,
    showResults: showCustomerResults,
    selectedCustomer,
    selectCustomer: internalSelectCustomer,
    selectConsumidorFinal: internalSelectConsumidorFinal,
    clearCustomer,
  } = useSalesCustomerSearch();

  const handleSelectCustomer = (customer) => {
    internalSelectCustomer(customer);
    if (onCustomerSelected) {
      onCustomerSelected(customer);
    }
  };

  const handleSaveCustomer = async (customerData) => {
    try {
      const mappedData = {
        name: customerData.nombre,
        phone: customerData.telefono,
        email: customerData.email,
        isFrequent: customerData.joined,
      };

      const savedCustomer = await createCustomer(mappedData);
      handleSelectCustomer(savedCustomer);
      setShowRegisterPopup(false);

      return savedCustomer;
    } catch (error) {
      console.error("Error al guardar el cliente:", error);
      throw error;
    }
  };

  const handleConsumidorFinal = () => {
    internalSelectConsumidorFinal();
    if (onCustomerSelected) {
      onCustomerSelected({ id: "final", name: "Consumidor Final" });
    }
  };

  const handleClearCustomer = () => {
    clearCustomer();
    if (onCustomerSelected) {
      onCustomerSelected(null);
    }
  };

  return (
    <section className="mb-6">
      <h2 className="text-base font-medium text-foreground mb-3">Cliente</h2>

      <div className="flex flex-col gap-4">
        <div className="relative">
          <CustomerSearchInput
            value={customerQuery}
            onChange={setCustomerQuery}
            onClear={handleClearCustomer}
            placeholder="Buscar clientes"
            className="w-[432px] h-[36px]"
          />

          <CustomerSearchResults
            customers={customers}
            loading={loadingCustomers}
            onSelect={handleSelectCustomer}
            show={showCustomerResults && !selectedCustomer}
          />
        </div>

        <div className="flex space-x-6">
          <Button
            onClick={handleConsumidorFinal}
            className="btn-standard bg-btn-primary hover:bg-btn-primary/90 text-white disabled:text-muted-foreground disabled:bg-secondary disabled:cursor-not-allowed disabled:shadow-none"
            disabled={selectedCustomer !== null}
          >
            <UserCheck className="h-4 w-4" />
            <span>Consumidor final</span>
          </Button>

          <Button
            onClick={() => setShowRegisterPopup(true)}
            className="btn-standard bg-btn-primary hover:bg-btn-primary/90 text-white disabled:text-muted-foreground disabled:bg-secondary disabled:cursor-not-allowed disabled:shadow-none"
            disabled={selectedCustomer !== null}
          >
            <UserPlus className="h-4 w-4" />
            <span>Nuevo cliente</span>
          </Button>
        </div>
      </div>

      {showRegisterPopup && (
        <RegisterCustomerPopup
          open={showRegisterPopup}
          onClose={() => setShowRegisterPopup(false)}
          onSave={handleSaveCustomer}
        />
      )}
    </section>
  );
}
