import { Dialog, DialogContent } from "@/shared/components/ui/dialog";
import { Button } from "@/shared/components/ui/button";
import { X, Download, Calendar } from "lucide-react";
import { useRef, useState } from "react";
import stokialogo from "@/assets/stockia.svg";
import jsPDF from "jspdf";
import domtoimage from "dom-to-image-more";

export function ReportExportModal({ isOpen, onClose, reportData }) {
  const modalContentRef = useRef(null);
  const [isExporting, setIsExporting] = useState(false);

  if (!reportData) {
    return null;
  }

  const handleExport = async () => {
    if (!reportData) {
      return;
    }

    if (!modalContentRef.current) {
      console.error("Modal content ref is not available");
      alert("Error: No se puede acceder al contenido del modal");
      return;
    }

    setIsExporting(true);

    try {
      await new Promise((resolve) => setTimeout(resolve, 1000));

      const footer = modalContentRef.current.querySelector(".export-footer");
      if (footer) footer.style.display = "none";

      const container = modalContentRef.current;
      const originalBoxShadow = container.style.boxShadow;
      const originalBorder = container.style.border;
      container.style.boxShadow = "none";
      container.style.border = "none";

      const allElements = container.querySelectorAll("*");
      const originalStyles = [];
      allElements.forEach((el, index) => {
        originalStyles[index] = {
          border: el.style.border,
          boxShadow: el.style.boxShadow,
          outline: el.style.outline,
          overflow: el.style.overflow,
        };
        el.style.border = "none";
        el.style.boxShadow = "none";
        el.style.outline = "none";
        el.style.overflow = "visible";
      });

      const dataUrl = await domtoimage.toPng(container, {
        quality: 1,
        bgcolor: "#ffffff",
        width: container.offsetWidth,
        height: container.scrollHeight,
      });

      if (footer) footer.style.display = "flex";
      container.style.boxShadow = originalBoxShadow;
      container.style.border = originalBorder;

      allElements.forEach((el, index) => {
        if (originalStyles[index]) {
          el.style.border = originalStyles[index].border || "";
          el.style.boxShadow = originalStyles[index].boxShadow || "";
          el.style.outline = originalStyles[index].outline || "";
          el.style.overflow = originalStyles[index].overflow || "";
        }
      });

      const img = new Image();
      img.src = dataUrl;

      await new Promise((resolve) => {
        img.onload = resolve;
      });

      const imgWidth = 210;
      const pageHeight = 297;
      const imgHeight = (img.height * imgWidth) / img.width;

      const pdf = new jsPDF("p", "mm", "a4");

      if (imgHeight <= pageHeight) {
        pdf.addImage(dataUrl, "PNG", 0, 0, imgWidth, imgHeight);
      } else {
        const scale = pageHeight / imgHeight;
        const scaledWidth = imgWidth * scale;
        const scaledHeight = pageHeight;
        const xOffset = (imgWidth - scaledWidth) / 2;
        pdf.addImage(dataUrl, "PNG", xOffset, 0, scaledWidth, scaledHeight);
      }

      const filename = `${reportData.title.replace(/ /g, "_")}_${
        new Date().toISOString().split("T")[0]
      }.pdf`;
      pdf.save(filename);
    } catch (error) {
      alert(`Error al generar el PDF: ${error.message}`);

      const footer = modalContentRef.current?.querySelector(".export-footer");
      if (footer) {
        footer.style.display = "flex";
      }
    } finally {
      setIsExporting(false);
    }
  };

  const getMonthYear = () => {
    if (reportData.dateRange) {
      const parts = reportData.dateRange.split(" - ");
      if (parts.length > 0) {
        const date = parts[0];
        return "Noviembre 2025";
      }
    }
    return "";
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-[600px] max-h-[90vh] overflow-y-auto p-0 bg-white border-0">
        <div ref={modalContentRef} className="p-8">
          <div className="mb-6">
            <h2 className="text-2xl font-bold text-stokia-neutral-900 mb-4">
              El reporte está listo para exportar
            </h2>

            <div className="border-t border-stokia-neutral-200 pt-4">
              <div className="flex justify-between items-center mb-3">
                <div>
                  <img src={stokialogo} alt="Stockia" className="h-6" />
                </div>
                <div className="text-center flex-1">
                  <p className="font-semibold text-stokia-neutral-900">
                    {reportData.title}
                  </p>
                </div>
                <div className="text-right">
                  <p className="text-sm text-stokia-neutral-600">Página 1/2</p>
                </div>
              </div>

              <div className="flex justify-between items-center text-sm text-stokia-neutral-600">
                <div className="flex items-center gap-1">
                  <Calendar className="h-4 w-4" />
                  <span>{reportData.dateRange}</span>
                </div>
                <span>{getMonthYear()}</span>
              </div>
            </div>
          </div>

          <div
            className="mb-6 bg-white rounded-lg border border-stokia-neutral-200 p-4"
            style={{ minHeight: "280px" }}
          >
            {reportData.chartComponent}
          </div>
          <div className="mb-6 overflow-x-auto rounded-lg border border-stokia-neutral-200">
            <table className="w-full text-center border-separate border-spacing-0">
              <thead className="text-xs bg-stokia-neutral-100 text-stokia-neutral-700 font-semibold h-[40px]">
                <tr className="[&_th]:px-4 [&_th]:py-2 uppercase tracking-wider">
                  {reportData.tableHeaders.map((header, idx) => (
                    <th key={idx} className={idx === 0 ? "text-left" : ""}>
                      {header}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-stokia-neutral-100 bg-white">
                {reportData.tableRows.map((row, idx) => (
                  <tr
                    key={idx}
                    className="[&_td]:text-stokia-neutral-900 [&_td]:text-sm [&_td]:px-4 [&_td]:py-3"
                  >
                    {row.map((cell, cellIdx) => (
                      <td
                        key={cellIdx}
                        className={cellIdx === 0 ? "text-left font-medium" : ""}
                      >
                        {cell}
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {reportData.endComponent && (
            <div className="mb-6 bg-white rounded-lg border border-stokia-neutral-200 p-4">
              {reportData.endComponent}
            </div>
          )}

          <div className="flex justify-between items-center pt-4 border-t border-stokia-neutral-200 export-footer">
            <Button
              variant="outline"
              onClick={onClose}
              className="text-stokia-neutral-700 border-stokia-neutral-300"
            >
              Cerrar
            </Button>
            <div className="flex items-center gap-4">
              <div className="text-right">
                <p className="text-xs text-stokia-neutral-500">powered by</p>
                <img src={stokialogo} alt="Stockia" className="h-5" />
              </div>
              <Button
                onClick={handleExport}
                disabled={isExporting}
                className="bg-stokia-primary-600 hover:bg-stokia-primary-700 text-white flex items-center gap-2 disabled:opacity-50"
              >
                <Download className="h-4 w-4" />
                {isExporting ? "Generando PDF..." : "Exportar"}
              </Button>
            </div>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}
