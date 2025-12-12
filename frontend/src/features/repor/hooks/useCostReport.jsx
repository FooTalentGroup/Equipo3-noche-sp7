import { useState, useCallback } from 'react';
import { getCostReport } from '../services/productsReportService';

export function useCostReport() {
    const [data, setData] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetch = useCallback(async (year, productName) => {
        if (!year || !productName) {
            setData([]);
            return;
        }

        setIsLoading(true);
        setError(null);

        try {
            const response = await getCostReport(year, productName);
            setData(response?.data || []);
        } catch (err) {
            console.error('Error fetching cost report:', err);
            setError(err);
            setData([]);
        } finally {
            setIsLoading(false);
        }
    }, []);

    return { data, isLoading, error, fetch };
}
