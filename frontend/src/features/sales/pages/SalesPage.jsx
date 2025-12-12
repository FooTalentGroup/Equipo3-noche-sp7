import React from "react";
import { OrderTabs } from "../components/OrderTabs";
import { Outlet } from "react-router";

const SalesPage = () => {
  return (
    <div className="h-full w-full flex flex-col">
      <OrderTabs />
      <Outlet />
    </div>
  );
};

export default SalesPage;
