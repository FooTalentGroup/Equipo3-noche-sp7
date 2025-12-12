import { useState, useCallback } from 'react';
import { getStockReport } from '../services/productsReportService';

export function useStockReport() {
    const [data, setData] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetch = useCallback(async (productName, startDate, endDate) => {
        if (!productName || !startDate || !endDate) {
            setData([]);
            return;
        }

        setIsLoading(true);
        setError(null);

        try {
            const response = await getStockReport(productName, startDate, endDate);
            setData(response?.data || []);
        } catch (err) {
            console.error('Error fetching stock report:', err);
            setError(err);
            setData([]);
        } finally {
            setIsLoading(false);
        }
    }, []);

    return { data, isLoading, error, fetch };
}
