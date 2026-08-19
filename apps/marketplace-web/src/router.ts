import { createRouter, createWebHistory } from 'vue-router'
import DiscoverView from './views/DiscoverView.vue'
import SkillDetailView from './views/SkillDetailView.vue'
import MySkillsView from './views/MySkillsView.vue'
import RegisterView from './views/RegisterView.vue'
import OperationView from './views/OperationView.vue'

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/discover' },
    { path: '/discover', component: DiscoverView },
    { path: '/skills/:slug', component: SkillDetailView },
    { path: '/my-skills', component: MySkillsView },
    { path: '/register', component: RegisterView },
    { path: '/operations/:id', component: OperationView }
  ],
  scrollBehavior: () => ({ top: 0 })
})
