import { Button } from "@/shared/components/ui/button";
import { Calendar } from "@/shared/components/ui/calendar";
import { Dialog, DialogContent, DialogTitle } from "@/shared/components/ui/dialog";
import { Label } from "@/shared/components/ui/label";
import { NativeSelect } from "@/shared/components/ui/native-select";
import { Description } from "@radix-ui/react-dialog";
import { useState } from "react";

const TITLE = "Reporte de productos"
const SUB_TITLE = "Consulta de un vistazo tus productos más vendidos, costos y stock con reportes simples de filtrar por día, semana o mes."
const REPORT_TYPES = [
  {
    label: "Productos más vendidos",
    value: "best_sellers"
  },
  {
    label: "Costos",
    value: "pricing"
  },
  {
    label: "Stock",
    value: "stock"
  }
]

function formatDateArg(date) {
  return new Intl.DateTimeFormat("es-ES", {
    day: "2-digit",
    month: "short",
    year: "numeric"
  })
    .format(date)
    .replace(".", ""); // quita el punto de "nov." → "nov"
}

function ProductsReport() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [dateRange, setDateRange] = useState({
    from: new Date(2025, 5, 12),
    to: new Date(2025, 6, 15),
  })

  return (<section>
    <h3 className="text-3xl font-semibold pb-3">{TITLE}</h3>
    <p className="text-stokia-neutral-500">{SUB_TITLE}</p>
    <div id="filters" className="flex flex-row py-3">
      <Label className="flex flex-col gap-3 items-start">
        Tipo de reporte
        <NativeSelect>
          {REPORT_TYPES.map((reportType) => {
            return (
              <option key={reportType.value} value={reportType.value}>{reportType.label}</option>
            )
          })}
        </NativeSelect>
      </Label>
      <section id="">
        <Label className="flex flex-col gap-3 items-start">
          Selección de periodo
          <Button variant="stokia" size="lg" onClick={() => setIsModalOpen(true)}>{formatDateArg(new Date())}</Button>
        </Label>
      </section>



    </div>
    <Dialog open={isModalOpen}>
      <DialogTitle className="sr-only">
        Calendar
      </DialogTitle>
      <Description className="sr-only">
        Calendar
      </Description>
      <DialogContent className="max-h-[90vh] overflow-y-auto p-0 gap-0 max-w-xl bg-stokia-neutral-50 border-0">
        <h4>Selección de fechas</h4>
          <Calendar
            mode="range"
            defaultMonth={dateRange?.from}
            selected={dateRange}
            onSelect={setDateRange}
            numberOfMonths={1}
            className="rounded-lg border shadow-sm"
          />
      </DialogContent>
    </Dialog>

  </section>)
}

export default ProductsReport;