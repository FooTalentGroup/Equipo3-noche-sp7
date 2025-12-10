import React, { useEffect, useRef } from 'react';
import { useNotifications } from '../hooks/useNotifications';

export function NotificationsDropdown({
                                          className = '', onClose = () => {
    }
                                      }) {
    const {notifications, fetchList, fetchUnread, markAllRead, loading} = useNotifications();
    const rootRef = useRef(null);

    useEffect(() => {
        const handleDocClick = (e) => {
            if (!rootRef.current) return;
            if (!rootRef.current.contains(e.target)) {
                onClose();
            }
        };
        document.addEventListener('mousedown', handleDocClick);
        return () => document.removeEventListener('mousedown', handleDocClick);
    }, [onClose]);

    useEffect(() => {
        // fetch the list when the dropdown mounts
        fetchList();
    }, [fetchList]);

    const handleMarkAll = async () => {
        try {
            await markAllRead();
            fetchUnread();
        } catch (err) {
            console.error('Mark all read failed', err);
        }
    };

    return (
        <div ref={rootRef} className={`w-[450px] bg-white rounded-[8px] shadow pt-2 ${className}`}>
            <div className="flex items-center justify-between mb-4">
                <div className="text-sm font-medium ml-2">Notificaciones</div>
                <div className="flex items-center gap-2">
                    <button onClick={handleMarkAll} className="text-xs text-[#436086]">Marcar todas como leídas</button>
                </div>
            </div>

            <div className="max-h-[420px] overflow-auto">
                {loading ? (
                    <div className="text-sm text-gray-500">Cargando...</div>
                ) : notifications.length === 0 ? (
                    <div className="text-sm text-gray-500">No hay notificaciones</div>
                ) : notifications.map(n => (
                    <div key={n.id}
                         className={`self-stretch h-auto pl-1.5 pr-4 py-2 max--w-[410px] bg-[#F4F5F7] border-b-[#A8B3B8] outline-1 outline-offset-[-0.50px] inline-flex justify-start items-center gap-2 ${n.isRead ? 'opacity-60' : ''}`}>
                        <div data-l="circle-alert" className="size-6 relative overflow-hidden mr-2">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                                 fill="none">
                                <path
                                    d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z"
                                    stroke="#C56231" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
                                <path d="M12 8V12" stroke="#C56231" strokeWidth="1.5" strokeLinecap="round"
                                      strokeLinejoin="round"/>
                                <path d="M12 16H12.01" stroke="#C56231" strokeWidth="1.5" strokeLinecap="round"
                                      strokeLinejoin="round"/>
                            </svg>
                        </div>
                        <div className="flex-1 inline-flex flex-col">
                            <div className="text-black text-sm">{n.message.split('\n')[0] ?? n.message}</div>
                            {/*<div className="text-black text-xs mt-1">{n.message}</div>*/}
                            {/*<div className="text-xs text-gray-400 mt-1">{new Date(n.createdAt).toLocaleString()}</div>*/}
                        </div>
                        {/*<div className="flex flex-col items-end gap-2">*/}
                        {/*  {!n.isRead && <button onClick={() => handleMarkRead(n.id)} className="px-3 py-1 bg-[#436086] text-white rounded text-sm">Marcar leída</button>}*/}
                        {/*</div>*/}
                    </div>
                ))}
            </div>
            <div className="h-[24px] flex items-center justify-center ">
                    <button onClick={handleMarkAll} className="text-lg text-[#436086] cursor-pointer">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                            <path d="M12 13C12.5523 13 13 12.5523 13 12C13 11.4477 12.5523 11 12 11C11.4477 11 11 11.4477 11 12C11 12.5523 11.4477 13 12 13Z" stroke="#525252" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M19 13C19.5523 13 20 12.5523 20 12C20 11.4477 19.5523 11 19 11C18.4477 11 18 11.4477 18 12C18 12.5523 18.4477 13 19 13Z" stroke="#525252" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M5 13C5.55228 13 6 12.5523 6 12C6 11.4477 5.55228 11 5 11C4.44772 11 4 11.4477 4 12C4 12.5523 4.44772 13 5 13Z" stroke="#525252" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </button>

            </div>
        </div>
    );
}
