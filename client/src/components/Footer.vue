<template>
  <div class="footer d-flex mt-auto">
    <div class="container d-flex align-items-center justify-content-between">
      <img src="@/assets/veloxal_logo.png" alt="Veloxal Logo" class="img-fluid">
      <nav>

      </nav>
      <p class="text-white mb-0">Robby Desrosiers 2022</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Footer',
};
</script>

<style scoped>
  .footer {
    height: 60px;
    width: 100%;
    background-color: rgb(35,35,35);
  }
  .img-fluid {
    height: 30px;
    width: auto;
  }
</style>
