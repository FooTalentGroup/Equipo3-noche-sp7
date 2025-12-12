import React, { useState, useEffect, useCallback, useRef } from 'react';
import { Search, Calendar, FileText, LoaderCircle } from 'lucide-react';
import { Button } from '@/shared/components/ui/button';
import { Dialog, DialogContent } from '@/shared/components/ui/dialog';
import ChartSalesDaily from './charts/ChartSalesDaily';
import ChartPaymentMethods from './charts/ChartPaymentMethods';
import { useSalesReport } from '../hooks/useSalesReport';
import { useProductSearch } from '../hooks/useProductSearch';
import { Calendar as CalendarComponent } from '@/shared/components/ui/calendar';
import { ReportExportModal } from './ReportExportModal';

function formatDateArg(date) {
  return new Intl.DateTimeFormat("es-ES", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  })
    .format(date)
    .replace(".", "");
}

const ReportSales = () => {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [productName, setProductName] = useState('');
  const [showCalendar, setShowCalendar] = useState(false);
  const [isExportModalOpen, setIsExportModalOpen] = useState(false);
  const [reportData, setReportData] = useState(null);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  const { data, isLoading, fetch, reset } = useSalesReport();
  const { query, setQuery, products, isLoading: isSearching, selectedProduct, setSelectedProduct } = useProductSearch();
  const searchRef = useRef(null);

  useEffect(() => {
    if (startDate && endDate) {
      const formatDate = (date) => date.toISOString().split('T')[0];
      fetch(formatDate(startDate), formatDate(endDate), productName || null);
    }
  }, [startDate, endDate, productName, fetch]);

  const handleInputChange = (e) => {
    const value = e.target.value;
    setQuery(value);
    setIsDropdownOpen(true);
    if (!value) {
      setSelectedProduct(null);
      setProductName('');
    }
  };

  const handleProductSelect = (product) => {
    setSelectedProduct(product);
    setQuery(product.name);
    setProductName(product.name);
    setIsDropdownOpen(false);
  };

  useEffect(() => {
    function handleClickOutside(event) {
      if (searchRef.current && !searchRef.current.contains(event.target)) {
        setIsDropdownOpen(false);
      }
    }
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleDateSelect = (range) => {
    if (range?.from) {
      setStartDate(range.from);
    }
    if (range?.to) {
      setEndDate(range.to);
    }
  };

  const formatCurrency = (value) => {
    if (value >= 1000000) {
      return `$${(value / 1000000).toFixed(1)}M`;
    } else if (value >= 1000) {
      return `$${new Intl.NumberFormat('es-ES').format(value)}`;
    }
    return `$${value}`;
  };

  const getPercentageColor = (value) => {
    if (value > 0) return 'bg-green-100 text-green-700';
    if (value < 0) return 'bg-red-100 text-red-700';
    return 'bg-gray-100 text-gray-700';
  };

  const formatDateRange = () => {
    if (!startDate || !endDate) return 'Elegir fechas';
    const formatDate = (date) => date.toLocaleDateString('es-ES', { day: '2-digit', month: 'short', year: 'numeric' });
    return `${formatDate(startDate)} - ${formatDate(endDate)}`;
  };

  useEffect(() => {
    if (data) {
      setReportData({
        title: 'Reporte de ventas',
        dateRange: formatDateRange(),
        chartComponent: (
          <div style={{ height: '250px' }}>
            <ChartSalesDaily dailySales={data.dailySales || []} />
          </div>
        ),
        tableHeaders: ['Fecha', 'Monto Total'],
        tableRows: (data.dailySales || []).map(item => [
          new Date(item.date).toLocaleDateString('es-ES', { day: '2-digit', month: 'short', year: 'numeric' }),
          `$${new Intl.NumberFormat('es-ES').format(item.totalAmount)}`
        ]),
        endComponent: (
          <div className="flex justify-center" style={{ height: '200px' }}>
            <div className="w-64">
              <ChartPaymentMethods
                paymentMethods={data.paymentMethodDistribution || []}
                totalAmount={data.metrics?.totalRevenue || 0}
              />
            </div>
          </div>
        )
      });
    }
  }, [data, startDate, endDate]);

  return (
    <div className="mt-4">
      <div className="flex justify-between items-start mb-6">
        <div>
          <h2 className="text-2xl font-bold text-stokia-neutral-900">Reporte de ventas</h2>
          <p className="text-stokia-neutral-500 mt-1">
            Analiza los ingresos, la cantidad de ventas y los métodos de pago más utilizados en tu negocio.
          </p>
        </div>
      </div>

      <div className="flex items-center gap-4 mb-6">
        <div className="flex-1 max-w-sm">
          <label className="block text-sm font-medium text-stokia-neutral-700 mb-2">Producto</label>
          <div className="relative" ref={searchRef}>
            <div className="relative">
              <input
                type="text"
                value={query}
                onChange={handleInputChange}
                onFocus={() => setIsDropdownOpen(true)}
                placeholder="Buscar producto"
                className="w-full px-4 py-2 pr-10 border border-stokia-neutral-300 rounded-md focus:outline-none focus:ring-2 focus:ring-stokia-primary-600"
              />
              <Search className="absolute right-3 top-1/2 -translate-y-1/2 h-4 w-4 text-stokia-neutral-500" />
            </div>

            {isDropdownOpen && query && products.length > 0 && (
              <div className="absolute z-50 w-full mt-1 bg-white border border-stokia-neutral-200 rounded-md shadow-lg max-h-60 overflow-auto">
                {products.map((product) => (
                  <button
                    key={product.id}
                    onClick={() => handleProductSelect(product)}
                    className="w-full px-4 py-2 text-left hover:bg-stokia-neutral-100 flex flex-col"
                  >
                    <span className="font-medium text-sm">{product.name}</span>
                    <span className="text-xs text-stokia-neutral-500">{product.category?.name}</span>
                  </button>
                ))}
              </div>
            )}

            {isSearching && (
              <div className="absolute z-50 w-full mt-1 bg-white border border-stokia-neutral-200 rounded-md shadow-lg p-4 text-center text-sm text-stokia-neutral-500">
                Buscando...
              </div>
            )}
          </div>
        </div>
        <div>
          <label className="block text-sm font-medium text-stokia-neutral-700 mb-2">Sección de periodo</label>
          <Button
            variant="outline"
            onClick={() => setShowCalendar(true)}
            className="flex items-center gap-2 px-4 py-2 bg-stokia-primary-600 text-white hover:bg-stokia-primary-700"
          >
            <Calendar className="h-4 w-4" />
            {formatDateRange()}
          </Button>
        </div>

        <div className="self-end">
          <Button
            onClick={() => setIsExportModalOpen(true)}
            disabled={!data}
            className="flex items-center gap-2 bg-stokia-primary-600 text-white hover:bg-stokia-primary-700 disabled:opacity-50"
          >
            <FileText className="h-4 w-4" />
            Ver reporte
          </Button>
        </div>
      </div>

      <Dialog open={showCalendar} onOpenChange={setShowCalendar}>
        <DialogContent className="max-h-[90vh] overflow-y-auto p-9 flex flex-col gap-4 max-w-lg bg-white border-0 items-center">
          <div className="flex justify-between w-full">
            <h4 className="text-xl">Selección de fechas</h4>
            <p className="flex items-center gap-1.5 text-sm">
              <Calendar className="h-4 w-4" />
              {startDate && endDate
                ? `${formatDateArg(startDate)} - ${formatDateArg(endDate)}`
                : 'Sin período seleccionado'}
            </p>
          </div>

          <CalendarComponent
            mode="range"
            className="[&_table]:border! [&_table]:border-separate [&_table]:border-stokia-neutral-300 [&_table]:p-4! [&_table]:rounded-lg!"
            defaultMonth={startDate || new Date()}
            selected={{ from: startDate, to: endDate }}
            onSelect={(range) => {
              if (!range) return;
              if (range.from) setStartDate(range.from);
              if (range.to) setEndDate(range.to);
            }}
            numberOfMonths={1}
            captionLayout="dropdown"
            classNames={{
              button_previous: 'hidden', button_next: 'hidden',
              dropdowns: 'w-full flex items-center text-sm font-medium h-(--cell-size) gap-1.5',
              week: 'flex w-full mt-2 gap-2',
              month_caption: "",
              dropdown_root: "px-3 relative has-focus:border-ring border border-stokia-neutral-300 shadow-xs has-focus:ring-ring/50 has-focus:ring-[3px] rounded-md",
            }}
          />

          <div className="flex gap-9">
            <Button variant="secondary" onClick={() => setShowCalendar(false)}>
              Cancelar
            </Button>
            <Button
              variant="stokia"
              onClick={() => setShowCalendar(false)}
            >
              Aplicar
            </Button>
          </div>
        </DialogContent>
      </Dialog>

      {isLoading && (
        <div className="flex flex-col items-center justify-center py-20">
          <LoaderCircle className="h-16 w-16 text-stokia-primary-600 animate-spin" />
          <span className="text-sm text-stokia-primary-600 mt-2">Cargando reporte...</span>
        </div>
      )}

      {!isLoading && !data && (
        <div className="flex flex-col items-center justify-center py-20 text-stokia-neutral-500">
          <FileText className="h-16 w-16 mb-4" />
          <p>Selecciona un rango de fechas para ver el reporte de ventas</p>
        </div>
      )}

      {!isLoading && data && (
        <>
          <div className="grid grid-cols-3 gap-6 mb-6">
            <div className="bg-white rounded-xl border border-stokia-neutral-200 p-6 shadow-sm">
              <div className="flex justify-between items-start mb-2">
                <span className="text-stokia-neutral-600 text-sm font-medium">Ingreso total por ventas</span>
                <span className={`px-2 py-1 rounded text-xs font-medium ${getPercentageColor(data.metrics?.revenueChangePercentage)}`}>
                  {data.metrics?.revenueChangePercentage > 0 ? '+' : ''}{data.metrics?.revenueChangePercentage || 0}%
                </span>
              </div>
              <p className="text-3xl font-bold text-stokia-neutral-900">
                {formatCurrency(data.metrics?.totalRevenue || 0)}
              </p>
            </div>

            <div className="bg-white rounded-xl border border-stokia-neutral-200 p-6 shadow-sm">
              <div className="flex justify-between items-start mb-2">
                <span className="text-stokia-neutral-600 text-sm font-medium">Cantidad ventas</span>
                <span className={`px-2 py-1 rounded text-xs font-medium ${getPercentageColor(data.metrics?.ordersChangePercentage)}`}>
                  {data.metrics?.ordersChangePercentage > 0 ? '+' : ''}{data.metrics?.ordersChangePercentage || 0}%
                </span>
              </div>
              <p className="text-3xl font-bold text-stokia-neutral-900">
                {data.metrics?.totalOrders || 0}
              </p>
            </div>

            <div className="bg-white rounded-xl border border-stokia-neutral-200 p-6 shadow-sm">
              <div className="flex justify-between items-start mb-2">
                <span className="text-stokia-neutral-600 text-sm font-medium">Ticket promedio</span>
                <span className={`px-2 py-1 rounded text-xs font-medium ${getPercentageColor(data.metrics?.averageTicketChangePercentage)}`}>
                  {data.metrics?.averageTicketChangePercentage > 0 ? '+' : ''}{data.metrics?.averageTicketChangePercentage || 0}%
                </span>
              </div>
              <p className="text-3xl font-bold text-stokia-neutral-900">
                {formatCurrency(data.metrics?.averageTicket || 0)}
              </p>
            </div>
          </div>

          <div className="grid grid-cols-3 gap-6">
            <div className="col-span-2 bg-white rounded-xl border border-stokia-neutral-200 p-6 shadow-sm" style={{ height: '350px' }}>
              <ChartSalesDaily dailySales={data.dailySales || []} />
            </div>
            <div className="bg-white rounded-xl border border-stokia-neutral-200 p-6 shadow-sm" style={{ height: '350px' }}>
              <ChartPaymentMethods
                paymentMethods={data.paymentMethodDistribution || []}
                totalAmount={data.metrics?.totalRevenue || 0}
              />
            </div>
          </div>
        </>
      )}

      <ReportExportModal
        isOpen={isExportModalOpen}
        onClose={() => setIsExportModalOpen(false)}
        reportData={reportData}
      />
    </div>
  );
};

export default ReportSales;
