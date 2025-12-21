import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Link } from "react-router-dom";
import { Loader2, Mail } from "lucide-react";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from "@/shared/components/ui/form";
import { Input } from "@/shared/components/ui/input";
import { Button } from "@/shared/components/ui/button";
import { AuthLayout } from "@/shared/components/layout/AuthLayout";
import { useForgotPassword } from "../hooks/useAuth";
import { forgotPasswordSchema } from "../validators/authValidators";

export function ForgotPasswordForm() {
  const [isSubmitted, setIsSubmitted] = useState(false);

  const form = useForm({
    resolver: zodResolver(forgotPasswordSchema),
    mode: "onChange",
    defaultValues: {
      email: "",
    },
  });

  const forgotPassword = useForgotPassword({
    onSuccess: (data) => {
      setIsSubmitted(true);
    },
    onError: (error) => {
      console.error("Error al enviar email:", error);
      form.setError("root", {
        message: error?.message || "Error al enviar el correo. Por favor, intenta de nuevo.",
      });
    },
  });

  function onSubmit(data) {
    forgotPassword.mutate(data.email);
  }

  if (isSubmitted) {
    return (
      <AuthLayout>
        <div className="w-full max-w-432 h-263 space-y-6 text-center border border-border bg-background rounded-md shadow-sm p-6">
          <div className="flex flex-col mb-6">
            <div className="w-12 h-12 bg-stokia-blue-light rounded-full flex items-center justify-center mb-2">
              <Mail className="w-6 h-6 text-chart-3" />
            </div>
            <h1 className="text-3xl text-left font-semibold py-2">
              ¡Correo enviado!
            </h1>
            <p className="text-muted-foreground text-sm text-left mb-2">
              Enviamos las instrucciones a tu correo.
              Por favor, revisa tu bandeja de entrada y la carpeta de spam.
            </p>
          </div>

          <Button
            asChild
            variant="stokia"
            className="w-full max-w-384 h-9 mx-auto"
          >
            <Link to="/login">Volver al inicio de sesión</Link>
          </Button>
        </div>
      </AuthLayout>
    );
  }

  return (
    <AuthLayout>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="w-full max-w-432 h-370 space-y-6 px-6 border border-border bg-background rounded-md  "
        >
          <div className="flex flex-col mb-8">
            <h1 className="text-3xl font-semibold py-4">
              Recupera tu contraseña
            </h1>
            <p className="text-muted-foreground text-sm">
              Ingresa tu correo electrónico y te enviaremos un enlace para
              restablecer tu clave.
            </p>
          </div>

          <FormField
            control={form.control}
            name="email"
            render={({ field, fieldState }) => (
              <FormItem>
                <FormLabel className="text-sm font-normal text-foreground">
                  Dirección de correo
                </FormLabel>
                <FormControl>
                  <Input
                    {...field}
                    type="email"
                    placeholder="Introducir correo electrónico"
                    className={`w-full max-w-384 h-9 text-xs ${fieldState.invalid ? "border-destructive" : ""
                      }`}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          {form.formState.errors.root && (
            <div className="text-destructive text-sm text-center">
              {form.formState.errors.root.message}
            </div>
          )}

          <Button
            type="submit"
            disabled={!form.formState.isValid || forgotPassword.isPending}
            variant="stokia"
            className="w-full max-w-384 h-9"
          >
            {forgotPassword.isPending && (
              <Loader2 className="h-4 w-4 animate-spin" />
            )}
            {forgotPassword.isPending ? "Enviando enlance..." : "Enviar enlace"}
          </Button>

          <div className="text-center w-full max-w-384 h-9">
            <Button
              asChild
              variant="stokia"
              className={
                `w-full max-w-384 h-9 bg-secondary text-foreground font-normal hover:bg-secondary
                ${forgotPassword.isPending ? "pointer-events-none opacity-50" : ""}`}
            >
              <Link to="/login">Volver al inicio de sesión</Link>
            </Button>
          </div>
        </form>
      </Form>
    </AuthLayout>
  );
}
