import React from "react";
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
import { FormInputField } from "./ProductFormInputField";
import { FormSelectField } from "./ProductFormSelectField";
import { LoadingModal } from "@/shared/components/ui/LoadingModal";
import { SuccessModal } from "@/shared/components/ui/SuccessModal";
import { DollarSign } from "lucide-react";

const mockCategories = [
    { id: 1, name: "Electrónica" },
    { id: 2, name: "Hogar y Jardín" },
    { id: 3, name: "Moda y Accesorios" },
    { id: 4, name: "Alimentos" },
];

const mockProviders = [
    { id: 1, name: "Proveedor 1" },
    { id: 2, name: "Proveedor 2" },
    { id: 3, name: "Proveedor 3" },
    { id: 4, name: "Proveedor 4" },
];

export function CreateProductComponent() {
    const { form, handlePost, handleError, isPending, isSuccess } = useCreateProduct();

    return (
        <div className="w-full flex flex-col md:max-w-5xl p-12 bg-sidebar border rounded-sm relative h-min">
                <Form {...form}>
                <section id="header" className="flex flex-col gap-2">
                    <h1 className="text-3xl font-bold">
                        Registrar un nuevo producto
                    </h1>
                    <h5 className="text-xs text-muted-foreground font-weight-light">
                        Completa la ficha técnica del producto para agregarlo al
                        inventario
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
                                    options={mockCategories.map((c) => ({
                                        value: c.id,
                                        label: c.name,
                                    }))}
                                />
                            </div>

                            <div className="flex flex-col md:flex-row w-full gap-6">
                                <FormInputField
                                    control={form.control}
                                    name="cost_price"
                                    startIcon={DollarSign}
                                    label="Costo de compra"
                                    placeholder="Colocar costo de compra"
                                    type="number"
                                    step="0.01"
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

                            <div className="flex flex-col md:flex-row w-full gap-6">
                                <FormInputField
                                    control={form.control}
                                    name="min_stock"
                                    label="Stock mínimo"
                                    type="number"
                                    step="1"
                                    required
                                />

                                <FormSelectField
                                    control={form.control}
                                    name="provider"
                                    label="Proveedor"
                                    placeholder="Colocar nombre del proveedor"
                                    required
                                    options={mockProviders.map((p) => ({
                                        value: p.id,
                                        label: p.name,
                                    }))}
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
                                        <div
                                            className="w-full"
                                            style={{ flex: 1 }}
                                        >
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
                            onClick={form.reset}
                            variant="outline"
                        >
                            Cancelar
                        </Button>
                        <Button type="submit" className="bg-btn-primary">
                            {isPending ? "Guardando..." : "Registrar Producto"}
                        </Button>
                    </section>
                </form>
            </Form>
            {isPending && <LoadingModal className="absolute top-0 left-0 w-full h-full bg-card" />}
            {isSuccess && <SuccessModal className="absolute top-0 left-0 w-full h-full bg-card" />}
        </div>
    );
}
