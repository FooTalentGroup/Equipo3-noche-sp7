import { NavLink } from 'react-router';
import { Package, ShoppingBag, UsersRound, ChartLine, House, UserRoundIcon } from 'lucide-react';
import { SidebarFooter } from './UserMenu';
import stokialogo from "@/assets/stockia.svg";


const navLinks = [
    {
        to: '/',
        icon: House,
        label: 'Inicio'
    },
    {
        to: '/sales',
        icon: ShoppingBag,
        label: 'Ventas'
    },
    {
        to: '/products',
        icon: Package,
        label: 'Productos'
    },
    {
        to: '/reports',
        icon: ChartLine,
        label: 'Reportes'
    },
    {
        to: '/customers',
        icon: UsersRound,
        label: 'Clientes'
    },
    {
        to: '/users',
        icon: UserRoundIcon,
        label: 'Gestión de Usuarios'
    }


];

const NavLinkItem = ({ to, icon: Icon, arrowIcon: Arrow, label }) => (
    <div>
        <NavLink
            to={to}
            className={({ isActive, isPending }) =>
                `flex items-center justify-between p-6 text-base transition-all w-full border-b-2 border-stokia-neutral-100 ${isPending
                    ? 'opacity-50'
                    : isActive
                        ? 'bg-stokia-primary-600 text-stokia-neutral-50'
                        : 'text-stokia-neutral-600 hover:bg-stokia-neutral-100'
                }`
            }
        >
            <div className="flex items-center gap-4 h-4">
                <Icon className='h-[18px] w-[18px]' />
                <span>{label}</span>
            </div>
        </NavLink>
    </div>
);

const SideMenu = () => {
    return (
        <aside className='hidden md:flex md:flex-col border-r bg-sidebar min-w-[16rem] justify-between h-full'>
            <div className="flex-1 overflow-y-auto">
                <div className="w-63 h-20 p-6 flex justify-center">
                    <img src={stokialogo} alt="Stokia Logo" className="h-[2.58rem]" />
                </div>

                <nav className='flex flex-col'>
                    {navLinks.map(link => (
                        <NavLinkItem key={link.to} {...link} />
                    ))}
                </nav>
            </div>

            <SidebarFooter />
        </aside>
    );
};

export default SideMenu;
