import apiClient from '@/shared/services/apiClient.js';

export const getProducts = async (params = {}) => {
  const {
    page = 0,
    size = 20,
    sort,
    q,
    categoryId,
    lowStock,
    includeInactive,
    deleted,
    name,
    category,
    minPrice,
    maxPrice,
  } = params;
  
  const queryParams = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
  });
  
  if (sort && Array.isArray(sort)) {
    sort.forEach((s) => queryParams.append('sort', s));
  } else if (sort) {
    queryParams.append('sort', sort);
  }

  if (q) {
    queryParams.append('q', q);
  }

  if (categoryId) {
    queryParams.append('categoryId', categoryId);
  }

  if (lowStock !== undefined && lowStock !== null) {
    queryParams.append('lowStock', lowStock.toString());
  }

  if (includeInactive !== undefined && includeInactive !== null) {
    queryParams.append('includeInactive', includeInactive.toString());
  }

  if (deleted !== undefined && deleted !== null) {
    queryParams.append('deleted', deleted.toString());
  }

  // Legacy params (for backward compatibility)
  if (name) {
    queryParams.append('name', name);
  }

  if (category && Array.isArray(category) && category.length > 0) {
    category.forEach((c) => queryParams.append('category', c));
  } else if (category) {
    queryParams.append('category', category);
  }

  if (minPrice !== undefined && minPrice !== null) {
    queryParams.append('minPrice', minPrice.toString());
  }

  if (maxPrice !== undefined && maxPrice !== null) {
    queryParams.append('maxPrice', maxPrice.toString());
  }

  const { data } = await apiClient.get(`/api/products?${queryParams.toString()}`);
  return data;
};

export const getProductById = async (id) => {
  const { data } = await apiClient.get(`/api/products/${id}`);
  return data;
};

export const createProduct = async (productData) => {
  const { data } = await apiClient.post('/api/products', productData);
  return data;
};

export const updateProduct = async (id, productData) => {
  const { data } = await apiClient.put(`/api/products/${id}`, productData);
  return data;
};

export const deleteProduct = async (id) => {
  const { data } = await apiClient.delete(`/api/products/${id}`);
  return data;
};

