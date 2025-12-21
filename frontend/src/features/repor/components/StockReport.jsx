import { useEffect, useState, useMemo } from "react";
import { LoaderCircle } from "lucide-react";
import { Pagination } from "@/shared/components/ui/pagination";
import ChartStock from "./charts/ChartStock";
import { useStockReport } from "../hooks/useStockReport";
import { useProductsReport } from "../contexts/ProductsReportContext";

const StockReport = () => {
  const { productName, startDate, endDate, setReportData } =
    useProductsReport();
  const { data, isLoading, fetch } = useStockReport();
  const [currentPage, setCurrentPage] = useState(0);

  useEffect(() => {
    if (productName && startDate && endDate) {
      const formattedStartDate = startDate.toISOString().split("T")[0];
      const formattedEndDate = endDate.toISOString().split("T")[0];
      fetch(productName, formattedStartDate, formattedEndDate);
    }
  }, [productName, startDate, endDate, fetch]);

  const pageSize = 3;

  const buildDateRangeDates = (s, e) => {
    const start = new Date(s);
    const end = new Date(e);
    start.setHours(0, 0, 0, 0);
    end.setHours(0, 0, 0, 0);
    const arr = [];
    for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
      arr.push(new Date(d));
    }
    return arr;
  };

  const rowsArray = useMemo(() => {
    if (startDate && endDate) {
      const dateRangeDates = buildDateRangeDates(startDate, endDate);
      return dateRangeDates.map((d) => {
        const key = d.toISOString().split("T")[0];
        const match = data.find(
          (item) => new Date(item.date).toISOString().split("T")[0] === key
        );
        return match
          ? { ...match }
          : {
              date: d.toISOString(),
              initialStock: "-",
              entries: "-",
              exits: "-",
              currentStock: "-",
              stockVariationPercent: 0,
            };
      });
    } else {
      return data;
    }
  }, [data, startDate, endDate]);

  const totalElements = rowsArray?.length || 0;
  const totalPages = Math.ceil(totalElements / pageSize);

  const paginatedData = rowsArray.slice(
    currentPage * pageSize,
    (currentPage + 1) * pageSize
  );

  const displayRows =
    paginatedData.length > 0
      ? paginatedData
      : Array.from({ length: pageSize }, () => null);

  const handlePageChange = (newPage) => {
    setCurrentPage(newPage);
  };

  const getVarColor = (val) => {
    if (val > 0) return "text-green-600";
    if (val < 0) return "text-red-500";
    return "text-stokia-neutral-950";
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("es-ES", { day: "2-digit", month: "short" });
  };

  const isSameDayRange = (s, e) => {
    if (!s || !e) return false;
    const a = new Date(s);
    const b = new Date(e);
    return (
      a.getFullYear() === b.getFullYear() &&
      a.getMonth() === b.getMonth() &&
      a.getDate() === b.getDate()
    );
  };

  const generatePlaceholderLabels = (refDate) => {
    if (
      !startDate ||
      !endDate ||
      (startDate && endDate && isSameDayRange(startDate, endDate))
    ) {
      return Array.from({ length: 11 }, (_, i) => {
        const hour = 8 + i;
        return `${String(hour).padStart(2, "0")}:00`;
      });
    }

    if (startDate && endDate) {
      const s = new Date(startDate);
      const e = new Date(endDate);
      s.setHours(0, 0, 0, 0);
      e.setHours(0, 0, 0, 0);
      const labels = [];
      for (let d = new Date(s); d <= e; d.setDate(d.getDate() + 1)) {
        labels.push(
          new Date(d).toLocaleDateString("es-ES", {
            day: "2-digit",
            month: "short",
          })
        );
      }
      return labels;
    }

    const ref = refDate ? new Date(refDate) : new Date();
    ref.setHours(0, 0, 0, 0);
    const y = ref.getFullYear();
    const m = ref.getMonth();
    const dim = new Date(y, m + 1, 0).getDate();
    return Array.from({ length: dim }, (_, i) => {
      const d = new Date(y, m, i + 1);
      return d.toLocaleDateString("es-ES", { day: "2-digit", month: "short" });
    });
  };

  const isSameDay = startDate && endDate && isSameDayRange(startDate, endDate);
  const { chartLabels, chartData } = useMemo(() => {
    let labels = [];
    let dataPoints = [];

    if (isSameDay) {
      labels = generatePlaceholderLabels(startDate);
      const buckets = Array(labels.length).fill(null);
      if (data && data.length > 0) {
        data.forEach((item) => {
          const dt = new Date(item.date);
          const hour = dt.getHours();
          const idx = hour - 8;
          if (idx >= 0 && idx < buckets.length) {
            buckets[idx] = Number(item.currentStock) || 0;
          }
        });
      }
      const hasAnyValue = buckets.some((v) => v != null);
      if (!hasAnyValue && data && data.length > 0) {
        const lastVal = Number(data[data.length - 1].currentStock) || 0;
        dataPoints = buckets.map(() => lastVal);
      } else {
        dataPoints = buckets.map((v) => (v == null ? 0 : v));
      }
    } else {
      if (startDate && endDate) {
        labels = rowsArray.map((r) => formatDate(r.date));
        dataPoints = rowsArray.map((r) =>
          r.currentStock == null || r.currentStock === "-" ? 0 : r.currentStock
        );
      } else {
        labels =
          data.length > 0
            ? data.map((item) => formatDate(item.date))
            : generatePlaceholderLabels(startDate);
        dataPoints =
          data.length > 0
            ? data.map((item) => item.currentStock)
            : Array(labels.length).fill(0);
      }
    }

    return { chartLabels: labels, chartData: dataPoints };
  }, [data, startDate, endDate, isSameDay, rowsArray]);

  useEffect(() => {
    if (productName && startDate && endDate) {
      const formattedStartDate = formatDate(startDate.toISOString());
      const formattedEndDate = formatDate(endDate.toISOString());

      const sameDayForReport =
        startDate && endDate && isSameDayRange(startDate, endDate);
      const showPlaceholder = !!productName || (startDate && endDate);

      const tableRows = rowsArray.map((row) => [
        formatDate(row.date),
        row.initialStock,
        row.entries,
        row.exits,
        row.currentStock,
        `${row.stockVariationPercent > 0 ? "+" : ""}${
          row.stockVariationPercent
        }%`,
      ]);

      setReportData({
        title: "Reporte de stock",
        dateRange: `${formattedStartDate} - ${formattedEndDate}`,
        chartComponent: (
          <ChartStock
            dataPoints={chartData}
            labels={chartLabels}
            forceHourly={
              rowsArray.length === 0 &&
              (!(startDate && endDate) || sameDayForReport)
            }
            showPlaceholder={showPlaceholder}
          />
        ),
        chartPayload: { dataPoints: chartData, labels: chartLabels },
        tableHeaders: [
          "Período",
          "Stock Inicial",
          "Entradas (+)",
          "Salidas (-)",
          "Stock Actual",
          "Var. Stock (%)",
        ],
        tableRows: tableRows,
      });
    }
  }, [
    productName,
    startDate,
    endDate,
    rowsArray,
    chartData,
    chartLabels,
    setReportData,
  ]);

  return (
    <>
      <div
        className="mb-10 mt-4 w-full p-6 bg-white rounded-xl shadow-sm border border-stokia-neutral-200"
        style={{ height: "350px" }}
      >
        <ChartStock
          dataPoints={chartData}
          labels={chartLabels}
          forceHourly={
            rowsArray.length === 0 && (!(startDate && endDate) || isSameDay)
          }
          showPlaceholder={!!productName || (startDate && endDate)}
        />
      </div>

      <section>
        <div
          className={`relative overflow-x-auto ${
            isLoading ? "h-[400px] overflow-hidden" : ""
          } flex-1`}
        >
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
              {displayRows.map((row, idx) => (
                <tr
                  key={row ? row.date : `empty-${idx}`}
                  className="hover:bg-stokia-neutral-100 transition-colors [&_td]:text-stokia-neutral-950 [&_td]:text-sm [&_td]:px-6 [&_td]:py-4"
                >
                  <td className="text-left pl-8 font-medium">
                    {row ? formatDate(row.date) : "-"}
                  </td>
                  <td
                    style={{
                      color:
                        row && row.initialStock < 0 ? "#ef4444" : undefined,
                    }}
                  >
                    {row ? row.initialStock : "-"}
                  </td>
                  <td
                    style={{
                      color: row && row.entries < 0 ? "#ef4444" : undefined,
                    }}
                  >
                    {row ? row.entries : "-"}
                  </td>
                  <td
                    style={{
                      color: row && row.exits < 0 ? "#ef4444" : undefined,
                    }}
                  >
                    {row ? row.exits : "-"}
                  </td>
                  <td
                    style={{
                      color:
                        row && row.currentStock < 0 ? "#ef4444" : undefined,
                    }}
                  >
                    {row ? row.currentStock : "-"}
                  </td>
                  <td
                    className={`font-medium ${getVarColor(
                      row ? row.stockVariationPercent : 0
                    )}`}
                  >
                    {row
                      ? `${row.stockVariationPercent > 0 ? "+" : ""}${
                          row.stockVariationPercent
                        }%`
                      : "-"}
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
        </div>

        {totalElements > 0 && (
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
