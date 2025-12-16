import React, { useState, useEffect } from 'react';
import UsersTable from '../components/UsersTable';
import RegisterUserPopup from '../components/RegisterUserPopup';
import { Plus, Search, History, ChevronLeft, ChevronRight } from 'lucide-react'; 
import { getUsers, deleteUser, createUser, updateUser } from '../services/usersService';
import { ConfirmDialog } from '@/features/products/components/ConfirmDialog';


const UsersPage = () => {
  const [users, setUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [isRegisterOpen, setIsRegisterOpen] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isConfirmOpen, setIsConfirmOpen] = useState(false);
  const [toDeleteId, setToDeleteId] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  const filteredUsers = users.filter(
    (user) =>
      user.nombre.toLowerCase().includes(searchTerm.toLowerCase()) ||
      user.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      user.role.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleEdit = (user) => {
    setEditingUser({
      id: user.id,
      nombre: user.nombre,
      email: user.email,
      role: user.__raw?.roleCode ?? user.role,
      accountStatus: user.__raw?.accountStatus ?? 'ACTIVE',
    });
    setIsRegisterOpen(true);
  };

  const handleDelete = (id) => {
    setToDeleteId(id);
    setIsConfirmOpen(true);
  };

  const openRegister = () => {
    setEditingUser(null);
    setIsRegisterOpen(true);
  };

  const closeRegister = () => {
    setIsRegisterOpen(false);
    setTimeout(() => setEditingUser(null), 200);
  };

  const handleSave = async (payload) => {
    try {
      if (payload.id) {
        await updateUser(payload.id, {
          name: payload.nombre,
          email: payload.email,
          role: payload.role,
          accountStatus: payload.accountStatus,
          ...(payload.password ? { password: payload.password } : {}),
        });
      } else {
        await createUser({
          name: payload.nombre,
          email: payload.email,
          role: payload.role,
          accountStatus: payload.accountStatus,
          password: payload.password,
        });
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
      setUsers((prev) => prev.filter((u) => u.id !== toDeleteId));
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

      const filtered = content.filter(
        (u) => !u.deleted && (!u.accountStatus || u.accountStatus === 'ACTIVE')
      );

      const mapped = filtered.map((u) => ({
        id: u.id ?? u._id ?? u.userId,
        nombre: u.name ?? u.nombre ?? '',
        email: u.email ?? '',
        role: roleMap[u.role] ?? (u.role ?? 'ENCARGADO'),
        __raw: {
          roleCode: u.role,
          accountStatus: u.accountStatus,
        },
      }));

      setUsers(mapped);
      setTotalPages(res.totalPages ?? 1);
      setCurrentPage(res.pageNumber ?? page);
    } catch (err) {
      console.error('Error fetching users', err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="p-6">
      <div className="mb-6">
        <div className="w-[570px] text-Stockia-Primary-950 text-3xl font-semibold">
          Registro de usuarios y asignación de roles
        </div>
        <div className="mt-2 w-[1039px] text-Stockia-Neutral-500---Hover text-base font-medium">
          Registra un nuevo usuario y asígnale un rol en el sistema.
        </div>
      </div>

      <div className="flex items-center justify-between mb-6">
        <div className="relative w-120">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 h-4 w-4" />
          <input
            type="text"
            placeholder="Buscar usuarios por nombre correo o rol"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-stokia-primary-500 focus:border-transparent"
          />
        </div>

        <div className="flex items-center gap-6">
          <button className="cursor-pointer flex items-center justify-center gap-2 py-2 bg-[#F5F5F5] rounded-[8px] shadow-md w-[199px]">
            
            <History className="h-4 w-4" /> 
            Historial de usuarios
          </button>

          <button
            onClick={openRegister}
            className="cursor-pointer flex items-center justify-center gap-2 p-[9.5px] bg-stokia-primary-600 text-stokia-neutral-50 rounded-[8px] w-[177.25px]"
          >
            <Plus className="h-4 w-4" />
            Registrar usuario
          </button>
        </div>
      </div>

      <UsersTable
        users={filteredUsers}
        onEdit={handleEdit}
        onDelete={handleDelete}
        isLoading={isLoading}
      />

      <div className="mt-6 flex items-center justify-center gap-1">
        <button
          disabled={currentPage === 0}
          onClick={() => setCurrentPage(Math.max(0, currentPage - 1))}
          className={`px-4 py-2 mx-1 text-sm font-medium border rounded transition-colors ${
            currentPage === 0
              ? 'bg-gray-100 text-gray-400 border-gray-200 cursor-not-allowed'
              : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-50'
          }`}
        >
          <div className="flex items-center gap-2">
            <ChevronLeft className="h-4 w-4" />
            Anterior
          </div>
        </button>

        {Array.from({ length: Math.max(totalPages, 1) }, (_, index) => (
          <button
            key={index}
            onClick={() => setCurrentPage(index)}
            disabled={index >= totalPages}
            className={`px-3 py-2 mx-1 text-sm font-medium border rounded transition-colors ${
              index === currentPage
                ? 'bg-stokia-primary-600 text-stokia-neutral-50 border-stokia-primary-600'
                : index >= totalPages
                ? 'bg-gray-100 text-gray-400 border-gray-200 cursor-not-allowed'
                : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-50'
            }`}
          >
            {index + 1}
          </button>
        ))}

        <button
          disabled={currentPage >= Math.max(totalPages - 1, 0)}
          onClick={() => setCurrentPage(Math.min(totalPages - 1, currentPage + 1))}
          className={`px-4 py-2 mx-1 text-sm font-medium border rounded transition-colors ${
            currentPage >= Math.max(totalPages - 1, 0)
              ? 'bg-gray-100 text-gray-400 border-gray-200 cursor-not-allowed'
              : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-50'
          }`}
        >
          <div className="flex items-center gap-2">
            Siguiente
            <ChevronRight className="h-4 w-4" />
          </div>
        </button>
      </div>
      
     
      <RegisterUserPopup
        isOpen={isRegisterOpen}
        onClose={closeRegister}
        onSave={handleSave}
        initialData={editingUser}
        roleMap={roleMap}
      />

      <ConfirmDialog
        isOpen={isConfirmOpen}
        onClose={() => setIsConfirmOpen(false)}
        onConfirm={handleConfirmDelete}
        title="Confirmar Eliminación"
        description="¿Estás seguro de que deseas eliminar este usuario? Esta acción no se puede deshacer."
      />
    </div>
  );
};

export default UsersPage;
