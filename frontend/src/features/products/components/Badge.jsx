// components/Badge.jsx
import React from "react";

const cn = (...classes) => classes.filter(Boolean).join(" ");

const Badge = ({ variant = "success", children, className, ...props }) => {
    const variants = {
        // Alto stock → Verde sólido
        success: "bg-[#3C7D45] text-white",

        // Bajo stock (1 a 10 unidades) → Naranja sólido
        warning: "bg-[#C56231] text-white",

        // Sin stock (0 unidades) → Rojo sólido
        destructive: "bg-[#C93939] text-white",
    };

    const baseClasses = cn(
        "inline-flex items-center justify-center",
        "w-full min-h-6 h-6", // height: 24px → h-6 en Tailwind (24/4=6)
        "px-2",               // padding-left/right: 2 → pero como es pequeño, px-2 queda perfecto
        "py-0",               // padding-top/bottom controlado por line-height
        "rounded",            // border-radius: 4px → rounded en Tailwind
        "text-xs font-semibold leading-6", // Alineación vertical perfecta
        variants[variant],
        className
    );

    return (
        <span className={baseClasses} {...props}>
      {children}
    </span>
    );
};

export default Badge;