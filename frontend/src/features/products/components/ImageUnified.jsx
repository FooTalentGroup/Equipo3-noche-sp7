import { FormField, FormItem, FormLabel, FormControl, FormMessage } from "@/shared/components/ui/form";
import { ImageDropzone } from "@/shared/components/ui/ImageDropzone";
import { FormInputField } from "@/features/products/components/ProductFormInputField";
import { useEffect } from "react";

export function UnifiedImageField({ form }) {
  const imageFile = form.watch("imageFile");
  const photoUrl = form.watch("photoUrl");

  const handleClearBoth = () => {
    form.setValue("imageFile", null);
    form.setValue("photoUrl", "");
  };

  useEffect(()=>{
    if(photoUrl){
      form.setValue("imageFile", null);
    }
    if(imageFile){
      form.setValue("photoUrl", "");
    }
  },[photoUrl, imageFile, form])

  return (
    <div className="space-y-4">
      <FormInputField
        control={form.control}
        name="photoUrl"
        label="URL de la imagen"
        placeholder="https://ejemplo.com/imagen.jpg"
        type="url"
        disabled={!!imageFile}
      />

      {/* Divider */}
      <div className="relative">
        <div className="absolute inset-0 flex items-center">
          <span className="w-full border-t" />
        </div>
        <div className="relative flex justify-center text-xs uppercase">
          <span className="bg-stokia-neutral-50 px-2 text-muted-foreground">
            O sube un archivo
          </span>
        </div>
      </div>

      <FormField
        control={form.control}
        name="imageFile"
        render={({ field }) => (
          <FormItem className="flex flex-col w-full">
            <FormLabel className="w-full md:w-28 self-start">
              <b>Multimedia</b>
            </FormLabel>
            <div className="w-full" style={{ flex: 1 }}>
              <FormControl>
                <ImageDropzone
                  value={imageFile || photoUrl}
                  onChange={(value) => {
                    if (value) {
                      if (value instanceof File) {
                        form.setValue("imageUrl", "");
                        field.onChange(value);
                      }
                    } else {
                      handleClearBoth();
                    }
                  }}
                  disabled={!!photoUrl && !imageFile}
                />
              </FormControl>
              <FormMessage />
            </div>
          </FormItem>
        )}
      />
    </div>
  );
}