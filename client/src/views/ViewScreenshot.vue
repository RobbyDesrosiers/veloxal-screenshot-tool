<template>
  <div class="image-holder">
    <img id="image" class="user-image" :src="this.screenshot" alt="screenshot">
  </div>
</template>

<script>

export default {
  name: 'ViewScreenshot',
  components: {
  },
  data() {
    return {
      screenshot: '',
    };
  },
  methods: {
    // https://stackoverflow.com/questions/42785229/axios-serving-png-image-is-giving-broken-image
    getScreenshot() {
      this.screenshot = `http://api.veloxal.io/v/${this.$route.params.id}`;
    },
  },
  beforeMount() {
    this.getScreenshot();
  },
};
</script>

<style scoped>
.image-holder {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
