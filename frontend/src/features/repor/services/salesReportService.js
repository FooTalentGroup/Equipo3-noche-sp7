import apiClient from '@/shared/services/apiClient.js';

const URI = '/api/reports/sales';

export const getSalesReport = async (startDate, endDate, productName = null) => {
    const queryParams = new URLSearchParams();

    if (startDate) {
        queryParams.append('startDate', startDate);
    }

    if (endDate) {
        queryParams.append('endDate', endDate);
    }

    if (productName) {
        queryParams.append('productName', productName);
    }

    const { data } = await apiClient.get(`${URI}?${queryParams.toString()}`);
    return data;
};
