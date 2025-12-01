import React from "react";
import { Bell } from "lucide-react";
import { Button } from "@/shared/components/ui/button";
import { useLocation } from "react-router-dom";

const PAGE_LABELS = [
  { path: "/products", label: "Producto" },
  { path: "/inventory-movements", label: "Historial de movimientos", parent: "Producto" },
  { path: "/sales", label: "Ventas" },
  { path: "/customers", label: "Clientes" },
  { path: "/suppliers", label: "Proveedores" },
  { path: "/discounts", label: "Descuentos" },
  { path: "/predictions-IA", label: "Predicciones de IA" },
  { path: "/reports", label: "Reportes" },
  { path: "/home", label: "Inicio" },
];

export function TopBar() {
  const { pathname } = useLocation();

  const currentPage = PAGE_LABELS.find((p) => pathname.startsWith(p.path));
  const pageLabel = currentPage?.label || pathname.split("/").filter(Boolean)[0] || "Panel";
  const hasParent = currentPage?.parent;

  return (
    <div className="top-0 z-40 flex items-center border py-2.5 px-8 h-20 rounded-sm">
      {hasParent ? (
        <div className="min-h-9 pl-4 pr-1.5 py-2 opacity-60 rounded-lg inline-flex justify-start items-center gap-2">
          <div className="text-center justify-center text-[#404040] text-[14px] font-normal  leading-5 tracking-tight">
            {currentPage.parent} &gt;
          </div>
          <div className="text-center justify-center text-[#404040] text-[14px] font-medium  leading-5 tracking-tight">
            {pageLabel}
          </div>
        </div>
      ) : (
        <h2 className="text-lg font-semibold">{pageLabel}</h2>
      )}

      <div className="flex-1" />

      <Button variant="outline">
        <Bell className="h-5 w-5" />
        <span className="hidden sm:inline">Notificaciones</span>
      </Button>
    </div>
  );
}
