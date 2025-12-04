import React from "react";

export const OrderTabs = ({ activeTab, onTabChange }) => {
  const tabs = [
    { id: "register", label: "Registrar pedido" },
    { id: "pending", label: "Pendiente de cobro" },
    { id: "confirmed", label: "Confirmado" },
    { id: "cancelled", label: "Cancelado" }
  ];

  return (
    <div className="w-full flex justify-center mb-6">
      <div className="h-[45px] bg-secondary  rounded-lg border border-gray-200 px-6 flex items-center gap-[75px]">
      {tabs.map((tab) => (
        <button
          key={tab.id}
          onClick={() => onTabChange(tab.id)}
          className={`px-4 py-2 text-sm font-medium transition-colors  ${
            activeTab === tab.id
              ? "border-b-2 border-primary text-btn-primary"
              : "text-foreground hover:text-btn-primary"
          }`}
        >
          {tab.label}
        </button>
      ))}
    </div>
    </div>
  );
};