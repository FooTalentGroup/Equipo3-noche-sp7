"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate, Link } from "react-router-dom";
import { Eye, EyeOff, UtensilsCrossed } from "lucide-react";
import { toast } from "sonner";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from "@/shared/components/ui/form";
import { Button } from "@/shared/components/ui/button";
import { useRegister } from "../hooks/useAuth.js";
import { setAuthToken, clearAuthData } from "../utils/authStorage.js";
import { registerSchema } from "../validators/authValidators.js";

export function RegisterForm() {
  const [showPassword, setShowPassword] = useState(false);
  
  const form = useForm({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      username: "",
      password: "",
    },
  });
  const navigate = useNavigate();

  const register = useRegister({
    onSuccess: (data) => {
      if (data && data.success && data.data && data.data.token) {
        setAuthToken(data.data.token);
        toast.success("¡Registro exitoso!", {
          description: "Tu cuenta ha sido creada correctamente.",
        });
        navigate("/dashboard", { replace: true });
      } else if (data && data.success) {
        clearAuthData();
        toast.info("Registro completado", {
          description: "Por favor, inicia sesión para continuar.",
        });
        navigate("/login", { replace: true });
      }
    },
    onError: (error) => {
      console.error("Error al registrarse:", error);
      clearAuthData();

      const errorData = error?.response?.data || error?.data || {};
      const errorDetails = errorData.details || {};

      if (errorData.error === 'VALIDATION_ERROR' && Object.keys(errorDetails).length > 0) {
        Object.keys(errorDetails).forEach((field) => {
          form.setError(field, {
            type: 'server',
            message: errorDetails[field],
          });
        });
        toast.error("Error de validación", {
          description: errorData.message || "Por favor, corrige los errores en el formulario.",
        });
      } else {
        toast.error("Error al registrarse", {
          description: errorData.message || error.message || "Por favor, intenta de nuevo.",
        });
        form.setError("root", {
          message: errorData.message || error.message || "Error al registrarse. Por favor, intenta de nuevo.",
        });
      }
    },
  });

  function onSubmit(data) {
    register.mutate(data);
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-8">
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="p-6 border rounded-lg shadow-lg w-full max-w-sm space-y-6"
        >
          <div className="flex flex-col items-center gap-3 mb-4">
            <div className="flex items-center gap-2 text-orange-600">
              <UtensilsCrossed className="h-8 w-8" />
              <span className="text-2xl font-bold">ProEat</span>
            </div>
            <h1 className="text-2xl font-bold text-gray-800">Crear Cuenta</h1>
            <p className="text-sm text-gray-600 text-center">
              Regístrate para comenzar a usar ProEat
            </p>
          </div>
          <FormField
            control={form.control}
            name="username"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-sm font-semibold text-orange-600">
                  Username
                </FormLabel>
                <FormControl>
                  <input
                    className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
                    placeholder="Username"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-sm font-semibold text-orange-600">
                  Password
                </FormLabel>
                <FormControl>
                  <div className="relative">
                    <input
                      type={showPassword ? "text" : "password"}
                      className="w-full p-2 pr-10 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
                      placeholder="Password"
                      {...field}
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-2 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700 focus:outline-none"
                      aria-label={showPassword ? "Ocultar contraseña" : "Mostrar contraseña"}
                    >
                      {showPassword ? (
                        <EyeOff className="h-5 w-5" />
                      ) : (
                        <Eye className="h-5 w-5" />
                      )}
                    </button>
                  </div>
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          {form.formState.errors.root && (
            <div className="text-red-500 text-sm text-center">
              {form.formState.errors.root.message}
            </div>
          )}
          <Button
            className="w-full text-sm font-semibold bg-orange-600 hover:bg-yellow-900 text-white"
            type="submit"
            disabled={register.isPending}
          >
            {register.isPending ? "Registrando..." : "Registrarse"}
          </Button>
          <div className="text-center text-sm">
            <span className="text-gray-600">¿Ya tienes una cuenta? </span>
            <Link
              to="/login"
              className="text-orange-600 hover:text-orange-700 font-semibold"
            >
              Inicia sesión aquí
            </Link>
          </div>
        </form>
      </Form>
    </div>
  );
}
