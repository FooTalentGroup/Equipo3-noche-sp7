import { Button } from "@/shared/components/ui/button";
import { Calendar } from "@/shared/components/ui/calendar";
import { Dialog, DialogContent, DialogTitle } from "@/shared/components/ui/dialog";
import { Label } from "@/shared/components/ui/label";
import { NativeSelect } from "@/shared/components/ui/native-select";
import { Description } from "@radix-ui/react-dialog";
import { Calendar1, SearchIcon, X } from "lucide-react";
import { useState } from "react";
import {
  InputGroup,
  InputGroupAddon,
  InputGroupButton,
  InputGroupInput,
} from "@/shared/components/ui/input-group";
import { Outlet, useNavigate, useLocation } from "react-router";
import { useProductsReport } from "../contexts/ProductsReportContext";
import { useMostSoldProducts } from "../hooks/useMostSold";
import { useTopFiveProducts } from "../hooks/useTopFiveSellers";

const TITLE = "Reporte de productos";
const SUB_TITLE =
  "Consulta de un vistazo tus productos más vendidos, costos y stock con reportes simples de filtrar por día, semana o mes.";

const REPORT_TYPES = [
  { label: "Productos más vendidos", value: "best_sellers" },
  { label: "Costos", value: "pricing" },
  { label: "Stock", value: "stock" },
];

function formatDateArg(date) {
  return new Intl.DateTimeFormat("es-ES", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  })
    .format(date)
    .replace(".", "");
}

function ProductsReport() {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const {
    startDate,
    endDate,
    setStartDate,
    setEndDate,
  } = useProductsReport();

  const { fetch: fetchMostSold } = useMostSoldProducts();
  const { fetch: fetchTopFive } = useTopFiveProducts();

  const navigate = useNavigate();
  const location = useLocation();

  const currentPath = location.pathname.split("/").pop();
  const currentReportType =
    REPORT_TYPES.find((t) => t.value === currentPath)?.value ||
    REPORT_TYPES[0].value;

  const handleReportTypeChange = (e) => {
    navigate(e.target.value);
  };

  return (
    <section className="w-6xl">
      <h3 className="text-3xl font-semibold pb-3">{TITLE}</h3>
      <p className="text-stokia-neutral-500">{SUB_TITLE}</p>

      <div id="filters" className="flex flex-row pt-3 gap-3.5">
        <Label className="flex flex-col gap-3 items-start">
          Tipo de reporte
          <NativeSelect value={currentReportType} onChange={handleReportTypeChange}>
            {REPORT_TYPES.map((reportType) => (
              <option key={reportType.value} value={reportType.value}>
                {reportType.label}
              </option>
            ))}
          </NativeSelect>
        </Label>

        <section id="specificFilters" className="flex flex-row gap-3">
          <Label className="flex flex-col gap-3 items-start">
            Selección de periodo
            <Button
              variant="stokia"
              className=""
              onClick={() => setIsModalOpen(true)}
            >
              <Calendar1 size={12} />
              {formatDateArg(startDate)} - {formatDateArg(endDate)}
            </Button>
          </Label>

          <Label className="flex flex-col gap-3 items-start">
            Producto
            <InputGroup>
              <InputGroupInput placeholder="Buscar producto" />
              <InputGroupAddon>
                <SearchIcon />
              </InputGroupAddon>
              <InputGroupButton
                onClick={() => setSearch("")}
                size="icon-xs"
                variant="ghost"
                className="shadow-none!"
              >
                <X />
              </InputGroupButton>
            </InputGroup>
          </Label>
        </section>
      </div>

      <Outlet />

      <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
        <DialogTitle className="sr-only">Calendar</DialogTitle>
        <Description className="sr-only">Calendar</Description>

        <DialogContent className="max-h-[90vh] overflow-y-auto p-9 flex flex-col gap-4 max-w-lg bg-white border-0 items-center">
          <div className="flex justify-between w-full">
            <h4 className="text-xl">Selección de fechas</h4>

            <p className="flex items-center gap-1.5 text-sm">
              <Calendar1 size={14} />
              {formatDateArg(startDate)} - {formatDateArg(endDate)}
            </p>
          </div>

          <Calendar
            mode="range"
            className="[&_table]:border! [&_table]:border-separate [&_table]:border-stokia-neutral-300 [&_table]:p-4! [&_table]:rounded-lg!"
            defaultMonth={startDate}
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
              table: "bg-red-100!"
            }}
          />

          <div className="flex gap-9">
            <Button variant="secondary" onClick={() => setIsModalOpen(false)}>
              Cancelar
            </Button>

            <Button
              variant="stokia"
              onClick={() => {
                fetchMostSold();
                fetchTopFive();
                setIsModalOpen(false);
              }}
            >
              Aplicar
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </section>
  );
}

export default ProductsReport;
