import { UserPlus, UserCheck, AlertCircle } from "lucide-react";
import { Button } from "@/shared/components/ui/button";
import RegisterCustomerPopup from "@/features/customers/components/RegisterCustomerPopup";
import { CustomerSearchInput } from './CustomerSearchInput';
import { CustomerSearchResults } from './CustomerSearchResults';
import { createCustomer } from '@/features/customers/services/customerService';
import { useSalesCustomerSearch } from '../hooks/useSalesCustomerSearch';
import { useEffect, useState } from 'react';
import { Alert, AlertTitle, AlertDescription } from "@/shared/components/ui/alert";

export function CustomerSection({ preSelectedCustomer, onCustomerSelected, onCustomerChange, disableRemove = false }) {
  const [showRegisterPopup, setShowRegisterPopup] = useState(false);
  const [isLocked, setIsLocked] = useState(false)

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

  useEffect(() => {
    if (preSelectedCustomer) {
      internalSelectCustomer(preSelectedCustomer);
      setCustomerQuery(preSelectedCustomer.name || '');
      setIsLocked(disableRemove);
    }
  }, [preSelectedCustomer, disableRemove]);

  useEffect(() => {
    if (onCustomerChange) {
      onCustomerChange(selectedCustomer);
    }
  }, [selectedCustomer, onCustomerChange]);

  const handleSelectCustomer = (customer) => {
    internalSelectCustomer(customer);
    if (onCustomerSelected) {
      onCustomerSelected(customer);
    }
    setIsLocked(true);
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
    setIsLocked(true);
  };

  const handleClearCustomer = () => {
    clearCustomer();
    if (onCustomerSelected) {
      onCustomerSelected(null);
    }
    setIsLocked(false);
  };
  const handleChangeQuery = (value) => {
    if (!isLocked && !disableRemove) {
      setCustomerQuery(value);
    }
  };


  return (
    <section className="mb-6">
      <h2 className="text-base font-medium text-foreground mb-3">Cliente</h2>

      <div className="flex flex-col md:flex-row gap-8 items-start justify-between">
        <div className="flex flex-col gap-4">
          <div className="relative">
            <CustomerSearchInput
              value={customerQuery}
              onChange={handleChangeQuery}
              onClear={handleClearCustomer}
              placeholder="Buscar clientes"
              className="w-[432px] h-[36px]"
              disableClear={isLocked || disableRemove}
            />

            <CustomerSearchResults
              customers={customers}
              loading={loadingCustomers}
              onSelect={handleSelectCustomer}
              show={showCustomerResults && !selectedCustomer && !isLocked}
            />
          </div>

          <div className="flex space-x-6">
            <Button
              onClick={handleConsumidorFinal}
              className="btn-standard bg-btn-primary hover:bg-btn-primary/90 text-white disabled:text-muted-foreground disabled:bg-secondary disabled:cursor-not-allowed disabled:shadow-none"
              disabled={selectedCustomer !== null || isLocked}
            >
              <UserCheck className="h-4 w-4" />
              <span>Consumidor final</span>
            </Button>

            <Button
              onClick={() => setShowRegisterPopup(true)}
              className="btn-standard bg-btn-primary hover:bg-btn-primary/90 text-white disabled:text-muted-foreground disabled:bg-secondary disabled:cursor-not-allowed disabled:shadow-none"
              disabled={selectedCustomer !== null || isLocked}
            >
              <UserPlus className="h-4 w-4" />
              <span>Nuevo cliente</span>
            </Button>
          </div>
        </div>

        {!selectedCustomer && (
          <Alert variant="default" className="flex-1 bg-transparent max-w-[432px] border-red-200 text-red-800">
            <AlertCircle className="h-4 w-4" />
            <div className="flex flex-col gap-1">
              <AlertTitle className="text-red-700 font-medium">Falta cargar cliente</AlertTitle>
              <AlertDescription className="text-red-600/90 text-xs">
                Debes seleccionar un cliente de tu lista, crear un nuevo cliente o consumidor final antes de confirmar la venta.
              </AlertDescription>
            </div>
          </Alert>
        )}
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
