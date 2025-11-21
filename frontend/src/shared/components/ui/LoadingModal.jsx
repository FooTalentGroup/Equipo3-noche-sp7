import * as React from "react";
import { Loader2 } from "lucide-react";
import { cn } from "@/lib/utils";

export function LoadingModal({
  title = "Actualizando inventario...",
  description = "Estamos actualizando el inventario, por favor espera...",
  className,
}) {
  return (
    <div
      className={cn(
        "flex flex-col items-center justify-center gap-4 p-8",
        className
      )}
    >
      <Loader2 className="h-20 w-20 animate-spin text-slate-800" />
      <div className="flex flex-col items-center gap-2 text-center">
        <h3 className="text-lg font-semibold text-gray-900">{title}</h3>
        <p className="text-sm text-gray-600">{description}</p>
      </div>
    </div>
  );
}

