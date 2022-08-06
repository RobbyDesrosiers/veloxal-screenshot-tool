<template>
    <div class="shape"></div>
</template>

<script>
export default {
  name: 'BackgroundBlob',
};
</script>

<style scoped>

* {
  box-sizing: border-box;
}

.shape {
  position: absolute;
  background: rgb(64,178,255);
  animation: morph 8s ease-in-out infinite;
  border-radius: 60% 40% 30% 70% / 60% 30% 70% 40%;
  height: 82px;
  width: 82px;
}

@keyframes morph {
  0% {
    border-radius:  60% 40% 30% 70% / 60% 30% 70% 40%;
  }

  50% {
    border-radius:  40% 50% 40% 60% / 50% 40% 60% 50%;
  }

  100% {
    border-radius:  60% 40% 30% 70% / 60% 30% 70% 40%;
  }
}
</style>
