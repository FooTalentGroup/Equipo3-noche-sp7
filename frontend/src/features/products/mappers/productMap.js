export function productMapper(product) {
  return {
    id: product.id,
    name: product.name,
    price: product.price,
    descuento: product.discount || 0,
    stock_actual: product.currentStock,
    min_stock: product.minStock,
    category: product.category?.name || "",
    proveedor: product.supplier?.name || "",
    photoUrl: product.photoUrl || "",
    isAvailable: product.isAvailable,
  };
}
