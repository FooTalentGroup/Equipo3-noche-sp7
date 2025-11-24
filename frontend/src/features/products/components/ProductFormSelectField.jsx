import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/shared/components/ui/form";
import { NativeSelect } from "@/shared/components/ui/native-select";

export function FormSelectField({
  control,
  name,
  label,
  options = [],
  placeholder = "Selecciona una opción",
  required = false,
  onChange,
}) {
  return (
    <FormField
      control={control}
      name={name}
      render={({ field, fieldState }) => (
        <FormItem className="flex flex-col w-full">
          <FormLabel className="w-full md:w-28">
            <b>{label}</b>{" "}
            {required && <span className="text-red-500">*</span>}
          </FormLabel>

          <div className="w-full" style={{ flex: 1 }}>
            <FormControl>
              <NativeSelect
                {...field}
                onChange={(e) => {
                  const value = e.target.value;
                  if (onChange) return onChange(e, field);
                  field.onChange(+value || 0);
                }}
                value={field.value ?? ""}
                className="w-full bg-card"
              >
                <option value="">{placeholder}</option>
                {options.map((opt) => (
                  <option key={opt.value} value={String(opt.value)}>
                    {opt.label}
                  </option>
                ))}
              </NativeSelect>
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
