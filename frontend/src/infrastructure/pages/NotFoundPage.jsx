import { Button } from "@/shared/components/ui/button";
import { Home, SearchX } from "lucide-react";
import { Link } from "react-router";

const NotFound = () => {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center space-y-8 text-center">
      <SearchX className="h-32 w-32 text-orange-600" />
      <h1 className="text-9xl font-bold text-black">404</h1>
      <div className="space-y-3">
        <h2 className="text-3xl font-semibold text-orange-600">
          ¡Oops! Página no encontrada
        </h2>
        <p className="text-lg text-black">
          Lo sentimos, la página que buscas no existe, fue eliminada o ha sido
          movida.
        </p>
      </div>
      <Button asChild className="bg-orange-600 hover:bg-yellow-900">
        <Link to="/">
          <Home className="mr-2 h-4 w-4" />
          Volver al Inicio
        </Link>
      </Button>
    </div>
  );
};

export default NotFound;

