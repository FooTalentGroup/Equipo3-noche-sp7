import React, { useState, useRef, useEffect } from "react";
import { Bell } from "lucide-react";
import { Button } from "@/shared/components/ui/button";
import { useLocation } from "react-router-dom";
import { NotificationsDropdown } from '@/features/notifications/components/NotificationsDropdown';
import { useNotifications } from '@/features/notifications/hooks/useNotifications';

const PAGE_LABELS = [
  { path: "/products", label: "Producto" },
  { path: "/users", label: "Administrador" },
  { path: "/inventory-movements", label: "Historial de movimientos", parent: "Producto" },
  { path: "/sales", label: "Ventas" },
  { path: "/customers", label: "Clientes" },
  { path: "/suppliers", label: "Proveedores" },
  { path: "/discounts", label: "Descuentos" },
  { path: "/predictions-IA", label: "Predicciones de IA" },
  { path: "/reports", label: "Reportes" },
  { path: "/home", label: "Inicio" },
];

export function TopBar() {
  const { pathname } = useLocation();
  const [showNotif, setShowNotif] = useState(false);
  const btnRef = useRef(null);
  const { unreadCount, fetchUnread, fetchList, showLowStockNotification } = useNotifications();

  const toggleNotif = async () => {
    setShowNotif(s => !s);
    if (!showNotif) {
      fetchUnread();
      try {
        await fetchList();
        if (typeof window !== 'undefined' && 'Notification' in window && Notification.permission === 'default') {
          Notification.requestPermission().then((perm) => {
            if (perm === 'granted') {
              try { showLowStockNotification(); } catch (e) { /* ignore */ }
            }
          }).catch(() => {
          });
        } else {
          try { showLowStockNotification(); } catch (e) { /* ignore */ }
        }
      } catch (e) { /* ignore */ }
    }
  };

  useEffect(() => {
    try {
      fetchUnread();
      fetchList();
    } catch (e) {
    }
  }, [pathname, fetchUnread, fetchList]);

  const currentPage = PAGE_LABELS.find((p) => pathname.startsWith(p.path));
  const pageLabel = currentPage?.label || pathname.split("/").filter(Boolean)[0] || "Panel";
  const hasParent = currentPage?.parent;

  return (
    <div className="top-0 z-40 flex items-center border py-2.5 px-8 h-20 rounded-sm">
      {hasParent ? (
        <div className="min-h-9 pl-4 pr-1.5 py-2 opacity-60 rounded-lg inline-flex justify-start items-center gap-2">
          <div className="text-center justify-center text-[#404040] text-[14px] font-normal  leading-5 tracking-tight">
            {currentPage.parent} &gt;
          </div>
          <div className="text-center justify-center text-[#404040] text-[14px] font-medium  leading-5 tracking-tight">
            {pageLabel}
          </div>
        </div>
      ) : (
        currentPage?.path === '/users' ? (
          <h2 className="text-[14px] text-[#404040] font-medium">{pageLabel}</h2>
        ) : (
          <h2 className="text-lg font-semibold">{pageLabel}</h2>
        )
      )}

      <div className="flex-1" />

      <div className="relative">
        <Button variant="outline" onClick={toggleNotif} ref={btnRef}>
          <div className="relative">
            <Bell className="h-5 w-5" />
            {unreadCount > 0 && (
              <span className="h-5 absolute -top-3 -left-12 inline-flex items-center justify-center px-2 py-0.5 text-xs font-semibold leading-none text-white bg-red-600 rounded-tl-sm rounded-tr-sm rounded-bl-sm">{unreadCount}</span>
            )}
          </div>
          <span className="hidden sm:inline">Notificaciones</span>
        </Button>

        {showNotif && (
          <div className="absolute right-0 mt-2 z-50">
            <NotificationsDropdown className="shadow-lg" onClose={() => setShowNotif(false)} />
          </div>
        )}
      </div>
    </div>
  );
}
