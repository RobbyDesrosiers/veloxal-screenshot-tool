<template>
  <div class="cont mb-5">
    <img :src="require(`@/assets/veloxal_stacked_logo.png`)" alt="logo" class="logo">
    <div class="d-flex gap-3">
      <button
        type="button"
        @click="downloadInstaller"
        class="btn btn-dark rounded-pill">
        Windows Installer
      </button>
      <button
        type="button"
        class="btn btn-dark rounded-pill disabled">
        Mac Installer
      </button>
    </div>
  </div>
</template>

<script>
import { downloads } from '@/assets/scripts.js';

export default {
  name: 'HomeDownload',
  methods: {
    downloadInstaller() {
      downloads.downloadInstaller();
    },
  },
};
</script>

<style scoped>
  .cont {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    gap: 15px;
  }
  .logo {
    width: 500px;
  }
  @media screen and (max-width: 500px) {
    .logo {
      width: 400px;
    }
  }
  @media screen and (max-width: 400px) {
    .logo {
      width: 300px;
    }
  }
  @media screen and (max-width: 300px) {
    .logo {
      width: 200px;
    }
  }
</style>
