import MovementHistoryPage from '../pages/MovementHistoryPage.jsx';

export const inventoryRoutes = [
    { path: 'inventory-movements', element: <MovementHistoryPage /> },
    { path: 'inventory-movements/:productId', element: <MovementHistoryPage /> },
];