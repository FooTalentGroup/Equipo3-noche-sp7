import React, { useEffect, useState } from "react";
import { LoaderCircle } from "lucide-react";
import BestSellersChart from "./charts/ChartBestSellers";

import { Pagination } from "@/shared/components/ui/pagination";
import { useTopFiveProducts } from "../hooks/useTopFiveSellers";
import { useMostSoldProducts } from "../hooks/useMostSold";
import { useProductsReport } from "../contexts/ProductsReportContext";

const BestSellersReport = () => {
  const {
    data: topFive,
    isLoading: loadingTopFive,
    fetch: fetchTopFive,
  } = useTopFiveProducts();

  const {
    content,
    pagination,
    page,
    setPage,
    isLoading: loadingMostSold,
    fetch: fetchMostSold,
  } = useMostSoldProducts();

  const { setReportData, startDate, endDate, productName } = useProductsReport();
  const [stableProducts, setStableProducts] = useState([]);

  useEffect(() => {
    fetchMostSold();
  }, [page, fetchMostSold]);

  useEffect(() => {
    if (topFive?.data?.content?.length > 0) {
      setStableProducts(topFive.data.content);
    } else {
      setStableProducts([]);
    }
  }, [topFive]);

  useEffect(() => {
    if (content.length > 0) {
      const formatDate = (date) => {
        if (!date) return '';
        return date.toLocaleDateString('es-ES', { day: '2-digit', month: 'short' });
      };
      const dateRange = startDate && endDate
        ? `${formatDate(startDate)} - ${formatDate(endDate)}`
        : 'Todos los periodos';

      const showPlaceholder = !!productName || (startDate && endDate);

      setReportData({
        title: 'Reporte de más vendidos',
        dateRange: dateRange,
        chartComponent: <BestSellersChart products={stableProducts} showPlaceholder={showPlaceholder} />,
        tableHeaders: ['Producto', 'Categoría', 'Precio de Venta', 'Cant. Inicial', 'Cant. Vendida', 'Cant. Actual'],
        tableRows: content.map(product => [
          product.productName,
          product.categoryName,
          `$${product.salePrice.toFixed(2)}`,
          product.initialQuantity,
          product.quantitySold,
          product.currentQuantity
        ])
      });
    }
  }, [content, stableProducts, startDate, endDate, setReportData]);

  return (
    <>
      <div className="mb-10 mt-4 w-full" style={{ height: "300px" }}>
        <h1>Reporte de Ventas</h1>
        <BestSellersChart products={stableProducts} showPlaceholder={!!productName || (startDate && endDate)} />
      </div>

      <section>
        <div
          className={`relative overflow-x-auto ${loadingMostSold ? "h-[400px] overflow-hidden" : ""
            } flex-1`}
        >
          <table className="w-full text-left border-separate border-spacing-0">
            <thead className="text-sm bg-stokia-primary-100 text-stokia-neutral-950 font-normal h-[46px] sticky top-0 z-10">
              <tr className="[&_th]:px-6 [&_th]:py-3">
                <th className="rounded-tl-xl">PRODUCTO</th>
                <th>CATEGORÍA</th>
                <th>PRECIO DE VENTA</th>
                <th>CANT. INICIAL</th>
                <th>CANT. VENDIDA</th>
                <th className="rounded-tr-xl">CANT. ACTUAL</th>
              </tr>
            </thead>

            <tbody className="divide-y divide-stokia-neutral-100">
              {content.map((product) => (
                <tr
                  key={product.productId}
                  className="hover:bg-stokia-neutral-100 transition-colors [&_td]:text-stokia-neutral-950 [&_td]:text-sm [&_td]:px-6 [&_td]:py-4"
                >
                  <td>{product.productName}</td>
                  <td>{product.categoryName}</td>
                  <td>${product.salePrice.toFixed(2)}</td>
                  <td>{product.initialQuantity}</td>
                  <td>{product.quantitySold}</td>
                  <td>{product.currentQuantity}</td>
                </tr>
              ))}
            </tbody>
          </table>

          {loadingMostSold && (
            <div className="flex flex-col items-center justify-center absolute inset-0 bg-stokia-neutral-50/90 z-20">
              <LoaderCircle className="h-16 w-16 text-stokia-primary-600 animate-spin" />
              <span className="text-sm text-stokia-primary-600 mt-2">
                Cargando productos...
              </span>
            </div>
          )}

          {!loadingMostSold && content.length === 0 && (
            <div className="absolute inset-0 flex items-center justify-center">
              <span className="px-6 py-6 text-center text-stokia-neutral-600 text-sm">
                No hay productos para mostrar.
              </span>
            </div>
          )}
        </div>

        <Pagination
          currentPage={pagination.page}
          totalPages={pagination.totalPages}
          onPageChange={(newPage) => setPage(newPage)}
          totalElements={pagination.totalElements}
          pageSize={3}
          showInfo={true}
          className="mt-4"
        />
      </section>
    </>
  );
};

export default BestSellersReport;
