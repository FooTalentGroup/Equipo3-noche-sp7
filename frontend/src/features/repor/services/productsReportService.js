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

export const getCostReport = async (year, productName) => {
  const queryParams = new URLSearchParams();

  if (year) {
    queryParams.append('year', year.toString());
  }

  if (productName) {
    queryParams.append('productName', productName);
  }

  const { data } = await apiClient.get(`${URI}/costs?${queryParams.toString()}`);
  return data;
};

export const searchProducts = async (query) => {
  const queryParams = new URLSearchParams();

  if (query) {
    queryParams.append('q', query);
  }

  const { data } = await apiClient.get(`/api/products?${queryParams.toString()}`);
  return data;
};

export const getStockReport = async (productName, startDate, endDate) => {
  const queryParams = new URLSearchParams();

  if (productName) {
    queryParams.append('productName', productName);
  }

  if (startDate) {
    queryParams.append('startDate', startDate);
  }

  if (endDate) {
    queryParams.append('endDate', endDate);
  }

  const { data } = await apiClient.get(`${URI}/stock?${queryParams.toString()}`);
  return data;
};



