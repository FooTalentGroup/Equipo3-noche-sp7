// components/Badge.jsx
import React from "react";
import { BadgeCheck, TriangleAlert, OctagonX } from "lucide-react";

const cn = (...classes) => classes.filter(Boolean).join(" ");

const Badge = ({ variant = "success", children, className, ...props }) => {
    const variants = {
        success: "bg-[#3C7D45]",
        warning: "bg-[#C56231]",
        destructive: "bg-[#C93939]",
    };

    const baseClasses = cn(
        "flex items-center justify-center",
        "rounded-[4px]",
        variants[variant],
        className
    );

    let icon = null;
    const text = typeof children === 'string' ? children.trim().toLowerCase() : '';
    // Precise icon layout and style
    const iconStyle = {
        width: '15px',
        height: '15px',
        color: '#FAFAFA',
    };
    if (text === "alto stock") icon = <BadgeCheck style={iconStyle} />;
    else if (text === "bajo stock") icon = <TriangleAlert style={iconStyle} />;
    else if (text === "sin stock") icon = <OctagonX style={iconStyle} />;

    return (
        <span
            className={baseClasses}
            style={{
                width: '100px',
                height: '24px',
                minHeight: '24px',
                borderRadius: '4px',
                paddingTop: '3px',
                paddingBottom: '3px',
                paddingLeft: '5px',
                opacity: 1,
                angle: '0deg'
            }}
            {...props}
        >
            {icon && (
                <span className="flex items-center justify-center">
                    {icon}
                </span>
            )}
            <span
                className="overflow-hidden text-ellipsis text-center font-medium"
                style={{
                    fontFamily: `'Geist', 'Roboto Flex', system-ui, sans-serif`,
                    fontSize: '12px',
                    fontStyle: 'normal',
                    lineHeight: '18px',
                    letterSpacing: '0.18px',
                    color: 'var(--general-primary-foreground, #FAFAFA)',
                    display: '-webkit-box',
                    WebkitLineClamp: 1,
                    WebkitBoxOrient: 'vertical',
                    textOverflow: 'ellipsis',
                    overflow: 'hidden',
                    minWidth: 0,
                    flex: '1 0 0',
                }}
            >
                {children}
            </span>
        </span>
    );
};

export default Badge;