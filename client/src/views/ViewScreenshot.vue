<template>
  <div class="image-holder">
    <img class="user-image" :src="this.screenshot" alt="screenshot">
  </div>
</template>

<script>
import axios from 'axios';

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
      const path = `http:/api.veloxal.io/v/${this.$route.params.id}`;
      axios.get(path, { responseType: 'arraybuffer' })
        .then((res) => {
          const blob = new Blob(
            [res.data],
            { type: res.headers['content-type'] },
          );
          this.screenshot = URL.createObjectURL(blob);
        })
        .catch((error) => {
          console.log(error);
        });
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
