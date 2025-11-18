"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate, Link, useLocation } from "react-router-dom";
import { Eye, EyeOff, UtensilsCrossed } from "lucide-react";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from "@/shared/components/ui/form";
import { Button } from "@/shared/components/ui/button";
import { useLogin } from "../hooks/useAuth.js";
import { setAuthToken, clearAuthData } from "../utils/authStorage.js";
import { loginSchema } from "../validators/authValidators.js";

export function LoginForm() {
  const [showPassword, setShowPassword] = useState(false);
  
  const form = useForm({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      username: "",
      password: "",
    },
  });
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/dashboard";

  const login = useLogin({
    onSuccess: (data) => {
      console.log("Login success - Data recibida:", data);
      if (data && data.success && data.data && data.data.token) {
        console.log("Token encontrado, guardando...");
        setAuthToken(data.data.token);
        console.log("Token guardado, navegando a:", from);
        navigate(from, { replace: true });
      } else {
        console.warn("No se recibió token en la respuesta:", data);
        clearAuthData();
        form.setError("root", {
          message: data?.message || "No se recibió token de autenticación. Por favor, intenta de nuevo.",
        });
      }
    },
    onError: (error) => {
      console.error("Error al iniciar sesión:", error);
      clearAuthData();
      form.setError("root", {
        message: error?.message || "Error al iniciar sesión. Por favor, intenta de nuevo.",
      });
    },
  });

  function onSubmit(data) {
    login.mutate(data);
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
            <h1 className="text-2xl font-bold text-gray-800">Iniciar Sesión</h1>
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
            disabled={login.isPending}
          >
            {login.isPending ? "Iniciando sesión..." : "Iniciar Sesión"}
          </Button>
          <div className="text-center text-sm">
            <span className="text-gray-600">¿No tienes una cuenta? </span>
            <Link
              to="/register"
              className="text-orange-600 hover:text-orange-700 font-semibold"
            >
              Regístrate aquí
            </Link>
          </div>
        </form>
      </Form>
    </div>
  );
}
