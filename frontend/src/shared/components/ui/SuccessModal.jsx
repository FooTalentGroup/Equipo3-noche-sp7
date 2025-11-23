import * as React from "react";
import { Check, CheckCircle2 } from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "./button";

export function SuccessModal({
  title = "¡Inventario actualizado exitosamente!",
  description = "Serás redirigido a \"Productos\" en 5 segundos.",
  primaryButtonText = "Registrar Producto",
  secondaryButtonText = "Volver",
  onPrimaryClick,
  onSecondaryClick,
  showButtons = true,
  className,
}) {
  return (
    <div
      className={cn(
        "flex flex-col items-center justify-center gap-4 p-8",
        className
      )}
    >
      <div className="h-32 w-32 rounded-full border-8 border-btn-primary flex items-center justify-center">
        <Check strokeWidth={3} className="text-btn-primary w-12 h-12"></Check>
      </div>
      <div className="flex flex-col items-center gap-2 text-center">
        <h3 className="text-3xl font-semibold">{title}</h3>
        <p className="text-sm text-muted-foreground">{description}</p>
      </div>
      {showButtons && (
        <div className="pt-10 gap-3 flex">
          <Button variant="ghost" onClick={onSecondaryClick}>
            {secondaryButtonText}
          </Button>
          <Button className="bg-btn-primary" onClick={onPrimaryClick}>
            {primaryButtonText}
          </Button>
        </div>
      )}
    </div>
  );
}