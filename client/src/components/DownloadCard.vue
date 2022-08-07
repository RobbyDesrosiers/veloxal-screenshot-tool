<template>
  <div class="card-container d-flex flex-column align-items-center gap-3">
    <h5 class="mt-3">{{ title }}</h5>
    <button type="button" class="btn btn-dark rounded-pill" :class="active">
      Download
    </button>
    <p class="container desc mb-0">{{ desc }}</p>
    <p class="container desc fw-bold">Size: {{ size }}</p>
  </div>
</template>

<script>
export default {
  name: 'DownloadCard',
  props: {
    title: String,
    desc: String,
    size: String,
    active: String,
  },
};
</script>

<style scoped>
  .card-container {
    width: 300px;
    border-radius: 10px;
    background-color: var(--bs-light);
  }
  .desc {
    text-align: center;
  }
</style>
