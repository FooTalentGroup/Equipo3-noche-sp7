import React from 'react';
import { Pencil } from 'lucide-react';

export default function UsersTable({ users = [], onEdit, onDelete, isLoading = false }) {
  if (isLoading) {

    return (
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden max-w-[1116px] max-h-[673px] flex items-center justify-center">
        <div className="flex flex-col items-center justify-center gap-3 my-4">
          <div className="h-8 w-8 border-4 border-gray-300 rounded-full animate-spin" />
          <span className="text-sm text-gray-600">Cargando lista de usuarios...</span>
        </div>
      </div>
    );
  }

  return (
    <div className="w-[1116px] rounded-lg shadow-[0px_4px_4px_0px_rgba(0,0,0,0.25)] inline-flex flex-col justify-start items-start overflow-hidden bg-white">
      <div className="overflow-x-auto w-full">
        <table className="w-full text-sm border-collapse">
          <thead className="text-base bg-[#EAEEF4] text-[#485056] flex-1 self-stretch text-center justify-center font-medium h-[46px]">
            <tr>
              <th className="px-6 py-3 text-base bg-[#EAEEF4] text-[#485056] flex-1 self-stretch text-center justify-center font-medium">NOMBRE</th>
              <th className="px-6 py-3 text-base bg-[#EAEEF4] text-[#485056] flex-1 self-stretch text-center justify-center font-medium">CORREO ELÉCTRONICO</th>
              <th className="px-6 py-3 text-base bg-[#EAEEF4] text-[#485056] flex-1 self-stretch text-center justify-center font-medium">ROL</th>
              <th className="px-6 py-3 text-base bg-[#EAEEF4] text-[#485056] flex-1 self-stretch text-center justify-center font-medium">ACCIONES</th>
            </tr>
          </thead>

          <tbody className="divide-y divide-Stockia-Neutral-100---Disabled">
            {users.length === 0 ? (
              <tr>
                <td colSpan={4} className="px-6 py-6 text-center text-gray-500 text-sm">
                  No hay usuarios para mostrar.
                </td>
              </tr>
            ) : (
              users.map((u) => (
                <tr key={u.id} className="bg-[#F5F7FA]">
                  <td className="px-6 py-4 font-normal text-[#202326] text-[14px] flex-1 self-stretch text-center justify-center w-[234px] h-[24px]">{u.nombre}</td>
                  <td className="px-6 py-4 font-normal text-[#202326] text-[14px] flex-1 self-stretch text-center justify-center w-[234px] h-[24px]">{u.email}</td>
                  <td className="px-6 py-4 font-normal text-[#202326] text-[14px] flex-1 self-stretch text-center justify-center w-[234px] h-[24px]">
                    <div data-etiqueta={u.role} className={`inline-flex px-2.5 py-1.5 rounded-2xl items-center justify-center ${u.role === 'ADMINISTRADOR' ? 'bg-[#CBE7CF] text-[#2C4F31] text-[12px] font-normal w-full' : 'bg-[#D0DAE7] text-[#263243] text-[12px] font-normal w-full'}`}>
                      <span className="text-xs font-normal">{u.role}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-center w-[234px] h-[24px]">
                    <div className="flex items-center justify-center gap-3">
                      <button onClick={() => onEdit?.(u)} className="w-[72.25px] min-h-6 px-2 py-[3px] bg-unofficial-outline/10 rounded-sm shadow-[0px_1px_2px_0px_rgba(0,0,0,0.05)] outline-1 outline-unofficial-border-3 flex items-center justify-center gap-[6px] cursor-pointer">
                        <Pencil className="text-[#202326] h-[14px] w-[14px]"/>
                          <span className="text-[#202326] text-sm font-medium">Editar</span>
                      </button>
                      <button onClick={() => onDelete?.(u.id)} className="w-[72.25px] min-h-6 px-2 py-[3px] bg-[#C93939] rounded-sm shadow-[0px_2px_4px_0px_rgba(0,0,0,0.25)] flex items-center justify-center gap-[6px] cursor-pointer">
                          <svg xmlns="http://www.w3.org/2000/svg" width="14" height="15" viewBox="0 0 14 15" fill="none">
                              <path d="M8.2959 0.0126953C8.78596 0.0712296 9.21935 0.325919 9.53027 0.636719C9.88525 0.991833 10.1658 1.50619 10.166 2.08301V2.66699H12.75C13.1642 2.66699 13.5 3.00278 13.5 3.41699C13.5 3.83121 13.1642 4.16699 12.75 4.16699H12.166V12.75C12.166 13.3272 11.8855 13.842 11.5303 14.1973C11.175 14.5524 10.6602 14.834 10.083 14.834H3.41602C2.83911 14.8338 2.32485 14.5523 1.96973 14.1973C1.61454 13.842 1.33301 13.3272 1.33301 12.75V4.16699H0.75C0.335786 4.16699 0 3.83121 0 3.41699C0 3.00278 0.335786 2.66699 0.75 2.66699H3.33301V2.08301C3.33322 1.50602 3.6146 0.991845 3.96973 0.636719C4.32485 0.281593 4.83902 0.000211406 5.41602 0H8.08301L8.2959 0.0126953ZM2.83301 12.75C2.83301 12.8394 2.88566 12.992 3.03027 13.1367C3.17461 13.281 3.32655 13.3337 3.41602 13.334H10.083C10.1724 13.334 10.325 13.2813 10.4697 13.1367C10.6143 12.992 10.666 12.8394 10.666 12.75V4.16699H2.83301V12.75ZM5.41602 1.5C5.32652 1.50025 5.17466 1.55288 5.03027 1.69727C4.88588 1.84166 4.83326 1.99351 4.83301 2.08301V2.66699H8.66602V2.08301C8.66576 1.99358 8.61387 1.84156 8.46973 1.69727C8.36145 1.58911 8.24829 1.53228 8.16113 1.51074L8.08301 1.5H5.41602Z" fill="white"/>
                          </svg>
                        <span className="text-sm font-medium text-white">Borrar</span>
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
