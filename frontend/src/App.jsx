import { RouterProvider } from "react-router-dom";
import router from "@/infrastructure/router/routes.config.jsx";
import { Toaster } from "@/shared/components/ui/sonner.jsx";
import React from 'react';
import { Outlet } from 'react-router-dom';
import SideMenu from '@/shared/components/navigation/SideMenu.jsx';
import { TopBar } from '@/shared/components/layout/TopBar.jsx';

export default function App() {
  return (
    <div className="app-wrapper mx-auto">
      <div className="flex">
        <SideMenu />
        <div className="flex-1 min-h-screen bg-white">
          <div className="flex justify-start">
            <TopBar />
          </div>
          <main className="p-6">
            <Outlet />
          </main>
        </div>
      </div>
    </div>
  );
}
