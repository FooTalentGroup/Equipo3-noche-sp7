import * as React from "react";
import { Check, CheckCircle2, X } from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "./button";

export function SuccessModal({
  title = "¡Listo!",
  description = "Cliente registrado correctamente.",
  primaryButtonText = "Registrar cliente",
  secondaryButtonText = "Volver",
  onPrimaryClick,
  onSecondaryClick,
  showButtons = true,
  onClose,
  className,
}) {
  return (
    <div
      className={cn(
        "flex flex-col items-center justify-center gap-4 p-8",
        className
      )}
    >
      {/* Close button */}
      {onClose && (
        <button
          onClick={onClose}
          className="absolute right-12 top-12 w-6 h-6 text-[#525252] hover:text-[#171717] transition-colors"
          aria-label="Cerrar"
        >
          <X className="w-4 h-4" strokeWidth={2} />
        </button>
      )}

      <section className="flex flex-col gap-12 items-center">
          <CheckCircle2 strokeWidth={1.5} className="text-stokia-primary-800 w-32 h-32"></CheckCircle2>

        <div className="flex flex-col gap-6 items-center">
          <h3 className="text-3xl font-semibold">
            {title}
          </h3>

          <p className="text-sm text-muted-foreground">
            {description}
          </p>
        </div>

        {showButtons && (
          <div className="inline-flex justify-center items-center gap-6">
            <Button
              variant="ghost"
              onClick={onSecondaryClick}
            >
              {secondaryButtonText}
            </Button>
            <Button
              onClick={onPrimaryClick}
              variant="stokia"
            >
              {primaryButtonText}
            </Button>
          </div>
        )}
      </section>
    </div>
  );
}