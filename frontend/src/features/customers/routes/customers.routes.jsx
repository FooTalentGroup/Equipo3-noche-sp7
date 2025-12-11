
import CustomersPage from "../pages/CustomersPage";
import PurchaseHistoryPage from "../pages/PurchaseHistoryPage";


export const customersRoutes = [
  {
    path: "/customers",
    children: [
      {
        index: true,
        element: <CustomersPage />,
      },
      {
        path: "purchase-history/:id",
        element: <PurchaseHistoryPage />,
      },
    ],
  },
];
