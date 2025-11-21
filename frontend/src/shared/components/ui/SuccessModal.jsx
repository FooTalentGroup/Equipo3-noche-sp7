import * as React from "react";
import { CheckCircle2 } from "lucide-react";
import { cn } from "@/lib/utils";

export function SuccessModal({
  title = "¡Inventario actualizado exitosamente!",
  description = "Serás redirigido a \"Productos\" en 5 segundos.",
  className,
}) {
  return (
    <div
      className={cn(
        "flex flex-col items-center justify-center gap-4 p-8",
        className
      )}
    >
      <div className="h-20 w-20 rounded-full bg-[#436086] flex items-center justify-center">
        <CheckCircle2 className="h-12 w-12 text-white" />
      </div>
      <div className="flex flex-col items-center gap-2 text-center">
        <h3 className="text-lg font-semibold text-gray-900">{title}</h3>
        <p className="text-sm text-gray-600">{description}</p>
      </div>
    </div>
  );
}

