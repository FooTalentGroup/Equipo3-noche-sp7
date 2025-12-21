import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { formatDates } from "../formatDates";
import { useProductsReport } from "../contexts/ProductsReportContext";
import { getMostSoldProducts } from "../services/productsReportService";

export function useMostSoldProducts() {
  const { startDate, endDate } = useProductsReport();

  const { formattedStart, formattedEnd } = formatDates(startDate, endDate);

  const [page, setPage] = useState(0);

  const query = useQuery({
    queryKey: ["most-sold-products", formattedStart, formattedEnd, page],
    queryFn: () =>
      getMostSoldProducts({
        startDate: formattedStart,
        endDate: formattedEnd,
        page,
        size: 3,
      }),
    enabled: false,
    keepPreviousData: true,
  });

  const data = query.data?.data;

  return {
    content: data?.content ?? [],
    pagination: {
      page: data?.number ?? 0,
      totalPages: data?.totalPages ?? 0,
      totalElements: data?.totalElements ?? 0,
      first: data?.first ?? true,
      last: data?.last ?? true,
    },

    page,
    setPage,

    ...query,
    fetch: query.refetch,
  };
}
