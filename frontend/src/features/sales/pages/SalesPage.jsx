import React from "react";
import {
  Bell,
  Search,
  UserPlus,
  User,
  Calendar,
  ChevronLeft,
  ChevronRight,
} from "lucide-react";

import { Button } from "@/shared/components/ui/button";
import { Input } from "@/shared/components/ui/input";
import { ProductCard } from "../components/CardResult";

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
      stock: 30,
      price: "7.800",
      imageUrl: "https://bing.com/th?id=OSK.2b915c82d48447ccabe05de758f41869",
    },
  ];

  const today = new Date().toLocaleDateString("es-ES", {
    day: "numeric",
    month: "long",
    year: "numeric",
  });

  return (
    <div className="min-h-screen bg-stockia-fondo p-8 w-full">
      <div className="w-full h-full">
        <header className="flex justify-between items-center mb-8">
          <h1 className="text-2xl font-semibold text-gray-800">Nueva venta</h1>

          <Button
            variant="secondary"
            onClick={() => console.log("Ver Notificaciones")}
            className="flex items-center space-x-2 px-4 py-2 text-sm text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 shadow-sm"
          >
            <Bell className="h-4 w-4 text-gray-500" />
            <span>Notificaciones</span>
          </Button>
        </header>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-700 mb-3">Fecha</h2>

          <div className="relative inline-block">
            <Calendar className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
            <Input
              value={today}
              readOnly
              className="pl-10 w-56 border border-gray-300 bg-white rounded-md shadow-sm text-sm font-medium"
            />
          </div>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-700 mb-3">Cliente</h2>

          <div className="flex flex-wrap items-center gap-4 lg:gap-14">
            <div className="relative max-w-sm w-full sm:w-auto">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
              <Input
                placeholder="Buscar clientes"
                className="pl-10 pr-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm w-full sm:w-auto"
              />
            </div>

            <Button
              onClick={() => console.log("Agregar Nuevo Cliente")}
              className="bg-[#436086] hover:bg-[#384d6b] text-white py-2 px-4 rounded-md flex items-center space-x-2 shadow-sm text-sm"
            >
              <UserPlus className="h-4 w-4" />
              <span>Nuevo cliente</span>
            </Button>
          </div>

          <div className="mt-4">
            <Button
              onClick={() => console.log("Consumidor Final")}
              className="bg-[#436086] hover:bg-[#384d6b] text-white py-2 px-4 rounded-md flex items-center space-x-2 shadow-sm text-sm"
            >
              <User className="h-4 w-4" />
              <span>Consumidor final</span>
            </Button>
          </div>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-700 mb-3">
            Buscar Producto
          </h2>

          <div className="relative max-w-sm w-full">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
            <Input
              placeholder="Buscar opciones de comida"
              className="pl-10 border border-gray-300 rounded-md shadow-sm text-sm w-full"
            />
          </div>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-700 mb-6">
            Resultados
          </h2>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
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
        </section>

        <footer className="flex justify-end items-center mt-10 space-x-3">
          <Button
            onClick={() => console.log("Anterior")}
            className="flex items-center space-x-1 text-gray-700 bg-white border border-gray-300 rounded-md shadow-sm px-4 py-2 text-sm hover:bg-gray-50"
          >
            <ChevronLeft className="h-4 w-4" />
            <span>Anterior</span>
          </Button>

          <Button
            onClick={() => console.log("Siguiente")}
            className="flex items-center space-x-1 text-gray-700 bg-white border border-gray-300 rounded-md shadow-sm px-4 py-2 text-sm hover:bg-gray-50"
          >
            <span>Siguiente</span>
            <ChevronRight className="h-4 w-4" />
          </Button>
        </footer>
      </div>
    </div>
  );
};

export default SalesPage;
