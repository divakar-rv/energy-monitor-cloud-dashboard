import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5174
  },
  define: {
    global: 'window', // sockjs-client expects Node's `global`; map it to `window` in the browser
  },
})