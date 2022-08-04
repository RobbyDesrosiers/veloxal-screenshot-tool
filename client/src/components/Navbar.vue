<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
      <a class="navbar-brand" href="/">
        <img src="@/assets/logo.png"
             alt="Brand Logo"
             height="30"
             width="30"
             class="d-inline-block align-text-top">
        ScreenShot
      </a>
      <button class="navbar-toggler" type="button"
              data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup"
              aria-controls="navbarNavAltMarkup" aria-expanded="false"
              aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
        <div class="navbar-nav">
          <a class="nav-link"
             v-bind:class="{ active: homeActive }"
             href="/">Home</a>
          <a class="nav-link"
             v-bind:class="{ active: downloadActive }"
             href="/download">Download</a>
          <a class="nav-link" href="https://github.com/RobbyDesrosiers/QuickShot">Github</a>
          <a v-if="!signedIn" class="nav-link login-link" href="/">Sign In</a>
          <a v-else class="nav-link login-link" href="/">Sign Up</a>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'Navbar',
  props: {
    pageOpened: String,
  },
  data() {
    return {
      homeActive: false,
      downloadActive: false,
      signedIn: true,
    };
  },
  methods: {
    selectCurrentPage(page) {
      if (page === 'Home') {
        this.homeActive = true;
      } else if (page === 'Download') {
        this.downloadActive = true;
      }
    },
  },
  created() {
    this.selectCurrentPage(this.pageOpened);
  },
};
</script>

<style scoped>
.navbar {
  border-bottom: 2px solid var(--bs-green);
}
.login-link {
  justify-self: end;
}
</style>
