import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/views/Home.vue';
import Download from '@/views/Download';

const routes = [
  {
    path: '/',
    name: '/',
    component: Home,
  },
  {
    path: '/download',
    name: 'download',
    component: Download,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
