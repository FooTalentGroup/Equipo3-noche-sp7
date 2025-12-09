import React, { useState } from 'react';
import { LoaderCircle } from "lucide-react";
import { Pagination } from "@/shared/components/ui/pagination";
import ChartPricing from './charts/ChartPricing';

const MOCK_PRICING_DATA = [
  {
    id: 1,
    month: "Enero",
    unitsSold: 8,
    avgUnitTestPrice: 150.00,
    avgUnitCost: 37.00,
    avgTotalCost: 296.00,
    costVar: 0,
  },
  {
    id: 2,
    month: "Febrero",
    unitsSold: 10,
    avgUnitTestPrice: 150.00,
    avgUnitCost: 44.00,
    avgTotalCost: 440.00,
    costVar: 18.9,
  },
  {
    id: 3,
    month: "Marzo",
    unitsSold: 6,
    avgUnitTestPrice: 145.00,
    avgUnitCost: 42.00,
    avgTotalCost: 252.00,
    costVar: -4.5,
  },
  {
    id: 4,
    month: "Abril",
    unitsSold: 12,
    avgUnitTestPrice: 155.00,
    avgUnitCost: 40.00,
    avgTotalCost: 480.00,
    costVar: -5.0,
  },
  {
    id: 5,
    month: "Mayo",
    unitsSold: 15,
    avgUnitTestPrice: 160.00,
    avgUnitCost: 38.00,
    avgTotalCost: 570.00,
    costVar: -5.0,
  },
  {
    id: 6,
    month: "Junio",
    unitsSold: 11,
    avgUnitTestPrice: 155.00,
    avgUnitCost: 35.00,
    avgTotalCost: 385.00,
    costVar: -7.9,
  },
  {
    id: 7,
    month: "Julio",
    unitsSold: 9,
    avgUnitTestPrice: 150.00,
    avgUnitCost: 30.00,
    avgTotalCost: 270.00,
    costVar: -14.3,
  },
  {
    id: 8,
    month: "Agosto",
    unitsSold: 13,
    avgUnitTestPrice: 152.00,
    avgUnitCost: 32.00,
    avgTotalCost: 416.00,
    costVar: 6.7,
  },
  {
    id: 9,
    month: "Septiembre",
    unitsSold: 14,
    avgUnitTestPrice: 158.00,
    avgUnitCost: 38.00,
    avgTotalCost: 532.00,
    costVar: 18.8,
  },
  {
    id: 10,
    month: "Octubre",
    unitsSold: 16,
    avgUnitTestPrice: 160.00,
    avgUnitCost: 40.00,
    avgTotalCost: 640.00,
    costVar: 5.3,
  },
  {
    id: 11,
    month: "Noviembre",
    unitsSold: 18,
    avgUnitTestPrice: 165.00,
    avgUnitCost: 41.00,
    avgTotalCost: 738.00,
    costVar: 2.5,
  },
  {
    id: 12,
    month: "Diciembre",
    unitsSold: 22,
    avgUnitTestPrice: 170.00,
    avgUnitCost: 41.00,
    avgTotalCost: 902.00,
    costVar: 0,
  },
];

const PricingReport = () => {
  const [loading] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);

  const pageSize = 3;
  const totalElements = MOCK_PRICING_DATA.length;
  const totalPages = Math.ceil(totalElements / pageSize);

  const paginatedData = MOCK_PRICING_DATA.slice(
    currentPage * pageSize,
    (currentPage + 1) * pageSize
  );

  const handlePageChange = (newPage) => {
    setCurrentPage(newPage);
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(value);
  };

  const getVarColor = (val) => {
    if (val > 0) return 'text-green-600';
    if (val < 0) return 'text-red-500';
    return 'text-stokia-neutral-950';
  };

  const chartData = [37, 44, 42, 40, 38, 35, 30, 30, 36, 39, 40, 40];

  return (
    <>
      <div className="mb-10 mt-4 w-full p-6 bg-white rounded-xl shadow-sm border border-stokia-neutral-200" style={{ height: '350px' }}>
        <ChartPricing dataPoints={chartData} />
      </div>
      <section>
        <div className={`relative overflow-x-auto ${loading ? 'h-[400px] overflow-hidden' : ''} flex-1`}>
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
                <tr key={row.id} className="hover:bg-stokia-neutral-100 transition-colors [&_td]:text-stokia-neutral-950 [&_td]:text-sm [&_td]:px-6 [&_td]:py-4">
                  <td className="text-left pl-8 font-medium">{row.month}</td>

                  <td>{row.unitsSold}</td>

                  <td>{formatCurrency(row.avgUnitTestPrice)}</td>

                  <td>{formatCurrency(row.avgUnitCost)}</td>

                  <td>{formatCurrency(row.avgTotalCost)}</td>

                  <td className={`font-medium ${getVarColor(row.costVar)}`}>
                    {row.costVar > 0 ? '+' : ''}{row.costVar}%
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {loading && (
            <div className="flex flex-col items-center justify-center absolute inset-0 bg-stokia-neutral-50/90 z-20">
              <LoaderCircle className="h-16 w-16 text-stokia-primary-600 animate-spin" />
              <span className="text-sm text-stokia-primary-600 mt-2">
                Cargando datos...
              </span>
            </div>
          )}
        </div>

        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={handlePageChange}
          totalElements={totalElements}
          pageSize={pageSize}
          showInfo={true}
          className="mt-4"
        />
      </section>
    </>
  );
};

export default PricingReport;
