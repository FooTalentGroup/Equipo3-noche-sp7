import React from 'react';
import ReactDOM from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { QueryClientProvider } from '@tanstack/react-query';
import router from '@/infrastructure/router/routes.config.jsx';
import { queryClient } from '@/lib/query-client'; // <-- use named import
import './index.css';
import { Toaster } from '@/shared/components/ui/sonner.jsx';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
    <Toaster />
  </React.StrictMode>
);
