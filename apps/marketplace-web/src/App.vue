<script setup lang="ts">
import { ref } from 'vue'

const dark = ref(true)
function toggleTheme() {
  dark.value = !dark.value
  document.documentElement.dataset.theme = dark.value ? 'dark' : 'light'
}
</script>

<template>
  <a class="skip-link" href="#main">Skip to content</a>
  <header class="site-header">
    <div class="container nav-shell">
      <RouterLink class="brand" to="/discover" aria-label="Atlas Marketplace home">
        <span class="brand-mark" aria-hidden="true">A</span><span>Atlas Marketplace</span>
      </RouterLink>
      <nav class="nav-links" aria-label="Primary">
        <RouterLink to="/discover">Discover</RouterLink><RouterLink to="/my-skills">My Skills</RouterLink>
      </nav>
      <div class="nav-actions">
        <button class="button compact" type="button" @click="toggleTheme">{{ dark ? 'Light Theme' : 'Dark Theme' }}</button>
        <RouterLink class="button compact desktop-action" to="/my-skills">My Skills</RouterLink>
        <RouterLink class="button primary compact" to="/register">Register Skill</RouterLink>
      </div>
    </div>
  </header>
  <main id="main"><RouterView /></main>
  <footer class="site-footer"><div class="container footer-content"><span>Atlas Marketplace · Internal Engineering Platform</span><span>Discover · Install · Update · Uninstall</span></div></footer>
</template>
