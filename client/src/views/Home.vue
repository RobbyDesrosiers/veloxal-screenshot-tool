<template>
  <main class="container d-flex flex-column mt-5">
    <HomeDownload />
    <ScreenShotHeader />
    <ProductShowcase />
    <ProductImages />
  </main>
</template>

<script>
import ProductShowcase from '@/components/ProductShowcase';
import ScreenShotHeader from '@/components/ScreenShotHeader';
import HomeDownload from '@/components/HomeDownload';
import ProductImages from '@/components/ProductImages';

export default {
  name: 'Home',
  components: {
    ProductImages,
    HomeDownload,
    ScreenShotHeader,
    ProductShowcase,
  },
};

</script>

<style scoped>

</style>
