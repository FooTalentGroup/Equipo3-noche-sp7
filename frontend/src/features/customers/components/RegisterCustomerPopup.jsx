import { useEffect, useState } from 'react';
import { Button } from '@/shared/components/ui/button';
import { Input } from '@/shared/components/ui/input';

export function RegisterCustomerPopup({ open, onClose, onSave, initialData = null }) {
    const [form, setForm] = useState({
        nombre: '',
        telefono: '',
        email: '',
        ultimaCompra: '',
        joined: true,
    });
    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (initialData) {
            setForm({
                nombre: initialData.nombre || '',
                telefono: initialData.telefono || '',
                email: initialData.email || '',
                ultimaCompra: initialData.ultimaCompra || '',
                joined: !!initialData.joined,
                id: initialData.id,
            });
        } else {
            setForm({
                nombre: '',
                telefono: '',
                email: '',
                ultimaCompra: '',
                joined: true,
            });
        }
        setErrors({});
    }, [initialData, open]);

    if (!open) return null;

    function validate() {
        const e = {};
        if (!form.nombre || !String(form.nombre).trim()) e.nombre = 'Nombre es obligatorio';
        if (!form.telefono || !String(form.telefono).trim()) e.telefono = 'Numero telefonico es obligatorio';
        if (!form.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) e.email = 'Correo electronico válido es obligatorio';
        setErrors(e);
        return Object.keys(e).length === 0;
    }

    function submit(e) {
        e?.preventDefault();
        if (!validate()) return;
        // ensure consistent payload
        const payload = {
            id: form.id,
            nombre: form.nombre.trim(),
            telefono: form.telefono.trim(),
            email: form.email.trim(),
            ultimaCompra: form.ultimaCompra || null,
            joined: !!form.joined,
        };
        onSave && onSave(payload);
    }

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
            <div className="absolute inset-0 bg-black/40" onClick={onClose} />
            <form
                className="relative w-[700px] max-w-full bg-[#F4F5F7] rounded-xl shadow-lg p-10 z-10 flex flex-col"
                style={{ minHeight: '500px' }}
                onSubmit={submit}
            >
                <h3 className="text-lg font-semibold">Registrar cliente</h3>
                <p className="text-xs text-gray-500 mt-1">
                    Completa este formulario para registrar un nuevo cliente
                </p>
                <hr className='my-6 border-gray-300' />

                <div className="space-y-5 flex-1">
                    <label className="block">
                        <div className="text-sm font-medium text-gray-700">Nombre</div>
                        <Input
                            className="mt-1 bg-white shadow-sm"
                            value={form.nombre}
                            onChange={(e) => setForm(f => ({ ...f, nombre: e.target.value }))}
                        />
                        {errors.nombre && <p className="text-xs text-red-600 mt-1">{errors.nombre}</p>}
                    </label>

                    <label className="block">
                        <div className="text-sm font-medium text-gray-700">Numero telefonico</div>
                        <Input
                            className="mt-1 bg-white shadow-sm"
                            value={form.telefono}
                            onChange={(e) => setForm(f => ({ ...f, telefono: e.target.value }))}
                        />
                        {errors.telefono && <p className="text-xs text-red-600 mt-1">{errors.telefono}</p>}
                    </label>

                    <label className="block">
                        <div className="text-sm font-medium text-gray-700">Correo electronico</div>
                        <Input
                            className="mt-1 bg-white shadow-sm"
                            value={form.email}
                            onChange={(e) => setForm(f => ({ ...f, email: e.target.value }))}
                        />
                        {errors.email && <p className="text-xs text-red-600 mt-1">{errors.email}</p>}
                    </label>
                </div>

                <div className="flex justify-between items-start mt-8 pt-6">
                    <div className="flex-1 pr-8">
                        <div className="text-sm font-medium text-gray-700">Unirse a la comunidad Pro-eat</div>
                        <p className="text-xs text-gray-500 mt-1">
                            El cliente recibirá promociones y descuentos en su correo electrónico
                        </p>
                    </div>

                    <div className="shrink-0">
                        <label className="relative inline-flex items-center cursor-pointer">
                            <input
                                type="checkbox"
                                className="sr-only peer"
                                checked={!!form.joined}
                                onChange={(e) => setForm(f => ({ ...f, joined: e.target.checked }))}
                            />
                            <div className="w-[33px] h-[18px] bg-gray-200 peer-checked:bg-[#545F66] rounded-full transition-colors duration-300 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-[#545F66]/30" />
                            <div className="pointer-events-none absolute inset-0 flex items-center px-1">
                                <div className={`h-4 w-4 bg-white rounded-full shadow-lg transform transition-transform duration-300 ease-in-out ${form.joined ? 'translate-x-3' : 'translate-x-0'}`} />
                            </div>
                        </label>
                    </div>
                </div>

                <div className="mt-10 flex justify-start gap-6 pt-6">
                    <Button
                        type="button"
                        variant="outline"
                        className="w-36 h-11 text-[#404040] border-gray-300 hover:bg-gray-200"
                        onClick={onClose}
                    >
                        Cancelar
                    </Button>

                    <Button
                        type="submit"
                        className="w-44 h-11 bg-[#436086] hover:bg-[#364d6e] text-white font-medium"
                    >
                        Guardar cliente
                    </Button>
                </div>
            </form>
        </div>
    );
}