import React, { useState, useEffect, useRef } from 'react';
import { NavLink, useLocation } from 'react-router-dom';

const tabs = [
  { name: 'Ventas', path: 'sales' },
  { name: 'Productos', path: 'products' },
  { name: 'Usuarios', path: 'users' },
];

const ReportsNav = () => {
  const [indicatorStyle, setIndicatorStyle] = useState({ left: 0, width: 0 });
  const tabsRef = useRef([]);
  const location = useLocation();

  useEffect(() => {
    const pathSegments = location.pathname.split('/');
    const activeIndex = tabs.findIndex(tab => pathSegments.includes(tab.path));
    const indexToUse = activeIndex !== -1 ? activeIndex : 0;

    const activeTab = tabsRef.current[indexToUse];
    if (activeTab) {
      const barWidth = activeTab.offsetWidth * 0.7;
      const centeredLeft = activeTab.offsetLeft + (activeTab.offsetWidth - barWidth) / 2;

      setIndicatorStyle({
        left: centeredLeft,
        width: barWidth,
      });
    }
  }, [location.pathname]);

  return (
    <div className="flex justify-center w-full mb-8">
      <div className="relative flex bg-stokia-primary-50 rounded-lg px-16">
        <div
          className="absolute bottom-0 h-1 bg-stokia-primary-600 rounded-t-full transition-all duration-300 ease-in-out z-10"
          style={{
            left: indicatorStyle.left,
            width: indicatorStyle.width,
          }}
        />

        {tabs.map((tab, index) => (
          <NavLink
            key={tab.path}
            to={tab.path}
            ref={el => (tabsRef.current[index] = el)}
            className={({ isActive }) =>
              `relative z-0 px-8 py-4 text-xl font-medium transition-colors duration-200 outline-none focus-visible:ring-2 focus-visible:ring-blue-500 ${isActive ? 'text-stokia-neutral-950' : 'text-stokia-neutral-400 hover:text-slate-700'
              }`
            }
          >
            {tab.name}
          </NavLink>
        ))}
      </div>
    </div>
  );
};

export default ReportsNav;
