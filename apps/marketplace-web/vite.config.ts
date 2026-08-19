import { defineConfig } from 'vitest/config'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: { port: 5173, proxy: { '/api': { target: 'http://localhost:8080', auth: 'atlas-local:local-only' } } },
  test: { environment: 'jsdom', globals: true }
})
