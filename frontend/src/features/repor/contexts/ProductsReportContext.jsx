import { createContext, useContext, useState } from "react";

const ProductsReportContext = createContext();

export function ProductsReportProvider({ children }) {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [year, setYear] = useState(new Date().getFullYear());
  const [productName, setProductName] = useState('');
  const [isExportModalOpen, setIsExportModalOpen] = useState(false);
  const [reportData, setReportData] = useState(null);


  return (
    <ProductsReportContext.Provider
      value={{
        startDate,
        endDate,
        setStartDate,
        setEndDate,
        year,
        setYear,
        productName,
        setProductName,
        isExportModalOpen,
        setIsExportModalOpen,
        reportData,
        setReportData,
      }}
    >
      {children}
    </ProductsReportContext.Provider>
  );
}

export function useProductsReport() {
  return useContext(ProductsReportContext);
}
