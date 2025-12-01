import * as React from "react";
import { Check, X } from "lucide-react";
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
        "w-[610px] h-[625px] relative bg-[#F4F5F7] rounded-2xl shadow-[2px_2px_4px_0px_rgba(0,0,0,0.12)] flex flex-col items-center justify-center",
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

      {/* Check icon circle */}
      <div className="w-40 h-40 mb-6 flex items-center justify-center">
        <div className="w-28 h-28 rounded-full border-8 border-[#436086] flex items-center justify-center">
          <Check strokeWidth={3} className="text-[#436086] w-12 h-12" />
        </div>
      </div>

      {/* Title */}
      <h3 className="w-80 text-center text-[#171717] text-3xl font-semibold font-['Roboto_Flex'] mb-6">
        {title}
      </h3>

      {/* Description */}
      <p className="w-64 text-center text-[#171717] text-xs font-normal font-['Roboto_Flex'] mb-16">
        {description}
      </p>

      {/* Buttons */}
      {showButtons && (
        <div className="inline-flex justify-center items-center gap-6">
          <Button
            variant="ghost"
            onClick={onSecondaryClick}
            className="min-h-10 px-6 py-2.5 bg-transparent rounded-lg text-[#525252] text-sm font-medium hover:bg-gray-100"
          >
            {secondaryButtonText}
          </Button>
          <Button
            onClick={onPrimaryClick}
            className="min-h-10 px-6 py-2.5 bg-[#436086] hover:bg-[#364d6e] rounded-lg shadow-[0px_2px_4px_0px_rgba(0,0,0,0.25)] text-white text-sm font-medium"
          >
            {primaryButtonText}
          </Button>
        </div>
      )}
    </div>
  );
}