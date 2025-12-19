import { getUsername } from "@/features/auth/utils/authStorage";
import { AlertCircle, FileChartColumnIncreasing, PackagePlus, ShoppingBag, UserRoundPlus, Users } from "lucide-react";
import LinkCard from "../component/LinkCard";
import InfoCard from "../component/InfoCard";
import { useHomeStatistics } from "../home/useHomeStatistics";
import { InfoBanner } from "../component/HomeInfoBanner";

const LINK_CARDS = [
  {
    title: "Registrar cliente",
    icon: UserRoundPlus,
    href: "/customers",
    className: "col-span-2 sm:col-span-1",
    description: "Iniciar la carga de datos de un nuevo cliente.",
  },
  {
    title: "Registrar venta",
    icon: ShoppingBag,
    href: "/sales",
    className: "col-span-2 sm:col-span-1 [&_span]:bg-stokia-green-200 [&_svg]:text-stokia-green-600",
    description: "Iniciar la carga de un nuevo proceso de venta.",
  },
  {
    title: "Registrar producto",
    icon: PackagePlus,
    href: "/products/create",
    className: "col-span-2 sm:col-span-1 [&_span]:bg-stokia-yellow-200 [&_svg]:text-stokia-yellow-600",
    description: "Iniciar la carga de datos de un nuevo producto.",
  },
  {
    title: "Generar reporte",
    icon: FileChartColumnIncreasing,
    href: "/reports",
    className: "col-span-2 sm:col-span-1 [&_span]:bg-stokia-orange-200 [&_svg]:text-stokia-orange-600",
    description: "Crear métricas detalladas sobre los procesos.",
  },
];

const HomePage = () => {
  const { products, sales, pendingSales } = useHomeStatistics();
  return (
    <section className="max-w-5xl flex flex-col gap-6">
      <h1 className="text-4xl font-bold">{`¡Hola, ${getUsername()}! Te damos la bienvenida`}</h1>
      <InfoBanner />
      <div className="flex flex-col gap-6">
        <h4 className="text-2xl">Resumen actual</h4>
        <div className="grid grid-cols-2 lg:grid-cols-3 lg:auto-rows-fr gap-6">
          <InfoCard
            href="/products"
            title="Bajo stock"
            badgeIcon={AlertCircle}
            quantity={products}
            description="Productos que requieren atención"
            className="col-span-2 lg:col-span-1 [&_span]:bg-stokia-red-200 [&_svg]:text-destructive"
          />
          <InfoCard
            href="/sales/confirmed"
            title="Ventas"
            badgeIcon={ShoppingBag}
            quantity={sales}
            description="Ventas realizadas esta semana"
            className="col-span-2 lg:col-span-1 [&_span]:bg-stokia-green-200 [&_svg]:text-stokia-green-600"
          />
          <InfoCard
            href="/customers"
            title="Clientes"
            badgeIcon={Users}
            quantity={pendingSales}
            description="Clientes atendidos esta semana"
            className="col-span-2 lg:col-span-1"
          />
        </div>
      </div>
      <div className="flex flex-col gap-6">
        <h4 className="text-2xl">¿Qué quieres hacer hoy?</h4>
        <div className="grid grid-cols-2 lg:grid-cols-4 lg:auto-rows-fr gap-6">
          {LINK_CARDS.map(({ title, icon, href, className, description }) => (
            <LinkCard
              key={title}
              title={title}
              icon={icon}
              href={href}
              className={className}
              description={description}
            />
          ))}

        </div>
      </div>
    </section>
  );
};

export default HomePage;