import { NavLink } from 'react-router';
import { LayoutDashboard, Package,ShoppingBag, Percent, UtensilsCrossed } from 'lucide-react';

const navLinks = [
    {
        to: '/dashboard',
        icon: LayoutDashboard,
        label: 'Dashboard'
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
    }
];

const NavLinkItem = ({ to, icon: Icon, label }) => (
    <div>
    <NavLink
        to={to}
        className={({ isActive, isPending }) =>
            `flex items-center gap-3 transition-all px-3 py-2 w-66 ${
                isPending
                    ? 'opacity-50'
                    : isActive
                        ? 'bg-slate-300 text-neutral-700'
                        : 'hover:bg-gray-100'
            }`
        }
    >
        <Icon className='h-5 w-5' />
        <span>{label}</span>
    </NavLink>
    </div>
);

const SideMenu = () => {
    return (
        <aside className='hidden md:flex md:flex-col w-70 min-h-screen border-r bg-gray-100 py-4 px-3'>
            <nav className='flex flex-col gap-1'>
                {navLinks.map(link => (
                    <NavLinkItem key={link.to} {...link} />
                ))}
            </nav>
        </aside>
    );
};

export default SideMenu;