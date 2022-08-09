<template>
  <main class="container d-flex flex-column mt-5">
    <div class="alert alert-primary mb-5 text-center" role="alert">
      Thanks for viewing veloxal.io! This site is still under maintenance as the back
      end is not quite ready yet. Please checkout the
      <a href="https://github.com/RobbyDesrosiers/veloxal-screenshot-tool">
        github</a> if you have time!
    </div>
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
