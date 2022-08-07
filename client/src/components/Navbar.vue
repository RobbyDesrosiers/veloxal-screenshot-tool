<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
      <a class="navbar-brand" href="/">
        <img src="@/assets/veloxal_word_logo.png"
             alt="Brand Logo"
             height="30"
             class="d-inline-block align-text-top">
      </a>
      <button class="navbar-toggler" type="button"
              data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup"
              aria-controls="navbarNavAltMarkup" aria-expanded="false"
              aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
        <div class="navbar-nav">
          <router-link class="nav-link"
             to="/">Home</router-link>
          <router-link class="nav-link"
             to="/download">Download</router-link>
          <a class="nav-link" href="https://github.com/RobbyDesrosiers/veloxal-screenshot-tool">Github</a>
          <a v-if="!signedIn" class="nav-link login-link disabled" href="/">Sign In</a>
          <a v-else class="nav-link login-link disabled" href="/">Sign Up</a>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'Navbar',
  props: {
  },
  data() {
    return {
      signedIn: true,
    };
  },
  methods: {
  },
};
</script>

<style scoped>
.navbar {
  border-bottom: 2px solid var(--primary-blue);
}
.login-link {
  justify-self: end;
}
</style>
