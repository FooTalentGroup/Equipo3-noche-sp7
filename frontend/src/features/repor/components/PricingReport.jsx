import { useEffect, useState } from 'react';
import { LoaderCircle } from "lucide-react";
import { Pagination } from "@/shared/components/ui/pagination";
import ChartPricing from './charts/ChartPricing';
import { useCostReport } from '../hooks/useCostReport';
import { useProductsReport } from '../contexts/ProductsReportContext';

const PricingReport = () => {
  const { year, productName, setReportData } = useProductsReport();
  const { data, isLoading, fetch } = useCostReport();
  const [currentPage, setCurrentPage] = useState(0);

  useEffect(() => {
    if (year && productName) {
      fetch(year, productName);
    }
  }, [year, productName, fetch]);

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

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'USD' }).format(value);
  };

  const getVarColor = (val) => {
    if (val > 0) return 'text-green-600';
    if (val < 0) return 'text-red-500';
    return 'text-stokia-neutral-950';
  };

  useEffect(() => {
    if (data.length > 0 && year && productName) {
      const chartValues = data.map(item => item.avgUnitPrice);

      setReportData({
        title: 'Reporte de costos',
        dateRange: `${productName} - ${year}`,
        chartComponent: <ChartPricing dataPoints={chartValues} />,
        tableHeaders: ['Mes', 'Unid. Vend.', 'Precio Unit. Prom.', 'Costo Unit. Prom.', 'Costo Total Prom.', 'Var. Costos (%) Prom.'],
        tableRows: data.map(row => [
          row.monthName,
          row.unitsSold,
          formatCurrency(row.avgUnitPrice),
          formatCurrency(row.avgUnitCost),
          formatCurrency(row.totalAvgCost),
          `${row.costVariationPercent > 0 ? '+' : ''}${row.costVariationPercent}%`
        ])
      });
    }
  }, [data, year, productName, setReportData]);

  const chartData = data.map(item => item.avgUnitPrice);

  return (
    <>
      <div className="mb-10 mt-4 w-full p-6 bg-white rounded-xl shadow-sm border border-stokia-neutral-200" style={{ height: '350px' }}>
        <ChartPricing dataPoints={chartData} />
      </div>
      <section>
        <div className={`relative overflow-x-auto ${isLoading ? 'h-[400px] overflow-hidden' : ''} flex-1`}>
          <table className="w-full text-center border-separate border-spacing-0">
            <thead className="text-sm bg-stokia-primary-100 text-stokia-neutral-950 font-normal h-[46px] sticky top-0 z-10">
              <tr className="[&_th]:px-6 [&_th]:py-3 uppercase tracking-wider text-xs">
                <th className="rounded-tl-xl text-left pl-8">MES</th>
                <th>UNID. VEND.</th>
                <th>PRECIO UNIT. PROM.</th>
                <th>COSTO UNIT. PROM.</th>
                <th>COSTO TOTAL PROM.</th>
                <th className="rounded-tr-xl">VAR. COSTOS (%) PROM.</th>
              </tr>
            </thead>

            <tbody className="divide-y divide-stokia-neutral-100 bg-white">
              {paginatedData.map((row) => (
                <tr key={row.month} className="hover:bg-stokia-neutral-100 transition-colors [&_td]:text-stokia-neutral-950 [&_td]:text-sm [&_td]:px-6 [&_td]:py-4">
                  <td className="text-left pl-8 font-medium">{row.monthName}</td>

                  <td>{row.unitsSold}</td>

                  <td>{formatCurrency(row.avgUnitPrice)}</td>

                  <td>{formatCurrency(row.avgUnitCost)}</td>

                  <td>{formatCurrency(row.totalAvgCost)}</td>

                  <td className={`font-medium ${getVarColor(row.costVariationPercent)}`}>
                    {row.costVariationPercent > 0 ? '+' : ''}{row.costVariationPercent}%
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
                {productName && year
                  ? 'No hay datos para mostrar con los filtros seleccionados.'
                  : 'Selecciona un producto y año para ver el reporte.'}
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

export default PricingReport;
