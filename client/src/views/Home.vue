<template>
  <main class="container d-flex flex-column mt-5">
    <HomeDownload />
    <ScreenShotHeader />
    <ProductShowcase />
  </main>
</template>

<script>
import ProductShowcase from '@/components/ProductShowcase';
import ScreenShotHeader from '@/components/ScreenShotHeader';
import HomeDownload from '@/components/HomeDownload';

export default {
  name: 'Home',
  components: {
    HomeDownload,
    ScreenShotHeader,
    ProductShowcase,
  },
};

</script>

<style scoped>

</style>
