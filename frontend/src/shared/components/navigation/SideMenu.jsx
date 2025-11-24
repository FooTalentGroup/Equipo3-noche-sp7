import { NavLink } from 'react-router';
import { Package, ShoppingBag, Percent, UsersRound, Truck, ChartLine, Sparkles, House } from 'lucide-react';
import { UserMenu } from './UserMenu';
import stokialogo from "@/assets/stockia.svg";



const navLinks = [
    {
        to: '/home',
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
    }
    ,
    {
        to: '/discounts',
        icon: Percent,
        label: 'Descuentos'
    },
    {
        to: '/predictions-IA',
        icon: Sparkles,
        label: 'Predicciones de IA'
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
        to: '/suppliers',
        icon: Truck,
        label: 'Proveedores'
    }


];

const NavLinkItem = ({ to, icon: Icon, arrowIcon: Arrow, label }) => (
    <div>
        <NavLink
            to={to}
            className={({ isActive, isPending }) =>
                `flex items-center justify-between h-16 px-6 py-2 transition-all border-b-2 w-full ${isPending
                    ? 'opacity-50'
                    : isActive
                        ? 'bg-slate-300 text-neutral-700'
                        : 'hover:bg-gray-100'
                }`
            }
        >
            <div className="flex items-center gap-4">
                <Icon className='h-5 w-5' />
                <span>{label}</span>
            </div>
        </NavLink>
    </div>
);

const SideMenu = () => {
    return (
        <aside className='hidden md:flex md:flex-col border-r bg-gray-100 min-w-[16rem] justify-between max-h-256'>
            <div className="flex-1 overflow-y-auto">
                <div className="w-63 h-20 p-6 flex justify-center">
                    <img src={stokialogo} alt="Stokia Logo" className="w-51 h-[2.58rem]" />
                </div>

                <nav className='flex flex-col gap-1'>
                    {navLinks.map(link => (
                        <NavLinkItem key={link.to} {...link} />
                    ))}
                </nav>
            </div>

            <div className="">
                <UserMenu />
            </div>
        </aside>
    );
};

export default SideMenu;