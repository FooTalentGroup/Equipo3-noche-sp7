import React, { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/shared/components/ui/form";
import { Button } from "@/shared/components/ui/button";
import { useCreateProduct } from "../hooks/useCreateProduct";
import { ImageDropzone } from "@/shared/components/ui/ImageDropzone";
import { FormSelectField } from "./ProductFormSelectField";
import { LoadingModal } from "@/shared/components/ui/LoadingModal";
import { SuccessModal } from "@/shared/components/ui/SuccessModal";
import { FormInputField } from "@/features/products/components/ProductFormInputField";
import { useCategories } from "@/features/categories/context/CategoriesContext";
import { useProducts } from "@/features/products/context/ProductsContext";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/shared/components/ui/alert-dialog";

export function CreateProductComponent() {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEditing = !!id;

  const { form, handlePost, handleError, isPending, isSuccess, resetSuccess } =
    useCreateProduct();
  const { categories } = useCategories();
  const { products } = useProducts();

  useEffect(() => {
    console.log(isEditing && id);
    if (isEditing && id) {
      const product = products.find((p) => p.id === parseInt(id));
      if (product) {
        form.reset({
          name: product.name,
          category: product.category.id,
          min_stock: product.min_stock,
          price: parseFloat(product.price.replace(/[^0-9.-]+/g, "")),
          image_file: null,
        });
      }
    }
  }, [isEditing, id, products, form]);

  const handleRegisterAnother = () => {
    form.reset();
    if (resetSuccess) resetSuccess();
  };

  const handleGoToProducts = () => {
    navigate("/products");
  };

  return (
    <div className="w-full flex flex-col md:max-w-5xl p-12 bg-stokia-neutral-50 border rounded-sm relative h-min">
      <Form {...form}>
        <section id="header" className="flex flex-col gap-2">
          <h1 className="text-3xl font-bold">
            {isEditing ? "Editar producto" : "Registrar un nuevo producto"}
          </h1>
          <h5 className="text-xs text-muted-foreground font-weight-light">
            {isEditing
              ? "Edita la ficha técnica del producto para actualizar el inventario"
              : "Completa la ficha técnica del producto para agregarlo al inventario"}
          </h5>
        </section>
        <form
          className="flex flex-col gap-6 w-full max-w-2xl pt-12"
          onSubmit={form.handleSubmit(handlePost, handleError)}
        >
          <div className="flex flex-col gap-6">
            <section id="body" className="flex flex-col gap-6">
              <div className="flex flex-col md:flex-row w-full gap-6">
                <FormInputField
                  control={form.control}
                  name="name"
                  label="Nombre del producto"
                  placeholder="¿Cómo se llama este producto?"
                  required
                />
                <FormSelectField
                  control={form.control}
                  name="category"
                  label="Categoría"
                  placeholder="Seleccionar categoría"
                  required
                  options={categories.map((c) => ({
                    value: c.id,
                    label: c.name,
                  }))}
                />
              </div>

              <div className="flex flex-col md:flex-row w-full gap-6">
                <FormInputField
                  control={form.control}
                  name="min_stock"
                  label="Stock mínimo"
                  type="number"
                  step="1"
                  required
                />

                <FormInputField
                  control={form.control}
                  name="price"
                  label="Precio de venta"
                  type="number"
                  step="0.01"
                  placeholder="Precio de venta"
                  required
                />
              </div>

              <FormField
                control={form.control}
                name="image_file"
                render={({ field }) => (
                  <FormItem className="flex flex-col w-full">
                    <FormLabel className="w-full md:w-28 self-start">
                      <b>Multimedia</b>
                    </FormLabel>
                    <div className="w-full" style={{ flex: 1 }}>
                      <FormControl>
                        <ImageDropzone
                          value={field.value}
                          onChange={field.onChange}
                        />
                      </FormControl>
                      <FormMessage />
                    </div>
                  </FormItem>
                )}
              />
            </section>
          </div>
          <section id="footer" className="flex justify-end gap-3">
            <Button
              type="button"
              onClick={() => navigate("/products")}
              variant="outline"
            >
              Cancelar
            </Button>
            {!isEditing ? (
              <Button type="submit" className="bg-btn-primary">
                {isPending ? "Guardando..." : "Registrar Producto"}
              </Button>
            ) : (
              <ConfirmDialog
                buttonTitle="Guardar cambios"
                onAccept={form.handleSubmit(handlePost, handleError)}
                onOpenDialog={async () => {
                  const isValid = await form.trigger();
                  return isValid;
                }}
              />
            )}
          </section>
        </form>
      </Form>
      {isPending && (
        <LoadingModal className="absolute top-0 left-0 w-full h-full bg-card" />
      )}
      {isSuccess && (
        <SuccessModal
          className="absolute top-0 left-0 w-full h-full bg-card"
          title={
            isEditing
              ? "¡Producto actualizado exitosamente!"
              : "¡Producto registrado exitosamente!"
          }
          description={
            isEditing
              ? "Los cambios han sido guardados."
              : 'Serás redirigido a "Productos" en 5 segundos.'
          }
          primaryButtonText={
            isEditing ? "Seguir editando" : "Registrar otro producto"
          }
          secondaryButtonText="Volver"
          onPrimaryClick={
            isEditing
              ? () => resetSuccess && resetSuccess()
              : handleRegisterAnother
          }
          onSecondaryClick={handleGoToProducts}
        />
      )}
    </div>
  );
}

const ConfirmDialog = ({
  buttonTitle = "Confirmar",
  cancelTitle = "Cancelar",
  acceptTitle = "Sí, confirmar",
  dialogTitle = "¿Desea confirmar los cambios?",
  dialogDescription = "La información del producto se actualizará en el inventario",
  onAccept = () => {},
  onCancel = () => {},
  onOpenDialog = async () => true,
}) => {
  const [open, setOpen] = React.useState(false);

  const handleOpenChange = async (newOpen) => {
    if (newOpen) {
      const canOpen = await onOpenDialog();
      if (canOpen) {
        setOpen(true);
      }
    } else {
      setOpen(false);
    }
  };

  return (
    <AlertDialog open={open} onOpenChange={handleOpenChange}>
      <AlertDialogTrigger asChild>
        <Button type="button" className="bg-btn-primary">
          {buttonTitle}
        </Button>
      </AlertDialogTrigger>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>{dialogTitle}</AlertDialogTitle>
          <AlertDialogDescription>{dialogDescription}</AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel onClick={onCancel}>
            {cancelTitle}
          </AlertDialogCancel>
          <AlertDialogAction onClick={onAccept} className="bg-btn-primary">
            {acceptTitle}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};
