import React, { useEffect, useState } from 'react';
import { getUsers, updateUser } from '../services/usersService';
import { Loader } from 'lucide-react';

const roleMap = {
  ADMIN: 'ADMINISTRADOR',
  MANAGER: 'ENCARGADO',
  USER: 'USUARIO',
};

export default function UsersHistoryModal({ open, onClose, onRestore }) {
  const [users, setUsers] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [restoring, setRestoring] = useState({});

  useEffect(() => {
    if (!open) return;
    fetchAllUsers();
  }, [open]);

  const fetchAllUsers = async () => {
    setIsLoading(true);
    try {
      // Request a large page to get all users; backend is already paginated
      const res = await getUsers({ page: 0, size: 1000 });
      const content = res.users || [];
      const mapped = content.map((u) => ({
        id: u.id ?? u._id ?? u.userId,
        nombre: u.name ?? u.nombre ?? '',
        email: u.email ?? '',
        role: roleMap[u.role] ?? (u.role ?? 'ENCARGADO'),
        rawRole: u.role,
        // normalize accountStatus to uppercase & trim for consistent checks
        accountStatus: String(u.accountStatus ?? 'ACTIVE').trim().toUpperCase(),
        deleted: !!u.deleted,
      }));
      setUsers(mapped);
    } catch (err) {
      console.error('Error fetching all users', err);
      setUsers([]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleRestore = async (user) => {
    setRestoring((s) => ({ ...s, [user.id]: true }));
    try {
      // Only send accountStatus to restore the user
      await updateUser(user.id, { accountStatus: 'ACTIVE' });
      // refresh modal list
      await fetchAllUsers();
      // notify parent so main users list (active users) can refresh
      try { onRestore?.(); } catch (err) { /* swallow */ }
    } catch (err) {
      console.error('Error restoring user', err);
    } finally {
      setRestoring((s) => ({ ...s, [user.id]: false }));
    }
  };

  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/40" onClick={onClose} />


        <div className="relative max-w-full bg-white rounded-xl shadow-lg z-60">
        {/*<div className="flex items-center justify-between mb-4">*/}
        {/*  <h3 className="text-lg font-semibold">Historial de usuarios</h3>*/}
        {/*  <button onClick={onClose} className="text-sm text-gray-600">Cerrar</button>*/}
        {/*</div>*/}

        <div className="w-[1116px] rounded-lg shadow-[0px_4px_4px_0px_rgba(0,0,0,0.25)] overflow-hidden">
          {/* Scrollable container for table body */}
          <div className="overflow-auto w-full max-h-[460px]">
            {/* use table-fixed so columns distribute evenly; each th gets w-1/4 */}
            <table className="w-full text-sm border-collapse table-fixed">
              <colgroup>
                <col className="w-1/4" />
                <col className="w-1/4" />
                <col className="w-1/4" />
                <col className="w-1/4" />
              </colgroup>
              <thead className="text-base bg-[#EAEEF4] text-[#485056] text-center font-medium h-[46px] sticky top-0">
                <tr>
                  <th className="w-1/4 px-6 py-3 text-center">NOMBRE</th>
                  <th className="w-1/4 px-6 py-3 text-center">CORREO ELÉCTRONICO</th>
                  <th className="w-1/4 px-6 py-3 text-center">ROL</th>
                  <th className="w-1/4 px-6 py-3 text-center">ACCIONES</th>
                </tr>
              </thead>

              <tbody className="divide-y divide-Stockia-Neutral-100---Disabled">
                {isLoading ? (
                  <tr>
                    <td colSpan={4} className="p-6 text-center">
                      <Loader className="h-8 w-8 animate-spin text-gray-600 mx-auto" />
                    </td>
                  </tr>
                ) : users.length === 0 ? (
                  <tr>
                    <td colSpan={4} className="p-6 text-center text-gray-500">No hay usuarios para mostrar.</td>
                  </tr>
                ) : (
                  users.map((u) => (
                    <tr key={u.id} className="bg-Stockia-Primary-50">
                      <td className="px-6 py-3 text-sm text-Stockia-Neutral-950">{u.nombre}</td>
                      <td className="px-6 py-3 text-sm text-Stockia-Neutral-950">{u.email}</td>
                      <td className="px-6 py-3 text-sm text-center">
                        <div
                          data-etiqueta={u.role}
                          className={`inline-flex px-2.5 py-1.5 rounded-2xl items-center justify-center ${u.role === 'ADMINISTRADOR' ? 'bg-[#CBE7CF] text-[#2C4F31] text-[12px] font-normal w-full' : 'bg-[#D0DAE7] text-[#263243] text-[12px] font-normal w-full'}`}
                        >
                          <span className="text-xs font-normal">{u.role}</span>
                        </div>
                      </td>
                      <td className="px-6 py-3 text-sm text-center align-middle">
                        {(u.accountStatus !== 'ACTIVE') ? (
                          <button
                            onClick={() => handleRestore(u)}
                            disabled={!!restoring[u.id]}
                            className="mx-auto inline-flex items-center justify-center gap-2 cursor-pointer h-8 px-3 bg-unofficial-outline/10 rounded-sm shadow-[0px_1px_2px_0px_rgba(0,0,0,0.05)] outline-unofficial-border-3"
                          >
                            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 14 14" fill="none" className="inline-block">
                              <path d="M6.08301 4.54199C6.49722 4.54199 6.83301 4.87778 6.83301 5.29199V6.14746L6.94629 6.03516C7.66171 5.31974 8.65736 4.87509 9.75 4.875C10.7444 4.87509 11.6982 5.27055 12.4014 5.97363C13.1045 6.67688 13.5 7.63051 13.5 8.625C13.5 9.48202 13.2296 10.3174 12.7285 11.0127C12.2275 11.7076 11.5208 12.228 10.708 12.499C9.89516 12.7699 9.01707 12.7779 8.19922 12.5225C7.3813 12.2669 6.66356 11.7607 6.14941 11.0752C5.9011 10.7439 5.96876 10.2739 6.2998 10.0254C6.63107 9.77694 7.10102 9.8437 7.34961 10.1748C7.67487 10.6085 8.12907 10.9291 8.64648 11.0908C9.16381 11.2523 9.71923 11.2475 10.2334 11.0762C10.7476 10.9048 11.1947 10.5754 11.5117 10.1357C11.8287 9.69589 12 9.1672 12 8.625C12 8.02834 11.7627 7.45612 11.3408 7.03418C10.919 6.6124 10.3465 6.37509 9.75 6.375C9.06956 6.37509 8.45128 6.65126 8.00684 7.0957L7.89355 7.20898H8.75C9.16406 7.20916 9.5 7.54488 9.5 7.95898C9.49965 8.37279 9.16385 8.70881 8.75 8.70898H6.08301C6.00211 8.70895 5.92553 8.69172 5.85254 8.66797C5.83376 8.66188 5.81425 8.658 5.7959 8.65039C5.74449 8.62899 5.69624 8.60208 5.65137 8.57031C5.61713 8.54609 5.58341 8.51989 5.55273 8.48926C5.5027 8.43919 5.46458 8.38114 5.43164 8.32227C5.41694 8.29599 5.40116 8.27029 5.38965 8.24219C5.38189 8.22322 5.37821 8.20301 5.37207 8.18359C5.34953 8.11231 5.33307 8.03768 5.33301 7.95898V5.29199C5.33301 4.87789 5.66894 4.54217 6.08301 4.54199ZM3.49316 9.87891C3.87144 9.91723 4.16699 10.2366 4.16699 10.625C4.16699 11.0134 3.87144 11.3328 3.49316 11.3711L3.41699 11.375H0.75C0.335786 11.375 0 11.0392 0 10.625C0 10.2108 0.335786 9.875 0.75 9.875H3.41699L3.49316 9.87891ZM3.49316 5.87891C3.87144 5.91723 4.16699 6.23661 4.16699 6.625C4.16699 7.01339 3.87144 7.33277 3.49316 7.37109L3.41699 7.375H0.75C0.335786 7.375 0 7.03921 0 6.625C0 6.21079 0.335786 5.875 0.75 5.875H3.41699L3.49316 5.87891ZM12.75 1.875C13.1642 1.875 13.5 2.21079 13.5 2.625C13.5 3.03921 13.1642 3.375 12.75 3.375H0.75C0.335786 3.375 0 3.03921 0 2.625C0 2.21079 0.335786 1.875 0.75 1.875H12.75Z" fill="#0A0A0A"/>
                            </svg>
                            {restoring[u.id] ? 'Restaurando...' : 'Restaurar'}
                          </button>
                        ) : (
                          <div className="text-sm text-gray-500">ACTIVO</div>
                        )}
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>


        </div>
    </div>
  );
}
