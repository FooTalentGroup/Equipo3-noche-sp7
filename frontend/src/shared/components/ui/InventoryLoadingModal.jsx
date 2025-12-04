import { Loader } from 'lucide-react';

export function InventoryLoadingModal() {
    return (
        <div className="fixed inset-0 z-[2000] flex items-center justify-center bg-black/40">
            <div className="px-44 py-56 bg-[#F4F5F7] rounded-2xl inline-flex justify-start items-center gap-2.5">
                <div className="w-80 inline-flex flex-col justify-start items-center gap-6">
                    <div className="self-stretch flex flex-col justify-start items-center gap-12">
                        <div className="w-40 h-40 relative overflow-hidden flex items-center justify-center">
                            <Loader className="w-28 h-28 text-[#436086] animate-spin" strokeWidth={2.5} />
                        </div>
                        <div className="self-stretch h-16 text-center justify-center text-[#171717] text-3xl font-semibold font-['Roboto_Flex']">
                            Actualizando inventario...
                        </div>
                    </div>
                    <div className="w-64 h-7 text-center justify-center text-[#171717] text-xs font-normal font-['Roboto_Flex']">
                        Estamos actualizando el inventario, por favor espera…
                    </div>
                </div>
            </div>
        </div>
    );
}