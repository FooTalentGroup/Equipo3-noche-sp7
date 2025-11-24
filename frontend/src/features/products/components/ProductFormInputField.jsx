import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/shared/components/ui/form";
import { Input } from "@/shared/components/ui/input";
import { AlertCircle } from "lucide-react";
import { cn } from "@/lib/utils";

export function FormInputField({
  control,
  name,
  label,
  placeholder,
  type = "text",
  step,
  onChange,
  required = false,
  startIcon: StartIcon,
}) {
  return (
    <FormField
      control={control}
      name={name}
      render={({ field, fieldState }) => (
        <FormItem className="w-full">
          <FormLabel className="w-full flex items-center gap-1">
            <b>{label}</b>
            {required && <span className="text-red-500">*</span>}
          </FormLabel>

          <div className="w-full flex flex-col" style={{ flex: 1 }}>
            <FormControl>
              <div className="relative">
                {StartIcon && (
                  <div className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground pointer-events-none">
                    <StartIcon className="h-4 w-4" />
                  </div>
                )}
                {fieldState.error && (
                  <div className="absolute right-3 top-1/2 -translate-y-1/2 text-destructive pointer-events-none">
                    <AlertCircle className="h-4 w-4" />
                  </div>
                )}
                <Input
                  {...field}
                  type={type}
                  step={step}
                  placeholder={placeholder}
                  className={cn(
                    "bg-card",
                    StartIcon && "pl-9",
                    fieldState.error && "pr-9"
                  )}
                  maxLength={100}
                  onFocus={(e) => {
                    if (type === "number" && e.target.value === "0") {
                      field.onChange("");
                    }
                  }}
                  onBlur={(e) => {
                    if (type === "number" && e.target.value === "") {
                      field.onChange(0);
                    }
                  }}
                  onChange={(e) => {
                    const value = e.target.value;

                    if (onChange) return onChange(e, field);

                    if (type === "number") {
                      if (value === "") {
                        return field.onChange(0);
                      }
                      if (
                        value.length > 0 &&
                        parseInt(value) > 1 &&
                        value.startsWith("0")
                      ) {
                        return field.onChange(value.substring(1));
                      }
                      let num = e.target.valueAsNumber;
                      if (isNaN(num)) num = 0;
                      if (num < 0) num = 0;
                      return field.onChange(num);
                    }

                    field.onChange(value);
                  }}
                />
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
