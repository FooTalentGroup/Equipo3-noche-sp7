import { Outlet } from 'react-router';
import { Navbar } from '@/shared/components/navigation/Navbar.jsx';
import SideMenu from "@/shared/components/navigation/SideMenu.jsx";

export const ProtectedLayout = () => {
  return (
    <div>
      
        <div className="flex">
        <SideMenu/>
      <Outlet />
        </div>
    </div>
  );
};
