<template>
  <div class="d-flex align-items-center prod-feature">
    <div class="icon-container">
      <background-blob />
      <img :src="require(`@/assets/${img}`)" alt="img" class="img-fluid">
    </div>
    <div class="d-flex flex-column text">
      <h5 class="mb-1">{{this.title }}</h5>
      <p>{{ this.desc }}</p>
    </div>
  </div>
</template>

<script>
import BackgroundBlob from '@/components/BackgroundBlob';

export default {
  name: 'ProductFeature',
  components: {
    BackgroundBlob,
  },
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
.icon-container {
  min-width: 80px;
  min-height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.img-fluid {
  position: relative;
  width: 50px;
  height: 50px;
}
p, h5 {
  margin: 0;
}
.prod-feature {
  max-width: 450px;
  min-width: 450px;
  gap: 15px;
}
@media only screen and (max-width: 450px) {
  .prod-feature {
    max-width: 400px;
    min-width: 400px;
  }
}
@media only screen and (max-width: 400px) {
  .prod-feature {
    display: flex;
    flex-direction: column;
    max-width: 350px;
    min-width: 350px;
    gap: 8px;
  }
  .text {
    text-align: center;
  }
}
@media only screen and (max-width: 350px) {
  .prod-feature {
    display: flex;
    flex-direction: column;
    max-width: 280px;
    min-width: 280px;
    gap: 8px;
  }
  .text {
    text-align: center;
  }
}
</style>
