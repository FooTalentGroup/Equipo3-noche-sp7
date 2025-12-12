import { useEffect, useState } from 'react';
import { LoaderCircle } from "lucide-react";
import { Pagination } from "@/shared/components/ui/pagination";
import ChartStock from './charts/ChartStock';
import { useStockReport } from '../hooks/useStockReport';
import { useProductsReport } from '../contexts/ProductsReportContext';

const StockReport = () => {
  const { productName, startDate, endDate, setReportData } = useProductsReport();
  const { data, isLoading, fetch } = useStockReport();
  const [currentPage, setCurrentPage] = useState(0);

  useEffect(() => {
    if (productName && startDate && endDate) {
      const formattedStartDate = startDate.toISOString().split('T')[0];
      const formattedEndDate = endDate.toISOString().split('T')[0];
      fetch(productName, formattedStartDate, formattedEndDate);
    }
  }, [productName, startDate, endDate, fetch]);

  const pageSize = 3;
  const totalElements = data?.length || 0;
  const totalPages = Math.ceil(totalElements / pageSize);

  const paginatedData = data.slice(
    currentPage * pageSize,
    (currentPage + 1) * pageSize
  );

  const handlePageChange = (newPage) => {
    setCurrentPage(newPage);
  };

  const getVarColor = (val) => {
    if (val > 0) return 'text-green-600';
    if (val < 0) return 'text-red-500';
    return 'text-stokia-neutral-950';
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', { day: '2-digit', month: 'short' });
  };

  const chartLabels = data.map(item => formatDate(item.date));
  const chartData = data.map(item => item.currentStock);

  useEffect(() => {
    if (data.length > 0 && productName && startDate && endDate) {
      const formattedStartDate = formatDate(startDate.toISOString());
      const formattedEndDate = formatDate(endDate.toISOString());

      setReportData({
        title: 'Reporte de stock',
        dateRange: `${formattedStartDate} - ${formattedEndDate}`,
        chartComponent: <ChartStock dataPoints={chartData} labels={chartLabels} />,
        tableHeaders: ['Período', 'Stock Inicial', 'Entradas (+)', 'Salidas (-)', 'Stock Actual', 'Var. Stock (%)'],
        tableRows: data.map(row => [
          formatDate(row.date),
          row.initialStock,
          row.entries,
          row.exits,
          row.currentStock,
          `${row.stockVariationPercent > 0 ? '+' : ''}${row.stockVariationPercent}%`
        ])
      });
    }
  }, [data, productName, startDate, endDate, setReportData]);

  return (
    <>
      <div className="mb-10 mt-4 w-full p-6 bg-white rounded-xl shadow-sm border border-stokia-neutral-200" style={{ height: '350px' }}>
        {data.length > 0 ? (
          <ChartStock dataPoints={chartData} labels={chartLabels} />
        ) : (
          <div className="flex items-center justify-center h-full text-stokia-neutral-500">
            {productName && startDate && endDate
              ? 'No hay datos para mostrar'
              : 'Selecciona un producto y período para ver el reporte'}
          </div>
        )}
      </div>

      <section>
        <div className={`relative overflow-x-auto ${isLoading ? 'h-[400px] overflow-hidden' : ''} flex-1`}>
          <table className="w-full text-center border-separate border-spacing-0">
            <thead className="text-sm bg-stokia-primary-100 text-stokia-neutral-950 font-normal h-[46px] sticky top-0 z-10">
              <tr className="[&_th]:px-6 [&_th]:py-3 uppercase tracking-wider text-xs">
                <th className="rounded-tl-xl text-left pl-8">PERIODO</th>
                <th>STOCK INICIAL</th>
                <th>ENTRADAS (+)</th>
                <th>SALIDAS (-)</th>
                <th>STOCK ACTUAL</th>
                <th className="rounded-tr-xl">VAR. STOCK (%)</th>
              </tr>
            </thead>

            <tbody className="divide-y divide-stokia-neutral-100 bg-white">
              {paginatedData.map((row) => (
                <tr key={row.date} className="hover:bg-stokia-neutral-100 transition-colors [&_td]:text-stokia-neutral-950 [&_td]:text-sm [&_td]:px-6 [&_td]:py-4">
                  <td className="text-left pl-8 font-medium">{formatDate(row.date)}</td>

                  <td>{row.initialStock}</td>

                  <td>{row.entries}</td>

                  <td>{row.exits}</td>

                  <td>{row.currentStock}</td>

                  <td className={`font-medium ${getVarColor(row.stockVariationPercent)}`}>
                    {row.stockVariationPercent > 0 ? '+' : ''}{row.stockVariationPercent}%
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {isLoading && (
            <div className="flex flex-col items-center justify-center absolute inset-0 bg-stokia-neutral-50/90 z-20">
              <LoaderCircle className="h-16 w-16 text-stokia-primary-600 animate-spin" />
              <span className="text-sm text-stokia-primary-600 mt-2">
                Cargando datos...
              </span>
            </div>
          )}

          {!isLoading && data.length === 0 && (
            <div className="absolute inset-0 flex items-center justify-center">
              <span className="px-6 py-6 text-center text-stokia-neutral-600 text-sm">
                {productName && startDate && endDate
                  ? 'No hay datos para mostrar con los filtros seleccionados.'
                  : 'Selecciona un producto y período para ver el reporte.'}
              </span>
            </div>
          )}
        </div>

        {data.length > 0 && (
          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            onPageChange={handlePageChange}
            totalElements={totalElements}
            pageSize={pageSize}
            showInfo={true}
            className="mt-4"
          />
        )}
      </section>
    </>
  );
};

export default StockReport;
