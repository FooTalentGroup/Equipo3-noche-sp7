import React, { useEffect, useState } from "react";
import { Button } from "@/shared/components/ui/button.jsx";
import { Input } from "@/shared/components/ui/input.jsx";
import { SuccessModal } from "@/shared/components/ui/SuccessModal.jsx";
import { Loader } from "lucide-react";

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export default function RegisterCustomerPopup({ open, onClose, onSave, initialData = null }) {
    const isEditMode = !!initialData?.id;
    const [form, setForm] = useState({ nombre: "", telefono: "", email: "", esFrecuente: true });
    const [errors, setErrors] = useState({});
    const [showSuccess, setShowSuccess] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        if (open) {
            if (initialData) {
                setForm({
                    nombre: initialData.nombre || "",
                    telefono: initialData.telefono || "",
                    email: initialData.email || "",
                    esFrecuente: initialData.esFrecuente ?? true
                });
            } else {
                setForm({ nombre: "", telefono: "", email: "", esFrecuente: true });
            }
            setErrors({});
            setShowSuccess(false);
        }
    }, [initialData, open]);

    if (!open) return null;

    function validate() {
        const e = {};
        if (!form.nombre || form.nombre.trim().length < 3) {
            e.nombre = "El nombre debe tener al menos 3 caracteres";
        }
        if (!form.telefono || form.telefono.trim().length < 6) {
            e.telefono = "Número telefónico inválido";
        }
        if (!form.email || !emailRegex.test(form.email)) {
            e.email = "Correo electrónico inválido";
        }
        setErrors(e);
        return Object.keys(e).length === 0;
    }

    async function submit(e) {
        e?.preventDefault();
        if (!validate()) return;

        setIsSubmitting(true);
        try {
            await onSave?.({
                id: initialData?.id,
                nombre: form.nombre.trim(),
                email: form.email.trim(),
                telefono: form.telefono.trim(),
                esFrecuente: !!form.esFrecuente
            });

            setIsSubmitting(false);

            if (!isEditMode) {
                setShowSuccess(true);
            } else {
                handleClose();
            }
        } catch (error) {
            setIsSubmitting(false);

            const fieldErrors = error?.response?.data?.fieldErrors || error?.data?.fieldErrors;
            if (fieldErrors && Object.keys(fieldErrors).length > 0) {
                const normalized = Object.fromEntries(Object.entries(fieldErrors).map(([k, v]) => [k, String(v)]));
                setErrors(prev => ({ ...prev, ...normalized }));
                return;
            }

            if (error.response?.status === 500) {
                setErrors(prev => ({
                    ...prev,
                    email: "Cliente ya existente"
                }));
            } else {
                setErrors(prev => ({
                    ...prev,
                    email: error.response?.data?.message || error.message || "Cliente ya existente"
                }));
            }
        }
    }

    const handleClose = () => {
        setForm({ nombre: "", telefono: "", email: "", esFrecuente: true });
        setErrors({});
        setShowSuccess(false);
        setIsSubmitting(false);
        onClose?.();
    };

    const handleRegisterAnother = () => {
        setShowSuccess(false);
        setForm({ nombre: "", telefono: "", email: "", esFrecuente: true });
        setErrors({});
    };

    return (
        <div className="fixed inset-0 z-[1000] flex items-center justify-center">
            <div className="absolute inset-0 bg-black/40" onClick={!isSubmitting && !showSuccess ? handleClose : undefined} />
            <div
                role="dialog"
                aria-modal="true"
                aria-labelledby="register-customer-title"
                className="relative w-[700px] max-w-full bg-[#F4F5F7] rounded-xl shadow-lg p-10 z-10 flex flex-col min-h-[500px]"
            >
                {!isSubmitting && !showSuccess && (
                    <form onSubmit={submit} className="flex flex-col h-full">
                        <h3 id="register-customer-title" className="text-lg font-semibold">
                            {isEditMode ? 'Editar cliente' : 'Registrar cliente'}
                        </h3>
                        <p className="text-xs text-gray-500 mt-1">
                            {isEditMode
                                ? 'Actualiza la información del cliente'
                                : 'Completa este formulario para registrar un nuevo cliente'}
                        </p>
                        <hr className="my-6 border-gray-300" />

                        <div className="space-y-5 flex-1">
                            <div>
                                <label htmlFor="nombre" className="text-sm font-medium text-gray-700 flex items-center gap-1">
                                    Nombre <span className="text-red-500">*</span>
                                </label>
                                <Input
                                    id="nombre"
                                    className="mt-1 bg-white shadow-sm"
                                    value={form.nombre}
                                    onChange={(e) => setForm(f => ({ ...f, nombre: e.target.value }))}
                                />
                                {errors.nombre && <p className="text-xs text-red-600 mt-1">{errors.nombre}</p>}
                            </div>

                            <div>
                                <label htmlFor="telefono" className="text-sm font-medium text-gray-700 flex items-center gap-1">
                                    Número telefónico <span className="text-red-500">*</span>
                                </label>
                                <Input
                                    id="telefono"
                                    className="mt-1 bg-white shadow-sm"
                                    value={form.telefono}
                                    onChange={(e) => setForm(f => ({ ...f, telefono: e.target.value }))}
                                />
                                {errors.telefono && <p className="text-xs text-red-600 mt-1">{errors.telefono}</p>}
                            </div>

                            <div>
                                <label htmlFor="email" className="text-sm font-medium text-gray-700 flex items-center gap-1">
                                    Correo electrónico <span className="text-red-500">*</span>
                                </label>
                                <Input
                                    id="email"
                                    type="email"
                                    className="mt-1 bg-white shadow-sm"
                                    value={form.email}
                                    onChange={(e) => setForm(f => ({ ...f, email: e.target.value }))}
                                />
                                {errors.email && <p className="text-xs text-red-600 mt-1">{errors.email}</p>}
                            </div>
                        </div>

                        <div className="flex justify-between items-start mt-8 pt-6">
                            <div className="flex-1 pr-8">
                                <div className="text-sm font-medium text-gray-700">Unirse a la comunidad Stockia</div>
                                <p className="text-xs text-gray-500 mt-1">
                                    El cliente recibirá promociones y descuentos en su correo electrónico
                                </p>
                            </div>

                            <label className="relative inline-flex items-center cursor-pointer" aria-label="Unirse a la comunidad">
                                <input
                                    type="checkbox"
                                    className="sr-only peer"
                                    checked={form.esFrecuente}
                                    onChange={(e) => setForm(f => ({ ...f, esFrecuente: e.target.checked }))}
                                    aria-checked={form.esFrecuente}
                                />
                                <div className="w-[42px] h-[22px] bg-gray-200 peer-checked:bg-[#545F66] rounded-full transition-colors duration-300" />
                                <div className="pointer-events-none absolute inset-0 flex items-center px-1">
                                    <div
                                        className={`h-4 w-4 bg-white rounded-full shadow-lg transform transition-transform duration-300 ease-in-out ${form.esFrecuente ? 'translate-x-[18px]' : 'translate-x-0'}`}
                                    />
                                </div>
                            </label>
                        </div>

                        <div className="mt-10 flex justify-start gap-6 pt-6">
                            <Button
                                type="button"
                                variant="outline"
                                className="w-36 h-11 text-[#404040] border-gray-300 hover:bg-gray-200"
                                onClick={handleClose}
                            >
                                Cancelar
                            </Button>
                            <Button
                                type="submit"
                                className="w-44 h-11 bg-[#436086] hover:bg-[#364d6e] text-white font-medium"
                            >
                                {isEditMode ? 'Guardar cambios' : 'Guardar cliente'}
                            </Button>
                        </div>
                    </form>
                )}

                {isSubmitting && (
                    <div className="absolute inset-0 flex items-center justify-center bg-[#F4F5F7] rounded-xl">
                        <div className="flex flex-col items-center gap-4">
                            <Loader className="h-12 w-12 text-[#436086] animate-spin" />
                            <p className="text-sm text-gray-600">Guardando cliente...</p>
                        </div>
                    </div>
                )}

                {showSuccess && (
                    <div className="absolute inset-0 flex items-center justify-center bg-[#F4F5F7] rounded-xl">
                        <SuccessModal
                            title="¡Listo!"
                            description="Cliente registrado correctamente."
                            primaryButtonText="Registrar cliente"
                            secondaryButtonText="Volver"
                            onPrimaryClick={handleRegisterAnother}
                            onSecondaryClick={handleClose}
                            showButtons={true}
                        />
                    </div>
                )}
            </div>
        </div>
    );
}
