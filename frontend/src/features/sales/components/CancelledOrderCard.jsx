import React from "react";

export const CancelledOrderCard = ({ order }) => {
    return (
        <div className="bg-stokia-neutral-50 border border-border rounded-lg p-4 shadow-sm flex flex-col space-y-4">
            <div className="border-b border-muted pb-3">
                <h3 className="text-lg text-foreground text-center">
                    Pedido #{order.orderNumber || order.id}
                </h3>
            </div>

            <div className="flex-grow space-y-2">
                <div className="flex justify-between items-center text-sm font-medium">
                    <span className="text-foreground">Producto</span>
                    <span className="text-foreground">Cant.</span>
                </div>
                {order.products?.map((product, index) => (
                    <div
                        key={index}
                        className="flex justify-between items-center text-sm"
                    >
                        <span className="text-muted-foreground truncate flex-1 pr-2">
                            {product.name}
                        </span>
                        <span className="text-foreground font-medium min-w-[30px] text-right">
                            {product.quantity}
                        </span>
                    </div>
                ))}
            </div>
        </div>
    );
};
