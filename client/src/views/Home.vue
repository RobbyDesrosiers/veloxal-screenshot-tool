<template>
  <Navbar page-opened="Home"/>
  <main class="container d-flex flex-column mt-5">
    <ScreenShotHeader />
    <ProductShowcase />
  </main>
</template>

<script>

import Navbar from '@/components/Navbar';
import ProductShowcase from '@/components/ProductShowcase';
import ScreenShotHeader from '@/components/ScreenShotHeader';

export default {
  name: 'Home',
  components: {
    ScreenShotHeader,
    ProductShowcase,
    Navbar,
  },
};

</script>

<style scoped>

</style>
