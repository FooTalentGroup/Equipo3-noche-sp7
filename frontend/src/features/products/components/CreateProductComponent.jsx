import React, { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Form } from "@/shared/components/ui/form";
import { Button } from "@/shared/components/ui/button";
import { useCreateProduct } from "../hooks/useCreateProduct";
import { FormSelectField } from "./ProductFormSelectField";
import { LoadingModal } from "@/shared/components/ui/LoadingModal";
import { SuccessModal } from "@/shared/components/ui/SuccessModal";
import { FormInputField } from "@/features/products/components/ProductFormInputField";
import { useCategories } from "@/features/categories/context/CategoriesContext";
import { useProduct } from "../hooks/useProduct";
import { UnifiedImageField } from "./ImageUnified";
import { BackErrorAlert } from "./BackErrorAlert";
import { ConfirmDialog } from "./ConfirmDialog";
import { useState } from "react";
import { Trash2 } from "lucide-react";

export function CreateProductComponent() {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEditing = !!id;

  const { form, handlePost, handleError, isPending, isSuccess, resetSuccess } =
    useCreateProduct();
  const { categories, isFetching } = useCategories();
  const { isLoading, error, product } = useProduct();

  const [isConfirmOpen, setIsConfirmOpen] = useState(false);

  useEffect(() => {
    if (isEditing && id) {
      if (product && !isLoading) {
        form.reset({
          name: product.name,
          categoryId: product.category.id,
          minStock: product.minStock,
          price: parseFloat(product.price),
          photoUrl: product.photoUrl || null,
        });
      }
    }
  }, [isEditing, id, form, product]);

  const handleRegisterAnother = () => {
    if (resetSuccess) resetSuccess();
  };

  const handleAcceptEdit = () => {
    form.handleSubmit(handlePost, handleError)();
    setIsConfirmOpen(false);
  };

  const handleClicCancel = () => {
    form.reset();
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
        {error && <BackErrorAlert alertTitle={error.message} />}
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
                  name="categoryId"
                  label="Categoría"
                  placeholder="Seleccionar categoría"
                  required
                  isLoading={isFetching}
                  options={categories.map((c) => ({
                    value: c.id,
                    label: c.name,
                  }))}
                />
              </div>

              <div className="flex flex-col md:flex-row w-full gap-6">
                <FormInputField
                  control={form.control}
                  name="minStock"
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

              <UnifiedImageField form={form} />
            </section>
          </div>
          <section id="footer" className="flex justify-start gap-3">
            <Button type="button" onClick={handleClicCancel} variant="outline">
              Cancelar
            </Button>
            {!isEditing ? (
              <Button type="submit" variant="stokia">
                {isPending ? "Guardando..." : "Aceptar Registro"}
              </Button>
            ) : (
              <Button
                className={"bg-btn-primary"}
                type="button"
                onClick={() => setIsConfirmOpen(true)}
              >
                Guardar cambios
              </Button>
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
          primaryButtonText={isEditing ? "Seguir editando" : "Nuevo registro"}
          secondaryButtonText="Volver"
          onPrimaryClick={
            isEditing
              ? () => resetSuccess && resetSuccess()
              : handleRegisterAnother
          }
          onSecondaryClick={handleClicCancel}
        />
      )}
      {isEditing && (
        <ConfirmDialog
          isOpen={isConfirmOpen}
          handleOpenChange={setIsConfirmOpen}
          buttonTitle="Guardar cambios"
          onAccept={handleAcceptEdit}
          onOpenDialog={async () => {
            const isValid = await form.trigger();
            return isValid;
          }}
        />
      )}
    </div>
  );
}
