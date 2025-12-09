import { defineConfig } from 'vite'
import tailwindcss from '@tailwindcss/vite'
import react from '@vitejs/plugin-react'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  // shim `global` for libraries that expect a Node-like global in the browser (e.g. sockjs-client)
  define: {
    global: 'window',
  },
  // dev server proxy: forward websocket/sockjs requests to backend server
  server: {
    proxy: {
      // Forward /ws to backend websocket endpoint. Change target if your backend runs elsewhere.
      '/ws': {
        target: 'http://localhost:8080',
        ws: true,
        changeOrigin: true,
      },
    },
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
})
