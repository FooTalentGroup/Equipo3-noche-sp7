import React from 'react';
import { Bell } from 'lucide-react';
import { Button } from '@/shared/components/ui/button';
import { useLocation } from 'react-router-dom';

const PAGE_LABELS = [
    { path: '/products', label: 'Productos' },
    { path: '/sales', label: 'Ventas' },
    { path: '/customers', label: 'Clientes' },
    { path: '/suppliers', label: 'Proveedores' },
    { path: '/discounts', label: 'Descuentos' },
    { path: '/predictions-IA', label: 'Predicciones de IA' },
    { path: '/reports', label: 'Reportes' },
    { path: '/home', label: 'Inicio' },
];

export function TopBar() {
    const { pathname } = useLocation();

    const pageLabel =
        PAGE_LABELS.find(p => pathname.startsWith(p.path))?.label ||
        (pathname.split('/').filter(Boolean)[0] || 'Panel');

    return (
        <div
            className="top-0 z-40 flex items-center"
            style={{
                display: 'flex',
                width: '1188px',
                height: '84px',
                padding: '10px 0',
                alignItems: 'center',
                gap: '120px',
                flexShrink: 0,
                borderRadius: '5px',
                borderRight: '1px solid #E5E7EB',
                borderBottom: '1px solid #E5E7EB',
                borderLeft: '1px solid #E5E7EB',
                background: '#FFF',
            }}
        >
            <div className="pl-6">
                <h2 className="text-lg font-semibold text-gray-800">{pageLabel}</h2>
            </div>

            <div className="flex-1" />

            <button
                aria-label="Notificaciones"
                className="
    flex w-[143px] min-h-9 
    py-[7.5px] px-var(--4,16px)
    justify-center items-center 
    gap-var(--2,8px) shrink-0
    
    rounded-(--rounded-8,8px)
    border border-(--unofficial-border-3,#D4D4D4)
    bg-(--unofficial-outline,rgba(255,255,255,0.10))
    
    shadow-sm cursor-pointer
  "
            >
                <Bell className="h-5 w-5" />
                <span className="hidden sm:inline">Notificaciones</span>
            </button>
        </div>
    );
}