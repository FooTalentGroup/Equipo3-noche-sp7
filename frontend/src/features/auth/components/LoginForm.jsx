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
    <div className="min-h-screen w-full grid grid-cols-1 md:grid-cols-2">
    {/* --- Lado izquierdo --- */}
    <div className="hidden md:block w-full h-full"
         style={{ backgroundColor: "#BCBCBC" }}
    ></div>
     
    

    {/* --- Lado derecho ---- */}
    <div className="flex flex-col items-center justify-center p-8">
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="w-full max-w-sm space-y-6"
        >
          <div className="flex flex-col mb-8">
            <h1 className="text-2xl font-roboto font-semibold text-gray-900 py-4">Inicia sesión</h1>
            <p className="text-gray-500 text-sm font-roboto">
              Coloca tu dirección de correo y una contraseña segura
            </p>
          </div>

          <FormField
            control={form.control}
            name="username"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-sm font-normal font-roboto text-gray-700">
                  Dirección de correo
                </FormLabel>
                <FormControl>
                  <input
                    className="w-full p-2 border border-gray-200  rounded-md focus:outline-none 
                    focus:ring-2 focus:ring-gray-900
                    placeholder:text-gray-400 placeholder:text-sm"
                    placeholder="Introducir correo electrónico"
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
                <div className="flex justify-between items-center">
                <FormLabel className="text-sm font-normal font-roboto text-gray-700">
                  Contraseña
                </FormLabel>
                 <Link
                  to="/forgot-password"
                  className="text-xs text-gray-400 hover:text-gray-600 transition font-roboto underline underline-offset-2"
                >
                  Olvidé mi contraseña
                </Link>
                </div>
                <FormControl>
                  <div className="relative">
                    <input
                      type={showPassword ? "text" : "password"}
                      className="w-full p-2 pr-10 border border-gray-200 rounded-md 
                      focus:outline-none focus:ring-2 focus:ring-gray-900 font-normal 
                      font-roboto
                      placeholder:text-gray-400 placeholder:text-sm"
                      placeholder="Introducir contraseña"
                      style={{ "--tw-ring-color": "#436086" }}
                      {...field}
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-2 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700 focus:outline-none"
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
            type="submit"
            disabled={login.isPending}
            className={`
              w-full flex items-center justify-center gap-2 text-sm font-semibold
              border rounded-md transition-all
              ${login.isPending 
                ? "bg-white text-gray-500 border-gray-300 cursor-not-allowed" 
                : "text-white"
              }
            `}
            style={!login.isPending ? { backgroundColor: "#436086" } : {}}
          >
            {login.isPending &&  
            <svg
              className="animate-spin h-5 w-5 text-gray-500"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
            >
              <circle
                className="opacity-25"
                cx="12"
                cy="12"
                r="10"
                stroke="currentColor"
                strokeWidth="4"
              ></circle>
              <path
                className="opacity-75"
                fill="currentColor"
                d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"
              ></path>
            </svg>
            }

          {login.isPending ? "Iniciando sesión" : "Iniciar sesión"}
          </Button>
        </form>
      </Form>
    </div>
  </div>
  );
}
