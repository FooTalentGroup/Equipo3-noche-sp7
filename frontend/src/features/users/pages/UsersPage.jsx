import React, { useState } from 'react';
import UsersTable from '../components/UsersTable';
import RegisterUserPopup from '../components/RegisterUserPopup';
import { Plus } from 'lucide-react';

const UsersPage = () => {
  const [users, setUsers] = useState([
    { id: '1', nombre: 'Juan Pérez', email: 'juan.perez@example.com', role: 'ADMINISTRADOR' },
    { id: '2', nombre: 'María López', email: 'maria.lopez@example.com', role: 'ENCARGADO' },
    { id: '3', nombre: 'Carlos García', email: 'carlos.garcia@example.com', role: 'ENCARGADO' },
  ]);
  const [isRegisterOpen, setIsRegisterOpen] = useState(false);
  const [editingUser, setEditingUser] = useState(null);

  const handleEdit = (user) => {
    setEditingUser(user);
    setIsRegisterOpen(true);
  };

  const handleDelete = (id) => {
    setUsers((prev) => prev.filter((u) => u.id !== id));
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
    // payload: { id?, nombre, email, password, role }
    if (payload.id) {
      // update
      setUsers(prev => prev.map(u => u.id === payload.id ? { ...u, nombre: payload.nombre, email: payload.email, role: payload.role } : u));
    } else {
      // create (generate id)
      const newUser = { id: String(Date.now()), nombre: payload.nombre, email: payload.email, role: payload.role };
      setUsers(prev => [newUser, ...prev]);
    }
    closeRegister();
  };

  return (
    <div className="p-6">
      <div className="mb-6">
        <div className="w-[570px] justify-center text-Stockia-Primary-950 text-3xl font-semibold">Registro de usuarios y asignación de roles</div>
        <div className="mt-2 w-[1039px] justify-center text-Stockia-Neutral-500---Hover text-base font-medium">Registra un nuevo usuario y asígnale un rol en el sistema.</div>
      </div>

      <div className="flex items-center gap-6 mb-6">
        <button onClick={openRegister} className="cursor-pointer flex justify-center items-center gap-2 p-[9.5px] bg-stokia-primary-600 text-stokia-neutral-50 rounded-[8px] w-[177.25px]">
            <Plus className="h-4 w-4" />
            Registrar usuario
        </button>
        <button className="cursor-pointer py-2 rounded-[8px] bg-general-secondary text-general-secondary-foreground flex justify-center items-center gap-2 shadow-md bg-[#F5F5F5] w-[199px]">
            Historial de usuarios</button>
      </div>

      <UsersTable users={users} onEdit={handleEdit} onDelete={handleDelete} />

      <RegisterUserPopup open={isRegisterOpen} onClose={closeRegister} onSave={handleSave} initialData={editingUser} />
    </div>
  );
};

export default UsersPage;
