import { Check } from "lucide-react";
import { Link } from "react-router-dom";
import { Button } from "@/shared/components/ui/button";
import { AuthLayout } from "@/shared/components/layout/AuthLayout";

export function ResetPasswordSuccess() {
  return (
    <AuthLayout>
      <div className="w-full max-w-[432px] h-[270px] space-y-6 text-center border border-border bg-background rounded-md shadow-sm p-6">
        <div className="flex flex-col mb-6">
          <div className="w-12 h-12 bg-stokia-blue-light rounded-full flex items-center justify-center mb-2">
              <Check className="w-6 h-6" />
          </div>
            <h1 className="text-3xl text-left font-semibold py-2">
              ¡Contraseña actualizada!
            </h1>
            <p className ="text-ring text-sm text-left mb-2">
              Tu contraseña se restableció con éxito. Ingresa con 
              tu nueva clave.
            </p>
          </div>

          <Button
            asChild
            variant="stokia"
            className="w-full"
          >
            <Link to="/login">Iniciar sesión</Link>
          </Button>
        </div>
    </AuthLayout>
  );
}