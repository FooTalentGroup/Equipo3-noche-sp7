import { useEffect, useState } from 'react';
import { getCustomers,getFinalConsumer } from '@/features/customers/services/customerService';

let CACHED_CONSUMIDOR_FINAL = null;

export function useSalesCustomerSearch(delay = 500) {
  const [searchQuery, setSearchQuery] = useState('');
  const [debouncedSearch, setDebouncedSearch] = useState('');
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showResults, setShowResults] = useState(false);
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [loadingConsumidorFinal, setLoadingConsumidorFinal] = useState(false);


  useEffect(() => {
    const id = setTimeout(() => {
      setDebouncedSearch(searchQuery.trim());
    }, delay);
    return () => clearTimeout(id);
  }, [searchQuery, delay]);


  useEffect(() => {
    const fetchCustomers = async () => {
      if (!debouncedSearch || debouncedSearch.length < 2) {
        setCustomers([]);
        setShowResults(false);
        setLoading(false);
        return;
      }

      setLoading(true);
      setShowResults(true);

      try {
        const response = await getCustomers({ name: debouncedSearch });

        const payload = response?.data ?? response;

        const customersArray = Array.isArray(payload?.customers)
          ? payload.customers
          : Array.isArray(payload?.customers?.content)
            ? payload.customers.content
            : Array.isArray(payload?.content)
              ? payload.content
              : Array.isArray(payload)
                ? payload
                : [];

        const mappedAll = customersArray.map((u) => ({
          raw: u,
          id: u.id,
          name: u.name ?? u.nombre ?? '',
          email: u.email ?? u.correo ?? '',
          phone: u.phone ?? u.telefono ?? '',
          isFrequent: u.isFrequent ?? u.esFrecuente ?? false,
          clientStatus: String(u.clientStatus ?? u.status ?? 'ACTIVE').trim().toUpperCase(),
        }));

        const activeClients = mappedAll.filter((c) => c.clientStatus === 'ACTIVE');

        const mapped = activeClients.map((c) => ({
          id: c.id,
          name: c.name,
          email: c.email,
          phone: c.phone,
          isFrequent: c.isFrequent,
        }));

        setCustomers(mapped);
      } catch (error) {
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

 const selectConsumidorFinal = async() => {
    if (CACHED_CONSUMIDOR_FINAL) {
      setSelectedCustomer(CACHED_CONSUMIDOR_FINAL);
      setSearchQuery('Consumidor final');
      setShowResults(false);
      return;
    }

    setLoadingConsumidorFinal(true);
    
    try {
      const response = await getFinalConsumer();
      const consumidorFinalData = response.customer || response.client || response;
      
      if (!consumidorFinalData || !consumidorFinalData.id) {
        throw new Error('No se pudo obtener el consumidor final');
      }

      const consumidorFinal = {
        id: consumidorFinalData.id,
        name: consumidorFinalData.name || 'Consumidor final',
        email: consumidorFinalData.email || null,
        phone: consumidorFinalData.phone || null,
        isConsumidorFinal: true
      };

      CACHED_CONSUMIDOR_FINAL = consumidorFinal;
      
      setSelectedCustomer(consumidorFinal);
      setSearchQuery('Consumidor final');
      setShowResults(false);
      
      console.log('Consumidor Final obtenido:', consumidorFinal);
      
    } catch (error) {
      console.error('Error al obtener Consumidor Final:', error);
      alert('Error al obtener el Consumidor Final. Por favor, intenta nuevamente.');
    } finally {
      setLoadingConsumidorFinal(false);
    }
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
    loadingConsumidorFinal,
  };
}