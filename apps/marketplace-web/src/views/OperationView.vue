<script setup lang="ts">
import { onMounted,ref } from 'vue';import { useRoute } from 'vue-router';import { api } from '../api'
const route=useRoute(),operation=ref<{state:string;message:string}>(),error=ref('')
onMounted(async()=>{try{operation.value=await api.operation(String(route.params.id))}catch(e){error.value=e instanceof Error?e.message:'Operation unavailable.'}})
</script>
<template><section class="shell"><div v-if="error" class="error" role="alert">{{error}}</div><div v-else-if="!operation" role="status">Loading operation…</div><div v-else class="panel"><div class="eyebrow">Lifecycle result</div><h1>{{operation.state}}</h1><p class="muted">{{operation.message}}</p><div v-if="operation.state==='SUCCEEDED'" class="panel"><strong>Local development result</strong><p>This confirms Registry API orchestration only. It is not evidence of a real Host installation or invocation.</p></div><RouterLink class="button" to="/my-skills">Go to My Skills</RouterLink></div></section></template>
