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
        "flex flex-col items-center justify-center gap-12 p-8",
        className
      )}
    >
      <Loader2 className="h-32 w-32 animate-spin text-stokia-primary-600" strokeWidth={1.5} />
      <div className="flex flex-col items-center gap-6 text-center">
        <h3 className="text-3xl font-semibold">{title}</h3>
        <p className="text-sm text-stokia-neutral-400">{description}</p>
      </div>
    </div>
  );
}

