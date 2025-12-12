import { ProtectedRoute } from "@/infrastructure/router/ProtectedRoute.jsx";
import { ProtectedLayout } from "@/shared/components/layout/ProtectedLayout.jsx";
import ReportsPage from "../pages/ReportsPage";
import { elements } from "chart.js";
import { Navigate } from "react-router";
import ReportsSales from "../pages/ReportsSales";
import ProductsReport from "../pages/ProductsReportPage";
import { ProductsReportProvider } from "../contexts/ProductsReportContext";
import BestSellersReport from "../components/BestSellersReport";
import PricingReport from "../components/PricingReport";
import StockReport from "../components/StockReport";
import ReportsUsers from "../pages/ReportsUsers";


export const reportsRoutes = [
  {
    path: "/reports",
    element: <ReportsPage />,
    children: [
      { index: true, element: <Navigate to="sales" replace /> },
      { path: 'sales', element: <ReportsSales /> },
      {
        path: 'products',
        element: (
          <ProductsReportProvider>
            <ProductsReport />
          </ProductsReportProvider>
        ),
        children: [
          { index: true, element: <Navigate to="best_sellers" replace /> },
          { path: 'best_sellers', element: <BestSellersReport /> },
          { path: 'pricing', element: <PricingReport /> },
          { path: 'stock', element: <StockReport /> },
        ],
      },
      { path: 'users', element: <ReportsUsers /> },
    ],

  },
];