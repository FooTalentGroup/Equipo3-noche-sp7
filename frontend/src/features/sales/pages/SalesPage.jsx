import React, { useState } from "react";
import { OrderTabs } from "../components/OrderTabs";
import { Button } from "@/shared/components/ui/button";
import { CustomerSection } from "../components/CustomerSection";
import { ProductCard } from "../components/CardResult";
import { SalesSummary } from "../components/SalesSummary";
import { Input } from "@/shared/components/ui/input";
import {  Search } from "lucide-react";


const SalesPage = () => {

  const [activeTab, setActiveTab] = useState("register");

  const products = [
    {
      name: "Bowl de quinoa, pollo y vegetales",
      stock: 45,
      price: "12.50",
      imageUrl: "https://bing.com/th?id=OSK.180ec435a730a8119c04240ef2264415",
    },
    {
      name: "Ensalada César con pollo",
      stock: 120,
      price: "11.80",
      imageUrl: "https://bing.com/th?id=OSK.175d97d4382be4a9b5838df803d3cfff",
    },
    {
      name: "Salmón grillado con vegetales",
      stock: 3,
      price: "7.800",
      imageUrl: "https://bing.com/th?id=OSK.2b915c82d48447ccabe05de758f41869",
    },

  ];

  return (
    <div className="min-h-screen w-full flex flex-col">
      <OrderTabs activeTab={activeTab} onTabChange={setActiveTab} />
      
      <div className="bg-background rounded-2xl shadow-sm p-8  border border-border max-w-[1200px] mx-auto w-full">
        <CustomerSection/>

        <section className="mb-6">
          <h2 className="text-base font-medium text-foreground mb-3">
            Buscar producto
          </h2>

          <div className="relative w-full max-w-[432px]">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
            <Input
              placeholder="Buscar producto"
              className="pl-10 h-[36px]"
            />
          </div>
        </section>

        <div className="flex-grow grid grid-cols-1 lg:grid-cols-[2fr_1fr] gap-8 items-start">
          <section className="flex-grow flex flex-col">
            <h2 className="text-base font-medium text-foreground  mb-4">
              Resultados
            </h2>

            <div className="overflow-y-auto max-h-[450px] bg-secondary border border-border rounded-lg p-4 custom-scroll">
              <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-6 min-w-[773px]">
                {products.map((product, index) => (
                  <ProductCard
                    key={index}
                    name={product.name}
                    stock={product.stock}
                    price={product.price}
                    imageUrl={product.imageUrl}
                  />
                ))}
              </div>
            </div>
          </section>

   
          <div className="sticky top-8 flex flex-col space-y-4">
            <h2 className="text-base font-medium text-foreground mb-4">
              Resumen de compra 
            </h2>
            <SalesSummary />
            <div className="flex space-x-4">
              <Button
                onClick={() => console.log("Agregar nota")}
                className="bg-stokia-neutral-50 text-foreground py-2 px-4 rounded-lg  flex items-center space-x-2 shadow-sm text-sm min-w-[131px] h-[40px]"
              >
                <span>Agregar nota</span>
              </Button>
              <Button
                onClick={() => console.log("Finalizar pedido")}
                className="bg-stokia-neutral-50 text-foreground py-2 px-4 rounded-lg flex items-center space-x-2 shadow-sm text-sm min-w-[149px] h-[40px]"
              >
                <span>Finalizar pedido</span>
              </Button>
            </div>
          </div>
        </div>    
      </div>
    </div>
  );
};

export default SalesPage;