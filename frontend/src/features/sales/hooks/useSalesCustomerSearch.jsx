import { useEffect, useState } from 'react';
import { getCustomers } from '@/features/customers/services/customerService';


export function useSalesCustomerSearch(delay = 500) {
  const [searchQuery, setSearchQuery] = useState('');
  const [debouncedSearch, setDebouncedSearch] = useState('');
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showResults, setShowResults] = useState(false);
  const [selectedCustomer, setSelectedCustomer] = useState(null);


  useEffect(() => {
    const id = setTimeout(() => {
      setDebouncedSearch(searchQuery.trim());
    }, delay);
    return () => clearTimeout(id);
  }, [searchQuery, delay]);


  useEffect(() => {
    const fetchCustomers = async () => {
      if (debouncedSearch.length < 2) {
        setCustomers([]);
        setShowResults(false);
        return;
      }

      setLoading(true);
      setShowResults(true);

      try {
        const response = await getCustomers({ name: debouncedSearch });
        const customersList = response?.customers?.content || [];
        setCustomers(customersList);
      } catch (error) {
        console.error("Error al buscar clientes:", error);
        setCustomers([]);
      } finally {
        setLoading(false);
      }
    };

    fetchCustomers();
  }, [debouncedSearch]);

  const selectCustomer = (customer) => {
    setSelectedCustomer(customer);
    setSearchQuery(customer.name);
    setShowResults(false);
  };

  const selectConsumidorFinal = () => {
    const consumidorFinal = {
      id: 'consumidor-final',
      name: 'Consumidor final',
      email: null,
      phone: null,
      isConsumidorFinal: true
    };
    setSelectedCustomer(consumidorFinal);
    setSearchQuery('Consumidor final');
    setShowResults(false);
  };

  const clearCustomer = () => {
    setSelectedCustomer(null);
    setSearchQuery('');
    setCustomers([]);
    setShowResults(false);
  };

  return {
    searchQuery,
    setSearchQuery,
    customers,
    loading,
    showResults,
    setShowResults,
    selectedCustomer,
    selectCustomer,
    clearCustomer,
    selectConsumidorFinal,
  };
}