<template>
  <h2 class="text-center mt-4">Veloxal.io UI</h2>
  <div class="d-flex justify-content-center gap-4 flex-wrap mb-4 mt-2">
    <img class="img-fluid" :src="require(`@/assets/product_1.png`)" alt="product img">
    <img class="img-fluid" :src="require(`@/assets/product_3.png`)" alt="product img">
    <img class="img-fluid" :src="require(`@/assets/product_2.png`)" alt="product img">
  </div>
</template>

<script>
export default {
  name: 'ProductImages',
};
</script>

<style scoped>
.img-fluid {
  max-width: 400px;
  height: auto;
}
@media only screen and (max-width: 1400px) {
  .img-fluid {
    max-width: 100%;
  }
}
</style>
