<template>
  <div id="app" class="d-flex flex-column">
    <Navbar />
    <router-view/>
    <Footer />
  </div>
</template>
<script>

import Navbar from '@/components/Navbar';
import Footer from '@/components/Footer';

export default {
  name: 'App',
  data() {
    return {
      page: 'Home',
    };
  },
  methods: {
  },
  components: {
    Navbar,
    Footer,
  },
};

</script>
<style>
#app {
  min-height: 100vh;
}
</style>
