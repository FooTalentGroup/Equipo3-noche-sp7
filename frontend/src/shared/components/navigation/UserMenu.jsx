import { useState } from "react";
import { useNavigate } from "react-router";
import { LogOut, HelpCircle } from "lucide-react"; 
import { useLogout } from "@/features/auth/hooks/useAuth.js";
import { clearAuthData } from "@/features/auth/utils/authStorage.js";
import ButtonExitIcon from "@/assets/button-exit.png"; 


const LogoutModal = ({ isOpen, onClose, onConfirm, isPending }) => {
  if (!isOpen) return null;

  return (
    
    <div 
      className="fixed inset-0 flex justify-center items-center z-50"
      
      style={{ backgroundColor: 'rgba(31, 41, 55, 0.7)' }} 
    > 
      <div className="bg-white p-6 rounded-lg shadow-2xl w-full max-w-md"> 
        
        <div className="flex justify-center items-center mb-6"> 
          <h2 className="text-lg font-bold">¿Estás seguro que quieres cerrar sesión?</h2>
        </div>

        <div className="flex justify-center gap-4 mt-4"> 
          <button
            onClick={onClose}
            className="px-4 py-2 text-gray-700 bg-gray-100 rounded hover:bg-gray-200 transition shadow-sm border border-gray-200"
          >
            Cancelar
          </button>
          <button
            onClick={onConfirm}
            disabled={isPending}
            className={`px-4 py-2 rounded transition font-semibold 
              ${isPending 
                ? "bg-red-300 cursor-not-allowed text-white" 
                : "bg-[#C93939] text-white hover:bg-red-700"} 
              flex items-center gap-2
            `}
          >
          
            {!isPending && (
              <img 
                src={ButtonExitIcon} 
                alt="Icono de Salir" 
                
                className="h-6 w-10" 
              />
            )}
            {isPending ? "Cerrando sesión..." : "Cerrar sesión"}
          </button>
        </div>
      </div>
    </div>
  );
};


const SidebarItem = ({ icon: Icon, label, onClick, className = "", isActive = false }) => {
  const baseClasses = "flex items-center gap-3 w-full p-6 transition text-base";
  
  const activeHoverStyles = "hover:bg-[#F4F5F7] hover:shadow-[0_4px_4px_0_rgba(0,0,0,0.25)]";
  const activeClasses = isActive
    ? "bg-[#F4F5F7] shadow-[0_4px_4px_0_rgba(0,0,0,0.25)]" 
    : activeHoverStyles; 

  return (
    <button onClick={onClick} className={`${baseClasses} ${activeClasses} ${className}`}>
      <Icon className="h-[18px] w-[18px]" />
      <span>{label}</span>
    </button>
  );
};

export const SidebarFooter = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const navigate = useNavigate();

  const logout = useLogout({
    onSuccess: () => {
      clearAuthData();
      navigate("/login", { replace: true });
    },
    onError: (error) => {
      console.error("Error al cerrar sesión:", error.message);
      clearAuthData(); 
      navigate("/login", { replace: true });
    },
  });

  const handleLogout = () => {
    logout.mutate(); 
  };

  const handleOpenModal = () => setIsModalOpen(true);
  const handleCloseModal = () => setIsModalOpen(false);

  const navigateToFAQ = () => {
   
  };

  const logoutColorClasses = "text-[#C93939]"; 

  return (
    <div className="flex flex-col border-t border-[#E4E8E9]">
      
      <SidebarItem 
        icon={HelpCircle} 
        label="Preguntas frecuentes"
        onClick={navigateToFAQ}
        className="text-[#525252]" 
      />
      
      <SidebarItem
        icon={LogOut} 
        label="Cerrar sesión"
        onClick={handleOpenModal}
        className={logoutColorClasses} 
      />

      <LogoutModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onConfirm={handleLogout}
        isPending={logout.isPending}  
      />
    </div>
  );
};
