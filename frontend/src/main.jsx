if (typeof global === 'undefined') {
  window.global = window;
}

import React from 'react';
import ReactDOM from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { QueryClientProvider } from '@tanstack/react-query';
import router from '@/infrastructure/router/routes.config.jsx';
import { queryClient } from '@/lib/query-client';
import './index.css';
import { Toaster } from '@/shared/components/ui/sonner.jsx';

if (import.meta.env && import.meta.env.MODE === 'development') {
  const originalInfo = console.info.bind(console);
  console.info = (...args) => {
    try {
      if (typeof args[0] === 'string' && args[0].includes('Download the React DevTools')) return;
    } catch (e) { /* ignore */ }
    originalInfo(...args);
  };
}

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
    <Toaster />
  </React.StrictMode>
);
