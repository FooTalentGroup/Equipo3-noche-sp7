import { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router";
import { LogOut } from "lucide-react";
import { useLogout } from "@/features/auth/hooks/useAuth.js";
import { clearAuthData, getUsername } from "@/features/auth/utils/authStorage.js";

const getInitials = (username) => {
  if (!username) return "?";
  const parts = username.trim().split(" ");
  if (parts.length >= 2) {
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
  }
  return username.substring(0, 2).toUpperCase();
};

export const UserMenu = () => {
  const [isOpen, setIsOpen] = useState(false);
  const menuRef = useRef(null);
  const navigate = useNavigate();
  const username = getUsername();

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
    setIsOpen(false);
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    if (isOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isOpen]);

  if (!username) return null;

  const initials = getInitials(username);

  return (
    <div className="relative" ref={menuRef}>
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="flex items-center gap-2 px-3 py-2 rounded-md hover:bg-gray-100 transition-colors focus:outline-none focus:ring-2 focus:ring-orange-500"
        aria-label="Menú de usuario"
        aria-expanded={isOpen}
      >
        <div className="w-8 h-8 rounded-full bg-orange-600 text-white flex items-center justify-center text-sm font-semibold">
          {initials}
        </div>
        <span className="text-gray-700 text-sm font-medium hidden sm:block">
          {username}
        </span>
      </button>

      {isOpen && (
        <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg border border-gray-200 py-1 z-50">
          <button
            onClick={handleLogout}
            disabled={logout.isPending}
            className="w-full flex items-center gap-2 px-4 py-2 text-sm text-red-600 hover:bg-red-50 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <LogOut className="h-4 w-4" />
            {logout.isPending ? "Cerrando sesión..." : "Cerrar Sesión"}
          </button>
        </div>
      )}
    </div>
  );
};

