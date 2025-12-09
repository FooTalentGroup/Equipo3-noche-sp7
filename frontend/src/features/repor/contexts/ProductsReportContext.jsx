import { createContext, useContext, useState } from "react";

const ProductsReportContext = createContext();

export function ProductsReportProvider({ children }) {
  const [startDate, setStartDate] = useState(new Date());
  const [endDate, setEndDate] = useState(new Date());


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
