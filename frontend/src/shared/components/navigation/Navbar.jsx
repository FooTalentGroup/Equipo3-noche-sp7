import { useState } from 'react';
import { NavLink } from 'react-router';
import {LayoutDashboard, Package, UtensilsCrossed, Menu, Plus, FileUp,Printer,Bell} from 'lucide-react';
import { UserMenu } from './UserMenu.jsx';
import {
  Sheet,
  SheetContent,
  SheetTrigger,
  SheetClose
} from '@/shared/components/ui/sheet.jsx';
import { Button } from '@/shared/components/ui/button.jsx';

export const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false);

  const navLinks = [
    {
      to: '/dashboard',
      icon: LayoutDashboard,
      label: 'Dashboard'
    },
    {
      to: '/products',
      icon: Package,
      label: 'Productos'
    }
  ];

  const NavLinkItem = ({ to, icon: Icon, label, onClick }) => (
    <NavLink
      to={to}
      onClick={onClick}
      className={({ isActive, isPending }) =>
        `flex items-center gap-3 rounded-md transition-all px-3 py-2 ${
          isPending
            ? 'opacity-50'
            : isActive
            ? 'bg-red-100 text-red-800'
            : 'hover:bg-gray-100'
        }`
      }
    >
      <Icon className='h-5 w-5' />
      <span>{label}</span>
    </NavLink>
  );

  return (
    <nav className='py-3 px-3 border top-0 bg-slate-50 z-40 h-21'>
      <div className='flex justify-between items-center px-3'>
        <div className='flex items-center gap-6'>
          <NavLink
            to='/dashboard'
            className='flex items-center mb-4 gap-2 text-black'
          >
            <UtensilsCrossed className='h-6 w-6' />
            <span className='text-xl font-bold'>ProEat</span>
          </NavLink>
        </div>
        <div className='flex items-center gap-2'>
          <UserMenu />
          <Sheet open={isOpen} onOpenChange={setIsOpen}>
            <SheetTrigger asChild>
              <Button
                variant='ghost'
                size='icon'
                className='md:hidden'
                aria-label='Abrir menú de navegación'
              >
                <Menu className='h-6 w-6 text-orange-600' />
              </Button>
            </SheetTrigger>
            <SheetContent side='left' className='w-64'>
              <div className='flex flex-col gap-4 mt-8'>
                <div className='flex items-center gap-2 text-orange-600 mb-4'>
                  <UtensilsCrossed className='h-6 w-6' />
                  <span className='text-xl font-bold'>ProEat</span>
                </div>
                <nav className='flex flex-col gap-2'>
                  {navLinks.map(link => (
                    <SheetClose key={link.to} asChild>
                      <NavLinkItem {...link} onClick={() => setIsOpen(false)} />
                    </SheetClose>
                  ))}
                </nav>
              </div>
            </SheetContent>
          </Sheet>
            <div className='flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-6'>
                <Button
                    className='bg-white text-neutral-950 hover:bg-gray-400 cursor-pointer shadow-sm mt-3'
                >
                    <Bell className='h-4 w-4 mr-1' />
                    Notificaciones
                </Button>
            </div>
        </div>
      </div>
    </nav>
  );
};
