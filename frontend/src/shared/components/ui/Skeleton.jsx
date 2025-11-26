import React from "react";

export default function Skeleton({ className = "", width = "full", height = "4", lines = 1 }) {
    const lineArray = Array.from({ length: lines });
    return (
        <div className={`animate-pulse ${className}`}>
            {lineArray.map((_, i) => (
                <div key={i} className={`bg-gray-200 rounded ${i === lineArray.length - 1 ? "" : "mb-2"} w-${width} h-${height}`} />
            ))}
        </div>
    );
}