import React from 'react';
import { Outlet } from 'react-router-dom';
import ReportsNav from '../components/ReportsNav';

const ReportsPage = () => {
    return (
        <div className="container mx-auto">
            <ReportsNav />
            <div className="mt-6">
                <Outlet />
            </div>
        </div>
    );
};

export default ReportsPage;