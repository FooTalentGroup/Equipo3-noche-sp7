import { Menu, Transition } from "@headlessui/react";
import { Edit } from "lucide-react";
import { Fragment, useState } from "react";
import { ProductAdjustmentDialog } from "./ProductAdjustmentDialog.jsx";

export default function ActionsMenu({ product }) {
    const [isDialogOpen, setIsDialogOpen] = useState(false);

    return (
        <>
            <Menu as="div" className="relative inline-block text-left">
                <Menu.Button className="p-2 hover:bg-gray-100 text-gray-500 cursor-pointer transition">
                    <Edit className="w-4 h-4" />
                </Menu.Button>

                <Transition
                    as={Fragment}
                    enter="transition ease-out duration-100"
                    enterFrom="opacity-0 translate-y-1"
                    enterTo="opacity-100 translate-y-0"
                    leave="transition ease-in duration-75"
                    leaveFrom="opacity-100 translate-y-0"
                    leaveTo="opacity-0 translate-y-1"
                >
                    <Menu.Items
                        className="
                        absolute right-0 mt-2 
                        flex flex-col items-center justify-center
                        w-[217px] p-0.5
                        rounded-xl
                        border border-[#A8B3B8]
                        bg-[#F4F5F7]
                        shadow-lg z-50
                        focus:outline-none
                    "
                    >
                        <div className="w-full">
                            <Menu.Item>
                                {({ active }) => (
                                    <button
                                        onClick={() => setIsDialogOpen(true)}
                                        className={`
                                        ${active ? "bg-gray-200" : ""}
                                        w-full text-left px-4 py-2
                                        text-[14px] leading-[21px] tracking-[0.07px]
                                        font-normal 
                                        text-[#0A0A0A]
                                    `}
                                    >
                                        Editar
                                    </button>
                                )}
                            </Menu.Item>

                            <Menu.Item>
                                {({ active }) => (
                                    <button
                                        className={`
                                        ${active ? "bg-gray-200" : ""}
                                        w-full text-left px-4 py-2
                                        text-[14px] leading-[21px] tracking-[0.07px]
                                        font-normal 
                                        text-[#0A0A0A]
                                        font-[]
                                    `}
                                    >
                                        Ver historial del producto
                                    </button>
                                )}
                            </Menu.Item>

                            <Menu.Item>
                                {({ active }) => (
                                    <button
                                        className={`
                                        ${active ? "bg-gray-200" : ""}
                                        w-full text-left px-4 py-2
                                        text-[14px] leading-[21px] tracking-[0.07px]
                                        font-normal 
                                        text-[#0A0A0A]
                                        font-[]
                                    `}
                                    >
                                        Movimientos de inventario
                                    </button>
                                )}
                            </Menu.Item>

                            <Menu.Item>
                                {({ active }) => (
                                    <button
                                        className={`
                                        ${active ? "bg-gray-200" : ""}
                                        w-full text-left px-4 py-2
                                        text-[14px] leading-[21px] tracking-[0.07px]
                                        font-normal 
                                        text-red-600
                                        font-[]
                                    `}
                                    >
                                        Borrar
                                    </button>
                                )}
                            </Menu.Item>
                        </div>
                    </Menu.Items>
                </Transition>
            </Menu>

            <ProductAdjustmentDialog
                open={isDialogOpen}
                onOpenChange={setIsDialogOpen}
                product={product}
            />
        </>
    );
}
