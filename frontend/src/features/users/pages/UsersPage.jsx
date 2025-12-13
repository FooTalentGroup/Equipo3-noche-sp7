import React, { useState, useEffect } from 'react';
import UsersTable from '../components/UsersTable';
import UsersHistoryModal from '../components/UsersHistoryModal';
import RegisterUserPopup from '../components/RegisterUserPopup';
import { Plus } from 'lucide-react';
import { getUsers, deleteUser, createUser, updateUser } from '../services/usersService';
import { ConfirmDialog } from '@/features/products/components/ConfirmDialog';

const UsersPage = () => {
  const [users, setUsers] = useState([]);
  const [isRegisterOpen, setIsRegisterOpen] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isConfirmOpen, setIsConfirmOpen] = useState(false);
  const [toDeleteId, setToDeleteId] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [isHistoryOpen, setIsHistoryOpen] = useState(false);

  const handleEdit = (user) => {
    // pass raw fields into editingUser to prefill the form with role code and accountStatus
    setEditingUser({ id: user.id, nombre: user.nombre, email: user.email, role: user.__raw?.roleCode ?? user.role, accountStatus: user.__raw?.accountStatus ?? 'ACTIVE' });
    setIsRegisterOpen(true);
  };

  const handleDelete = (id) => {
    // open confirm dialog
    setToDeleteId(id);
    setIsConfirmOpen(true);
  };

  const openRegister = () => {
    setEditingUser(null);
    setIsRegisterOpen(true);
  };

  const openHistory = () => {
    setIsHistoryOpen(true);
  };

  const closeRegister = () => {
    setIsRegisterOpen(false);
    setTimeout(() => setEditingUser(null), 200);
  };

  const handleSave = async (payload) => {
    try {
      if (payload.id) {
        await updateUser(payload.id, { name: payload.nombre, email: payload.email, role: payload.role, accountStatus: payload.accountStatus, ...(payload.password ? { password: payload.password } : {}) });
      } else {
        await createUser({ name: payload.nombre, email: payload.email, role: payload.role, accountStatus: payload.accountStatus, password: payload.password });
      }
      await fetchUsers();
      closeRegister();
    } catch (err) {
      console.error('Error saving user', err);
      throw err;
    }
  };

  const handleConfirmDelete = async () => {
    if (!toDeleteId) return;
    try {
      await deleteUser(toDeleteId);
      setUsers(prev => prev.filter(u => u.id !== toDeleteId));
    } catch (err) {
      console.error('Error deleting user', err);
    } finally {
      setIsConfirmOpen(false);
      setToDeleteId(null);
    }
  };

  useEffect(() => {
    fetchUsers(currentPage);
  }, [currentPage]);

  const roleMap = {
    ADMIN: 'ADMINISTRADOR',
    MANAGER: 'ENCARGADO',
    USER: 'USUARIO',
  };

  const fetchUsers = async (page = 0) => {
    setIsLoading(true);
    try {
      const res = await getUsers({ page, size: 20 });
      const content = res.users || [];

      // Map users and normalize accountStatus for consistent filtering
      const mappedAll = content.map(u => ({
        id: u.id ?? u._id ?? u.userId,
        nombre: u.name ?? u.nombre ?? '',
        email: u.email ?? '',
        // normalize accountStatus to uppercase and trim for consistent logic
        accountStatus: String(u.accountStatus ?? 'ACTIVE').trim().toUpperCase(),
        deleted: !!u.deleted,
        // UI label
        role: roleMap[u.role] ?? (u.role ?? 'ENCARGADO'),
        // keep raw fields for edit payload
        __raw: {
          roleCode: u.role,
          accountStatus: u.accountStatus,
        }
      }));

      // Filter only by normalized accountStatus === 'ACTIVE' so all active users are shown
      const filtered = mappedAll.filter(u => u.accountStatus === 'ACTIVE');

      const mapped = filtered.map(u => ({
        id: u.id,
        nombre: u.nombre,
        email: u.email,
        role: u.role,
        __raw: u.__raw
      }));

      setUsers(mapped);
      setTotalPages(res.totalPages ?? 1);
      setCurrentPage(res.pageNumber ?? res.pageNumber ?? page);
    } catch (err) {
      console.error('Error fetching users', err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="p-6">
      <div className="mb-6">
        <div className="w-[600px] justify-center text-Stockia-Primary-950 text-3xl font-semibold">Registro de usuarios y asignación de roles</div>
        <div className="mt-2 w-[1039px] justify-center text-Stockia-Neutral-500---Hover text-base font-medium">Registra un nuevo usuario y asígnale un rol en el sistema.</div>
      </div>

      <div className="flex items-center gap-6 mb-6">
        <button onClick={openRegister} className="cursor-pointer flex justify-center items-center gap-2 p-[9.5px] bg-stokia-primary-600 text-stokia-neutral-50 rounded-[8px] w-[177.25px]">
            <Plus className="h-4 w-4" />
            Registrar usuario
        </button>
        <button onClick={openHistory} className="cursor-pointer py-2 rounded-[8px] bg-general-secondary text-general-secondary-foreground flex justify-center items-center gap-2 shadow-md bg-[#F5F5F5] w-[199px]">
            Historial de usuarios</button>
      </div>

      <UsersTable users={users} onEdit={handleEdit} onDelete={handleDelete} isLoading={isLoading} />

      {totalPages > 1 && (
        <div className="mt-4 flex items-center gap-2">
          <button disabled={currentPage === 0} onClick={() => setCurrentPage(p => Math.max(0, p - 1))} className="px-3 py-1 bg-gray-100 rounded">Anterior</button>
          <div className="text-sm">Página {currentPage + 1} / {totalPages}</div>
          <button disabled={currentPage >= totalPages - 1} onClick={() => setCurrentPage(p => Math.min(totalPages - 1, p + 1))} className="px-3 py-1 bg-gray-100 rounded">Siguiente</button>
        </div>
      )}

      <RegisterUserPopup open={isRegisterOpen} onClose={closeRegister} onSave={handleSave} initialData={editingUser} />
      <UsersHistoryModal open={isHistoryOpen} onClose={() => setIsHistoryOpen(false)} onRestore={() => fetchUsers(currentPage)} />

      <ConfirmDialog
        isOpen={isConfirmOpen}
        handleOpenChange={setIsConfirmOpen}
        dialogTitle="¿Desea eliminar el usuario?"
        dialogDescription="Esta acción eliminará al usuario de forma permanente."
        cancelTitle="Cancelar"
        acceptTitle="Sí, eliminar"
        onAccept={handleConfirmDelete}
      />
    </div>
  );
};

export default UsersPage;
