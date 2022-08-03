<template>
    <div class="d-flex justify-content-center gap-4 flex-wrap">
      <ProductFeature img="logo.png"
                      title="Fast screenshot of selected area"
                      desc="Select any area
                      on your desktop and take its screenshot with 2 button-clicks." />
      <ProductFeature img="logo.png"
                      title="Share screenshots via Internet"
                      desc="Upload your screenshot to the server
                      and get its short link right away." />
      <ProductFeature img="logo.png"
                      title="Easy-to-use application"
                      desc="Simple and intuitive user interface makes your
                      work with the app easier and faster." />
      <ProductFeature img="logo.png"
                      title="Powerful editing tools"
                      desc="You can edit screenshots instantly when taking them with
                      our powerful editting tools." />
      </div>
</template>

<script>
import ProductFeature from '@/components/ProductFeature';

export default {
  name: 'ProductShowcase',
  components: {
    ProductFeature,
  },
};
</script>

<style scoped>

</style>
