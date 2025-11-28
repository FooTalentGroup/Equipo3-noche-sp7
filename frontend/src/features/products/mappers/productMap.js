export function productMapper(product) {
  return {
    id: product.id,
    name: product.name,
    price: product.price,
    descuento: product.discount || 0,
    stock_actual: product.currentStock ?? 0,
    currentStock: product.currentStock ?? 0,
    min_stock: product.minStock ?? 0,
    minStock: product.minStock ?? 0,
    category: typeof product.category === 'string' ? product.category : (product.category?.name || ""),
    categoryObj: product.category,
    proveedor: product.supplier?.name || "",
    photoUrl: product.photoUrl || "",
    isAvailable: product.isAvailable ?? true,
  };
}
