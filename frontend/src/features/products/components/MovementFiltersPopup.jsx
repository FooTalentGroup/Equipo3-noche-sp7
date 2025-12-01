import { X } from 'lucide-react';
import { Button } from '@/shared/components/ui/button.jsx';
import { Input } from '@/shared/components/ui/input.jsx';
import { useState, useEffect } from 'react';

export function MovementFiltersPopup({ open, onClose, filters, onApplyFilters }) {
    const [localFilters, setLocalFilters] = useState({
        startDate: '',
        endDate: '',
        userName: '',
        movementType: 'all',
    });

    useEffect(() => {
        if (open) {
            setLocalFilters(filters);
        }
    }, [open, filters]);

    if (!open) return null;

    const handleApply = () => {
        onApplyFilters(localFilters);
        onClose();
    };

    const handleClear = () => {
        const cleared = {
            startDate: '',
            endDate: '',
            userName: '',
            movementType: 'all',
        };
        setLocalFilters(cleared);
        onApplyFilters(cleared);
        onClose();
    };

    return (
        <div className="fixed inset-0 z-[1000] flex items-center justify-center bg-black/40">
            <div className="bg-white rounded-lg shadow-xl w-full max-w-md mx-4">
                <div className="flex items-center justify-between p-6 border-b">
                    <h3 className="text-lg font-semibold text-gray-900">Filtrar movimientos</h3>
                    <button
                        onClick={onClose}
                        className="text-gray-400 hover:text-gray-600 transition"
                    >
                        <X className="h-5 w-5" />
                    </button>
                </div>

                <div className="p-6 space-y-4">
                    {/* Date Range Filter */}
                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700">
                            Rango de fechas
                        </label>
                        <div className="grid grid-cols-2 gap-3">
                            <div>
                                <label className="text-xs text-gray-500 mb-1 block">Desde</label>
                                <Input
                                    type="date"
                                    value={localFilters.startDate}
                                    onChange={(e) =>
                                        setLocalFilters({ ...localFilters, startDate: e.target.value })
                                    }
                                    className="w-full"
                                />
                            </div>
                            <div>
                                <label className="text-xs text-gray-500 mb-1 block">Hasta</label>
                                <Input
                                    type="date"
                                    value={localFilters.endDate}
                                    onChange={(e) =>
                                        setLocalFilters({ ...localFilters, endDate: e.target.value })
                                    }
                                    className="w-full"
                                />
                            </div>
                        </div>
                    </div>

                    {/* User Filter */}
                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700">
                            Filtrar por usuario
                        </label>
                        <Input
                            type="text"
                            placeholder="Nombre del usuario"
                            value={localFilters.userName}
                            onChange={(e) =>
                                setLocalFilters({ ...localFilters, userName: e.target.value })
                            }
                            className="w-full"
                        />
                    </div>

                    {/* Movement Type Filter */}
                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700">
                            Tipo de movimiento
                        </label>
                        <div className="flex gap-2">
                            <Button
                                type="button"
                                variant={localFilters.movementType === 'all' ? 'default' : 'outline'}
                                onClick={() => setLocalFilters({ ...localFilters, movementType: 'all' })}
                                className={`flex-1 ${localFilters.movementType === 'all' ? 'bg-[#436086] hover:bg-slate-900' : ''}`}
                            >
                                Todos
                            </Button>
                            <Button
                                type="button"
                                variant={localFilters.movementType === 'IN' ? 'default' : 'outline'}
                                onClick={() => setLocalFilters({ ...localFilters, movementType: 'IN' })}
                                className={`flex-1 ${localFilters.movementType === 'IN' ? 'bg-[#436086] hover:bg-slate-900' : ''}`}
                            >
                                Entrada
                            </Button>
                            <Button
                                type="button"
                                variant={localFilters.movementType === 'OUT' ? 'default' : 'outline'}
                                onClick={() => setLocalFilters({ ...localFilters, movementType: 'OUT' })}
                                className={`flex-1 ${localFilters.movementType === 'OUT' ? 'bg-[#436086] hover:bg-slate-900' : ''}`}
                            >
                                Salida
                            </Button>
                        </div>
                    </div>
                </div>

                <div className="flex gap-3 p-6 border-t">
                    <Button
                        type="button"
                        variant="outline"
                        onClick={handleClear}
                        className="flex-1"
                    >
                        Limpiar filtros
                    </Button>
                    <Button
                        type="button"
                        onClick={handleApply}
                        className="flex-1 bg-[#436086] hover:bg-slate-900"
                    >
                        Aplicar filtros
                    </Button>
                </div>
            </div>
        </div>
    );
}
