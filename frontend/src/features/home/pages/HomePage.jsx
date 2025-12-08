import dashboardLinkCard from "@/assets/dashboard-link-card.jpg";
import { getUsername } from "@/features/auth/utils/authStorage";
import { BookKey, Package, ShoppingBag, Users } from "lucide-react";
import LinkCard from "../component/LinkCard";
import InfoCard from "../component/InfoCard";

const HomePage = () => {
  return (
    <section>
      <h1 className="text-4xl font-bold">{`¡Hola, ${getUsername()}! Te damos la bienvenida`}</h1>
      <div className="grid grid-cols-4 auto-rows-fr gap-6 mt-8">
        <LinkCard
          image={dashboardLinkCard}
          title="Registrar cliente"
          icon={BookKey}
          className="col-span-2"
        />
        <LinkCard
          href="/sales"
          image={dashboardLinkCard}
          title="Registrar venta"
          icon={BookKey}
          className="col-span-2"
        />
        <LinkCard
          href="/products/create"
          image={dashboardLinkCard}
          title="Registrar producto"
          icon={BookKey}
          className="col-span-2"
        />
        <LinkCard
          href="/reports"
          image={dashboardLinkCard}
          title="Generar reporte"
          icon={BookKey}
          className="col-span-2"
        />
        <InfoCard
          title="Productos"
          badgeIcon={Package}
          quantity={10}
          description="Productos con poco stock"
          className="col-span-1"
        />
        <InfoCard
          title="Ventas"
          badgeIcon={ShoppingBag}
          quantity={80}
          description="Ventas registradas esta semana"
          className="col-span-1"
        />
        <InfoCard
          title="Clientes"
          badgeIcon={Users}
          quantity={6}
          description="Clientes están siendo atendidos"
          className="col-span-2"
        />
      </div>
    </section>
  );
};

export default HomePage;
