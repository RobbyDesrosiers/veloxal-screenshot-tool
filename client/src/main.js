import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

// tut: https://testdriven.io/blog/developing-a-single-page-app-with-flask-and-vuejs/

const application = createApp(App);
application.use(router);
application.mount('#app');
