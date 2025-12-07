import React, { useEffect } from 'react';
import { useNotifications } from '../hooks/useNotifications';
import {CircleAlert} from "lucide-react";

export default function NotificationsDropdown({ className = '' }) {
  const { notifications, fetchList, fetchUnread, markRead, markAllRead, loading } = useNotifications();

  useEffect(() => {
    // fetch the list when the dropdown mounts
    fetchList();
  }, [fetchList]);

  const handleMarkRead = async (id) => {
    try {
      await markRead(id);
      // fetchUnread will be handled by hook state; call to reconcile if necessary
      fetchUnread();
    } catch (err) {
      console.error('Mark read failed', err);
    }
  };

  const handleMarkAll = async () => {
    try {
      await markAllRead();
      fetchUnread();
    } catch (err) {
      console.error('Mark all read failed', err);
    }
  };

  return (
    <div className={`w-[450px] bg-white rounded-[8px] shadow p-2 ${className}`}>
      <div className="flex items-center justify-between mb-4">
        <div className="text-sm font-medium">Notificaciones</div>
        <div className="flex items-center gap-2">
          <button onClick={handleMarkAll} className="text-xs text-[#436086]">Marcar todas como leídas</button>
        </div>
      </div>

      <div className="max-h-[420px] overflow-auto space-y-3">
        {loading ? (
          <div className="text-sm text-gray-500">Cargando...</div>
        ) : notifications.length === 0 ? (
          <div className="text-sm text-gray-500">No hay notificaciones</div>
        ) : notifications.map(n => (
          <div key={n.id} className={`self-stretch h-auto pl-1.5 pr-4 py-2 max--w-[410px] bg-[#F4F5F7] rounded-lg shadow-[0px_4px_4px_0px_rgba(0,0,0,0.25)] outline-1 outline-offset-[-0.50px] inline-flex justify-start items-center gap-2 ${n.isRead ? 'opacity-60' : ''}`}>
            <div data-l="circle-alert" className="size-6 relative overflow-hidden mr-2">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                    <path d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" stroke="#C56231" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M12 8V12" stroke="#C56231" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M12 16H12.01" stroke="#C56231" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
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

    </div>
  );
}
