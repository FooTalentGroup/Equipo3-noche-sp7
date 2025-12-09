import { getProducts } from "@/features/products/services/productService";
import { getPendingSales, getSalesLastWeek } from "@/features/sales/services/salesService";
import { useApiQueryFn } from "@/shared/hooks/useApi";

export function useHomeStatistics() {
  const { data: lowStockData, isLoading: isLoadingLowStock } = useApiQueryFn(
    ["low-stock-products"],
    () =>
      getProducts({
        size: 10,
        lowStock: true,
      })
  );

  const { data: salesLastWeek, isLoading: isLoadingSales } = useApiQueryFn(
    ["sales-last-week"],
    () => getSalesLastWeek()
  );

  const { data: pendingSales, isLoading: isLoadingPendingSales } = useApiQueryFn(
    ["pending-sales"],
    () => getPendingSales()
  );

  return {
    isLoading: isLoadingLowStock || isLoadingSales || isLoadingPendingSales,
    products: lowStockData?.totalElements ?? 0,
    sales: salesLastWeek?.totalElements ?? 0,
    pendingSales: pendingSales?.totalElements ?? 0,
  };
}
