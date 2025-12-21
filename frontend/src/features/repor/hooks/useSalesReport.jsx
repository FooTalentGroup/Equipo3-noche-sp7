import { useState, useCallback } from "react";
import { getSalesReport } from "../services/salesReportService";

export const useSalesReport = () => {
  const [data, setData] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetch = useCallback(async (startDate, endDate, productName = null) => {
    if (!startDate || !endDate) {
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      const response = await getSalesReport(startDate, endDate, productName);
      if (response.success) {
        setData(response.data);
      } else {
        setError(response.message || "Error al cargar el reporte");
      }
    } catch (err) {
      setError(err.message || "Error al cargar el reporte");
      setData(null);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const reset = useCallback(() => {
    setData(null);
    setError(null);
  }, []);

  return {
    data,
    isLoading,
    error,
    fetch,
    reset,
  };
};
