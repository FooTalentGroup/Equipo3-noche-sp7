import { NavLink } from 'react-router';
import { Package,ShoppingBag, Percent,  UsersRound, Truck, ChartLine, Sparkles, House } from 'lucide-react';
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

const NavLinkItem = ({ to, icon: Icon,arrowIcon:Arrow, label }) => (
    <div>
    <NavLink
        to={to}
        className={({ isActive, isPending }) =>
            `flex items-center justify-between transition-all px-3 py-2 w-55 ${
                isPending
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
        <aside className='hidden md:flex md:flex-col w-58 min-h-screen border-r bg-gray-100 py-4 px-3'>
             <div className="w-[252px] h-[80px]  py-4">
                <img src={stokialogo} alt="Stokia Logo" className="w-[204px]" />
             </div>
            
            <nav className='flex flex-col gap-1'>
                {navLinks.map(link => (
                    <NavLinkItem key={link.to} {...link} />
                ))}
            </nav>
             <div className="mt-auto pt-4 ">
                <UserMenu />
            </div>
        </aside>
    );
};

export default SideMenu;