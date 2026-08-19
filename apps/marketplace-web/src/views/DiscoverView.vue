<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { api, type SkillCard } from '../api'

const query = ref('')
const category = ref('IBM_ISERIES')
const skills = ref<SkillCard[]>([])
const totalItems = ref(0)
const loading = ref(false)
const error = ref('')
const certifiedCount = computed(() => skills.value.filter((skill) => skill.certificationState === 'CERTIFIED').length)

async function search() {
  loading.value = true
  error.value = ''
  try {
    const result = await api.search(query.value, category.value)
    skills.value = result.items
    totalItems.value = result.totalItems
  } catch (cause) {
    error.value = cause instanceof Error ? cause.message : 'Search failed.'
  } finally {
    loading.value = false
  }
}
function selectCategory(value: string) { category.value = value; void search() }
function clearFilters() { query.value = ''; category.value = ''; void search() }
onMounted(search)
</script>

<template>
  <section class="marketplace-hero">
    <div class="container">
      <div class="hero-badge">Internal AI Skills Marketplace</div>
      <h1>Find the right Skill<br>before you start coding.</h1>
      <p>Discover curated internal Skills across the software delivery lifecycle. Understand quality, ownership, compatibility and usage before you install.</p>
      <div class="hero-actions"><a class="button primary" href="#featured">Explore Skills</a><RouterLink class="button" to="/register">Register a Skill</RouterLink></div>
      <form class="prototype-search" role="search" @submit.prevent="search">
        <span class="search-icon" aria-hidden="true">⌕</span><label class="sr-only" for="skill-search">Search Skills</label>
        <input id="skill-search" v-model="query" placeholder="Search Skills, use cases, owners, tags or repositories...">
        <button class="button primary compact" type="submit">Search</button>
      </form>
      <div class="filter-pills" aria-label="Skill categories">
        <button :class="['filter-pill', { active: category === '' }]" type="button" @click="selectCategory('')">All categories</button>
        <button :class="['filter-pill', { active: category === 'IBM_ISERIES' }]" type="button" @click="selectCategory('IBM_ISERIES')">IBM iSeries</button>
      </div>
      <div class="catalog-stats" aria-label="Catalog summary">
        <div class="stat"><strong>{{ totalItems }}</strong><span>Published {{ totalItems === 1 ? 'Skill' : 'Skills' }}</span></div>
        <div class="stat"><strong>1</strong><span>Pilot category</span></div>
        <div class="stat"><strong>{{ certifiedCount }}</strong><span>Certified versions</span></div>
        <div class="stat"><strong>Local</strong><span>Registry environment</span></div>
      </div>
    </div>
  </section>

  <section id="featured" class="content-section">
    <div class="container">
      <header class="section-heading"><div><div class="kicker">Featured Skills</div><h2>Ready for the work in front of you</h2></div><p>Owner-maintained capabilities with version, certification, evidence and access information visible before installation.</p></header>
      <p v-if="loading" class="state-panel" role="status">Searching Atlas…</p>
      <div v-else-if="error" class="state-panel error" role="alert"><strong>Atlas could not load the catalog.</strong><span>{{ error }}</span><button class="button" type="button" @click="search">Retry</button></div>
      <div v-else-if="!skills.length" class="state-panel"><strong>No matching Skills</strong><span>Try a different keyword or clear the category filter.</span><button class="button" type="button" @click="clearFilters">Clear filters</button></div>
      <div v-else class="skill-grid" aria-live="polite">
        <RouterLink v-for="skill in skills" :key="skill.id" class="skill-card" :to="`/skills/${skill.slug}`">
          <div class="card-top"><span class="skill-icon" aria-hidden="true">{{ skill.displayName.charAt(0) }}</span><span class="verified">✓ {{ skill.certificationState === 'CERTIFIED' ? 'Certified' : 'Published' }}</span></div>
          <h3>{{ skill.displayName }}</h3><div class="owner">{{ skill.ownerDisplayName }}</div><p class="description">{{ skill.description }}</p>
          <p v-if="skill.curatedPreview" class="preview"><span>Example</span>{{ skill.curatedPreview }}</p>
          <div class="tags"><span class="tag phase-tag">Discovery</span><span class="tag">{{ skill.category.label }}</span><span v-for="tag in skill.tags" :key="tag" class="tag">{{ tag }}</span></div>
          <div class="card-metrics"><div><span>Version</span><strong>{{ skill.latestPublishedVersion ?? '—' }}</strong></div><div><span>Quality</span><strong>{{ skill.certificationState }}</strong></div><div><span>Rating</span><strong v-if="skill.rating.state === 'RATED'">★ {{ skill.rating.value }}</strong><strong v-else>Not rated</strong></div></div>
        </RouterLink>
      </div>
    </div>
  </section>

  <section class="content-section value-section"><div class="container value-panel"><div><div class="kicker">Why Atlas Marketplace</div><h2>Useful Skills, without centralizing their source.</h2><p>Owners keep their repositories and release workflows. Atlas makes discovery, evidence and managed lifecycle state visible in one trusted place.</p></div><div class="feature-grid"><article><strong>Distributed by design</strong><p>Source stays in owner-managed repositories.</p></article><article><strong>Quality visible</strong><p>Version and certification evidence at a glance.</p></article><article><strong>Context aware</strong><p>Browse by platform and concrete use case.</p></article><article><strong>Safe adoption</strong><p>Explicit scope, ownership and lifecycle boundaries.</p></article></div></div></section>
</template>
