import { useCallback } from "react";
import { useDropzone } from "react-dropzone";
import { ImageIcon } from "lucide-react";
import { Button } from "./button";

export function ImageDropzone({ value, onChange }) {
  const onDrop = useCallback(
    (acceptedFiles) => {
      if (acceptedFiles && acceptedFiles.length > 0) {
        onChange(acceptedFiles[0]);
      }
    },
    [onChange]
  );

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    multiple: false,
    accept: { "image/*": [] },
  });

  const getPreviewUrl = () => {
    if (!value) return null;
    
    if (typeof value === 'string') {
      return value;
    }
    if (value instanceof File) {
      return URL.createObjectURL(value);
    }
    return null;
  };

  const previewUrl = getPreviewUrl();

  return (
    <div
      {...getRootProps()}
      className="flex flex-col items-center justify-center gap-3 border border-dashed rounded-xl bg-card p-10 cursor-pointer transition hover:bg-accent/10 text-center min-h-40"
    >
      <input {...getInputProps()} />

      {isDragActive ? (
        <p className="text-sm text-muted-foreground">
          Suelta la imagen aquí...
        </p>
      ) : previewUrl ? (
        <div className="flex flex-col items-center gap-3">
          <img
            src={previewUrl}
            alt="preview"
            className="w-40 h-40 object-cover rounded-md shadow"
          />

          <Button
            variant="outline"
            type="button"
            onClick={(e) => {
              e.stopPropagation();
              onChange(null);
            }}
          >
            Quitar imagen
          </Button>
        </div>
      ) : (
        <div className="flex flex-col items-center gap-1">
          <span className="flex items-center justify-center bg-gray-100 rounded-full p-2">
            <ImageIcon className="h-5 w-5 text-muted-foreground" />
          </span>
          <p className="font-medium">Adjunta una imagen</p>
          <p className="text-xs text-muted-foreground">
            Arrastre aquí la imagen o cárguela desde su ordenador
          </p>
        </div>
      )}
    </div>
  );
}