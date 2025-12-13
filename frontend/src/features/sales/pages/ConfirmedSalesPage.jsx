import Skeleton from "@/shared/components/ui/Skeleton";
import ConfirmedOrderCard from "../components/confirmed/ConfirmedOrderCard";
import { AlertTriangle } from "lucide-react";
import { useConfirmedSales } from "../hooks/useConfirmedSales";

function ConfirmedSalesPage() {
  const { data, isLoading, error } = useConfirmedSales();

  const renderSkeletons = () => (
    <div className="flex flex-wrap gap-4 content-start w-full">
      {Array.from({ length: 6 }).map((_, index) => (
        <div className="h-44 w-60" key={index}>
          <Skeleton className="h-full w-full" />
        </div>
      ))}
    </div>
  );

  let content;

  if (isLoading) {
    content = renderSkeletons();

  } else if (error) {
    content = (
      <div className="w-full flex flex-col items-center justify-center p-12 text-stokia-neutral-600">
        <h3 className="text-xl font-semibold text-stokia-neutral-950">Error</h3>
        <p className="text-center mt-2">No se pudieron cargar las órdenes confirmadas. Por favor, revisa tu conexión a internet e intenta de nuevo.</p>
      </div>
    );

  } else if (data?.content?.length > 0) {
    content = data.content.map((order) => (
      <ConfirmedOrderCard key={order.id} order={order} />
    ));

  } else {
    content = (
      <div className="w-full flex flex-col items-center justify-center p-12 text-stokia-neutral-600">
        <AlertTriangle className="h-16 w-16 text-yellow-500 mb-4" />
        <p className="text-lg font-medium">No hay órdenes confirmadas.</p>
        <p className="text-center mt-1 text-sm">Las ventas registradas aparecerán aquí una vez que el pago haya sido finalizado.</p>
      </div>
    );
  }


  return (
    < section className="h-full w-full flex flex-wrap border border-stokia-neutral-300 gap-4 rounded-lg p-4 content-start" >
      {content}
    </section >
  );
}

export default ConfirmedSalesPage;