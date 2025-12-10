import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate, useSearchParams, Link } from "react-router-dom";
import { Eye, EyeOff, Loader2 } from "lucide-react";
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
import { useResetPassword } from "../hooks/useAuth";
import {resetPasswordSchema } from "../validators/authValidators";
import { AuthLayout } from "@/shared/components/layout/AuthLayout";



export function ResetPasswordForm() {
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const token = searchParams.get("token");

  const form = useForm({
    resolver: zodResolver(resetPasswordSchema),
    defaultValues: {
      password: "",
      confirmPassword: "",
    },
  });

  const resetPassword = useResetPassword({
    onSuccess: () => {
      navigate("/reset-password-success");
    },
    onError: (error) => {
      form.setError("root", {
        message: error?.message || "Error al restablecer contraseña.",
      });
    },
  });

  function onSubmit(data) {
    if (!token) {
      form.setError("root", { message: "Token inválido o expirado" });
      return;
    }
    resetPassword.mutate({ token, password: data.password });
  }

  return (
      <AuthLayout>
      <div className="fixed inset-0 bg-black/10 flex items-center justify-center z-50 p-4">
        <div className="relative bg-secondary rounded-lg p-8  max-w-[760px] shadow-2xl">
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <div className="space-y-2">
                <h1 className="text-3xl font-semibold text-foreground">
                  Restablecer contraseña
                </h1>
                <p className="text-muted-foreground text-xs">
                  Ingresa tu nueva clave para recuperar el acceso a tu cuenta.
                </p>
              </div>
              <hr className="border border-border my-4"/>
              <FormField
                control={form.control}
                name="password"
                render={({ field, fieldState }) => (
                  <FormItem>
                    <FormLabel className="text-sm font-normal font-roboto text-foreground">
                      Nueva contraseña <span className="text-destructive">*</span>
                    </FormLabel>
                    <FormControl>
                      <div className="relative">
                        <Input
                          {...field}
                          type={showPassword ? "text" : "password"}
                          placeholder="Ingrese contraseña"
                          className={`${fieldState.invalid ? "border-destructive" : ""} w-[664px]`}
                        />
                        <button
                          type="button"
                          onClick={() => setShowPassword(!showPassword)}
                          className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                        >
                          {showPassword ? (
                            <EyeOff className="h-4 w-4" />
                          ) : (
                            <Eye className="h-4 w-4" />
                          )}
                        </button>
                      </div>
                    </FormControl>
                    <p className="text-xs text-muted-foreground">
                      Mínimo 8 caracteres, incluyendo 1 mayúscula y 1 número.
                    </p>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="confirmPassword"
                render={({ field, fieldState }) => (
                  <FormItem>
                    <FormLabel className="text-sm font-normal font-roboto text-foreground">
                      Repetir contraseña <span className="text-destructive">*</span>
                    </FormLabel>
                    <FormControl>
                      <div className="relative">
                        <Input
                          {...field}
                          type={showConfirmPassword ? "text" : "password"}
                          placeholder="Repetir contraseña "
                          className={fieldState.invalid ? "border-destructive" : ""}
                        />
                        <button
                          type="button"
                          onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                          className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground"
                        >
                          {showConfirmPassword ? (
                            <EyeOff className="h-4 w-4" />
                          ) : (
                            <Eye className="h-4 w-4" />
                          )}
                        </button>
                      </div>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {form.formState.errors.root && (
                <div className="text-destructive text-sm">
                  {form.formState.errors.root.message}
                </div>
              )}

              <div className="flex justify-end gap-3 mt-70">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => navigate("/login")}
                >
                  Cancelar
                </Button>

                <Button
                  type="submit"
                  disabled={
                    resetPassword.isPending ||
                    !form.watch("password") ||
                    !form.watch("confirmPassword")
                  }
                  variant="stokia"
                >
                  {resetPassword.isPending && (
                    <Loader2 className="h-4 w-4 animate-spin mr-2" />
                  )}
                  Actualizar contraseña
                </Button>
              </div>
            </form>
          </Form>
        </div>
      </div>
    </AuthLayout>
  );
}