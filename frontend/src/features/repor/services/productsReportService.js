import apiClient from '@/shared/services/apiClient.js';

const URI = '/api/reports/products';

export const getMostSoldProducts = async (params = {}) => {
  const {
    startDate,
    endDate,
    page = 0,
    size = 3,
  } = params;

  const queryParams = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
  });

  if (startDate) {
    queryParams.append('startDate', startDate);
  }

  if (endDate) {
    queryParams.append('endDate', endDate);
  }

  const { data } = await apiClient.get(`${URI}/most-sold?${queryParams.toString()}`);
  return data;
};

export const getTopFiveProducts = async (params = {}) => {
  const {
    startDate,
    endDate,
    page = 0,
    size = 5,
  } = params;

  const queryParams = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
  });

  if (startDate) {
    queryParams.append('startDate', startDate);
  }

  if (endDate) {
    queryParams.append('endDate', endDate);
  }

  const { data } = await apiClient.get(`${URI}/most-sold?${queryParams.toString()}`);
  return data;
};

