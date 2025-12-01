import React from "react";
import {
  Search,
  UserPlus,
  User,
  ChevronLeft,
  ChevronRight,
} from "lucide-react";

import { Button } from "@/shared/components/ui/button";
import { Input } from "@/shared/components/ui/input";
import { ProductCard } from "../components/CardResult";
import { SalesSummary } from "../components/SalesSummary";



const SalesPage = () => {
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
    {
      name: "Otro producto 1",
      stock: 50,
      price: "10.00",
      imageUrl: "https://bing.com/th?id=OSK.180ec435a730a8119c04240ef2264415",
    },
    {
      name: "Otro producto 2",
      stock: 50,
      price: "10.00",
      imageUrl: "https://bing.com/th?id=OSK.175d97d4382be4a9b5838df803d3cfff",
    },
  ];

  return (
    <div className="min-h-screen bg-white p-8 w-full flex flex-col">
   
          <section className="mb-6">
            <h2 className="text-base font-medium text-gray-700 mb-3">
              Cliente
            </h2>

            <div className="flex flex-wrap flex-col gap-4">
              <div className="relative max-w-sm w-full sm:w-auto">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                <Input
                  placeholder="Buscar clientes"
                  className="pl-10 pr-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm w-[432px] h-[36px]"
                />
              </div>

              <div className="flex space-x-6">
                <Button
                  onClick={() => console.log("Consumidor Final")}
                  className="bg-[#436086] hover:bg-[#384d6b] text-white py-2 px-4 rounded-md flex items-center space-x-2 shadow-sm text-sm w-[204px] h-[40px]"
                >
                  <User className="h-4 w-4" />
                  <span>Consumidor final</span>
                </Button>

                <Button
                  onClick={() => console.log("Agregar Nuevo Cliente")}
                  className="bg-[#436086] hover:bg-[#384d6b] text-white py-2 px-4 rounded-md flex items-center space-x-2 shadow-sm text-sm w-[204px] h-[40px]"
                >
                  <UserPlus className="h-4 w-4" />
                  <span>Nuevo cliente</span>
                </Button>
              </div>
            </div>
          </section>

          <section className="mb-6">
            <h2 className="text-base font-medium text-gray-700 mb-3">
              Buscar producto
            </h2>

            <div className="relative max-w-sm w-full">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
              <Input
                placeholder="Buscar producto"
                className="pl-10 border border-gray-300 rounded-md shadow-sm text-sm w-[432px] h-[36px] "
              />
            </div>
          </section>
          <div className="flex-grow grid grid-cols-1 lg:grid-cols-[2fr_1fr] gap-8 items-start">

          <section className="flex-grow flex flex-col">
            <h2 className="text-base font-medium text-gray-700 mb-4">
              Resultados
            </h2>
            

            <div className="overflow-y-auto max-h-[450px] bg-gray-50 border border-gray-200 rounded-lg p-4 custom-scroll">
              <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-6">
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
            <h2 className="text-base font-medium text-gray-700 mb-4">
                Resumen de compra 
            </h2>
              <SalesSummary />
            <div className="flex space-x-4">
              <Button
                  onClick={() => console.log("Agregar Nuevo Cliente")}
                  className="bg-[#FAFAFA]  text-[#171717] py-2 px-4 rounded-[8px] flex items-center space-x-2 shadow-sm text-sm w-[131px] h-[40px]"
                >
                  <span>Agregar nota</span>
              </Button>
              <Button
                  onClick={() => console.log("Agregar Nuevo Cliente")}
                  className="bg-[#FAFAFA]  text-[#171717] py-2 px-4 rounded-[8px] flex items-center space-x-2 shadow-sm text-sm w-[149px] h-[40px]"
                >
                  <span>Finalizar pedido</span>
              </Button>

            </div>
               
          </div>
         
        </div>    
        
      </div>
  );
};

export default SalesPage;
