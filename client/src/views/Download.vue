<template>
  <main class="mb-5">
    <div class="container d-flex flex-column mt-5 justify-content-center gap-4">
      <h3 class="text-center">Windows Downloads</h3>
      <div class="d-flex gap-3 justify-content-center flex-wrap">
        <DownloadCard
          title="Windows Installer w/ JRE"
          desc="Download this installer with everything bundled including the JDK 17.0.2.
          This is the recommended option"
          size="212mb"
          @btn-click="downloadInstaller"
        />
        <DownloadCard
          title="Windows Executable Jar File"
          desc="Download the executable jar file without bundled JRE/JDK. This will operate if JDK
          17.0.2 is installed."
          size="32mb"
          @btn-click="downloadJar"
        />
      </div>
    </div>
    <div class="container d-flex flex-column mt-5 justify-content-center gap-4 ">
      <h3 class="text-center">Mac Downloads</h3>
      <div class="d-flex gap-3 justify-content-center flex-wrap">
        <DownloadCard
          title="Mac Installer w/ JRE"
          desc="Download this installer with everything bundled including the JDK 17.0.2.
          This is the recommended option"
          active="disabled"
          size="212mb"
        />
        <DownloadCard
          title="Mac Executable Jar File"
          desc="Download the executable jar file without bundled JRE/JDK. This will operate if JDK
          17.0.2 is installed."
          active="disabled"
          size="32mb"
        />
      </div>
    </div>
  </main>
</template>

<script>

import DownloadCard from '@/components/DownloadCard';
import { downloads } from '@/assets/scripts.js';

export default {
  name: 'Download',
  components: {
    DownloadCard,
  },
  methods: {
    downloadInstaller() {
      downloads.downloadInstaller();
    },
    downloadJar() {
      downloads.downloadJar();
    },
  },
};
</script>

<style scoped>

</style>
