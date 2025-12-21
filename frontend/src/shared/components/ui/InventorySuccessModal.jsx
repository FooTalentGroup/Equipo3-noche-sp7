import { Check } from 'lucide-react';

export function InventorySuccessModal() {
    return (
        <div className="fixed inset-0 z-2000 flex items-center justify-center bg-black/40">
            <div className="px-44 py-56 bg-[#F4F5F7] rounded-2xl inline-flex justify-start items-center gap-2.5">
                <div className="w-80 inline-flex flex-col justify-start items-center gap-12">
                    <div className="w-40 h-40 relative overflow-hidden flex items-center justify-center">
                        <div className="w-32 h-32 rounded-full border-8 border-[#2C5282] flex items-center justify-center">
                            <Check className="w-16 h-16 text-[#2C5282]" strokeWidth={3} />
                        </div>
                    </div>
                    <div className="self-stretch flex flex-col justify-start items-center gap-6">
                        <div className="self-stretch h-16 text-center justify-center text-gray-900 text-3xl font-semibold font-['Roboto_Flex']">
                            ¡Inventario actualizado exitosamente!
                        </div>
                        <div className="w-64 h-7 text-center justify-center text-gray-900 text-xs font-normal font-['Roboto_Flex']">
                            Serás redirigido a "Movimientos de inventario" en 5 segundos.
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
