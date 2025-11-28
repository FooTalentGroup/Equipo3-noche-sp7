import React from "react";
import { Bell } from "lucide-react";
import { Button } from "@/shared/components/ui/button";
import { useLocation } from "react-router-dom";

const PAGE_LABELS = [
  { path: "/products", label: "Producto" },
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

  const pageLabel =
    PAGE_LABELS.find((p) => pathname.startsWith(p.path))?.label ||
    pathname.split("/").filter(Boolean)[0] ||
    "Panel";

  return (
    <div className="top-0 z-40 flex items-center border py-2.5 px-8 h-20 rounded-sm">
      <h2 className="text-lg font-semibold">{pageLabel}</h2>

      <div className="flex-1" />

      <Button variant="outline">
        <Bell className="h-5 w-5" />
        <span className="hidden sm:inline">Notificaciones</span>
      </Button>
    </div>
  );
}
