<template>
  <div class="d-flex align-items-center prod-feature">
    <div class="img-container">
      <img :src="require(`@/assets/${img}`)" alt="img" class="img-fluid">
    </div>
    <div class="d-flex flex-column ms-3">
      <h5 class="mb-1">{{this.title }}</h5>
      <p>{{ this.desc }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProductFeature',
  props: {
    img: String,
    title: String,
    desc: String,
  },
  data() {
    return {

    };
  },
};
</script>

<style scoped>
.img-container {
  min-width: 80px;
  max-width: 80px;
  height: auto;
}
p, h5 {
  margin: 0;
}
.prod-feature {
  max-width: 450px;
  min-width: 450px;
}
</style>
