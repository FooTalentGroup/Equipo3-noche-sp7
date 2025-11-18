import { RouterProvider } from "react-router-dom";
import router from "@/infrastructure/router/routes.config.jsx";
import { Toaster } from "@/shared/components/ui/sonner.jsx";

function App() {
  return (
    <>
      <RouterProvider router={router} />
      <Toaster />
    </>
  );
}

export default App;
