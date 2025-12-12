import { createContext, useContext, useState } from "react";

const ProductsReportContext = createContext();

export function ProductsReportProvider({ children }) {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);


  return (
    <ProductsReportContext.Provider
      value={{
        startDate,
        endDate,
        setStartDate,
        setEndDate,
      }}
    >
      {children}
    </ProductsReportContext.Provider>
  );
}

export function useProductsReport() {
  return useContext(ProductsReportContext);
}
