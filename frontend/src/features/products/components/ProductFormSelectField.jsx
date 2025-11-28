import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/shared/components/ui/form";
import { NativeSelect } from "@/shared/components/ui/native-select";
import { Loader2 } from "lucide-react";
import { cn } from "@/lib/utils";

export function FormSelectField({
  control,
  name,
  label,
  options = [],
  placeholder = "Selecciona una opción",
  required = false,
  onChange,
  isLoading = false,
}) {
  return (
    <FormField
      control={control}
      name={name}
      render={({ field, fieldState }) => (
        <FormItem className="flex flex-col w-full">
          <FormLabel className="w-full md:w-28">
            <b>{label}</b>{" "}
            {required && <span className="text-destructive">*</span>}
          </FormLabel>

          <div className="w-full" style={{ flex: 1 }}>
            <FormControl>
              <div className="relative">
                <NativeSelect
                  {...field}
                  onChange={(e) => {
                    const value = e.target.value;
                    if (onChange) return onChange(e, field);
                    field.onChange(value || "");
                  }}
                  value={field.value ?? ""}
                  className={cn(
                    "w-full bg-card",
                    fieldState.error && "border-destructive focus-visible:ring-destructive"
                  )}
                  disabled={isLoading}
                  aria-invalid={!!fieldState.error}
                >
                  <option value="">
                    {isLoading ? "Cargando..." : placeholder}
                  </option>
                  {!isLoading && options.map((opt) => (
                    <option key={opt.value} value={String(opt.value)}>
                      {opt.label}
                    </option>
                  ))}
                </NativeSelect>
                {isLoading && (
                  <div className="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none">
                    <Loader2 className="h-4 w-4 animate-spin text-muted-foreground" />
                  </div>
                )}
              </div>
            </FormControl>

            {fieldState.error && (
              <FormMessage>{fieldState.error.message}</FormMessage>
            )}
          </div>
        </FormItem>
      )}
    />
  );
}