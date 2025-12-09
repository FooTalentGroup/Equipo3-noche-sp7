import { useQuery } from "@tanstack/react-query";
import { formatDates } from "../formatDates";
import { useProductsReport } from "../contexts/ProductsReportContext";
import { getTopFiveProducts } from "../services/productsReportService";
export function useTopFiveProducts() {
  const { startDate, endDate } = useProductsReport();

  const { formattedStart, formattedEnd } = formatDates(startDate, endDate);

  const query = useQuery({
    queryKey: ["top-five-products", formattedStart, formattedEnd],
    queryFn: () =>
      getTopFiveProducts({
        startDate: formattedStart,
        endDate: formattedEnd,
      }),
    enabled: false,
  });

  return {
    ...query,
    fetch: query.refetch,
  };
}
