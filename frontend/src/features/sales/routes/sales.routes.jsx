import SalesPage from "../pages/SalesPage.jsx";
import NewSalePage from "../pages/NewSalePage.jsx";
import SalePayment from "../components/SalePayment.jsx";
import ConfirmedSalesPage from "../pages/ConfirmedSalesPage.jsx";
import { Navigate } from "react-router";
import { PendingSalesPage } from "../pages/PendingSalesPage.jsx";
import { CancelledSalesPage } from "../pages/CancelledSalesPage.jsx";

export const salesRoutes = [
    {
        path: "/sales",
        element: <SalesPage />,
        children: [
            {
                index: true,
                element: <Navigate to="new" replace />
            },
            { path: 'new', element: <NewSalePage /> },
            { path: 'edit/:orderId', element: <NewSalePage /> },
            {
                path: 'pending',
                element: <PendingSalesPage />,
                children: [
                    { path: 'payment/:id', element: <SalePayment /> },
                ]
            },

            { path: 'confirmed', element: <ConfirmedSalesPage /> },
            { path: 'cancelled', element: <CancelledSalesPage /> },
        ],
    },
];

