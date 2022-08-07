import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/views/Home.vue';
import Download from '@/views/Download';
import ViewScreenshot from '@/views/ViewScreenshot';
import Ping from '@/views/ping';
import '@/assets/styles.css';

const routes = [
  {
    path: '/',
    name: '/',
    component: Home,
  },
  {
    path: '/ping',
    name: 'ping',
    component: Ping,
  },
  {
    path: '/download',
    name: 'download',
    component: Download,
  },
  {
    path: '/v/:id',
    name: 'view',
    component: ViewScreenshot,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
