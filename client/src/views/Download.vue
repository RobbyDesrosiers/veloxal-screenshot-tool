<template>
  <main class="mb-5">
    <div class="container d-flex flex-column mt-5 justify-content-center gap-4">
      <div v-if="downloadProgressMessage"
           v-bind:class="downloadAlert()"
           class="alert text-center" role="alert">
        {{ this.downloadProgressMessage }}
      </div>
      <h3 class="text-center">Windows Downloads</h3>
      <div class="d-flex gap-3 justify-content-center flex-wrap">
        <DownloadCard
          title="Windows Installer w/ JRE"
          desc="Download this installer with everything bundled including the JDK 17.0.2.
          This is the recommended option"
          size="189mb"
          @btn-click="downloadInstaller"
        />
        <DownloadCard
          title="Windows Executable Jar File"
          desc="Download the executable jar file without bundled JRE/JDK. This will operate if JDK
          17.0.2 is installed."
          size="38mb"
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
          size="0mb"
        />
        <DownloadCard
          title="Mac Executable Jar File"
          desc="Download the executable jar file without bundled JRE/JDK. This will operate if JDK
          17.0.2 is installed."
          active="disabled"
          size="0mb"
        />
      </div>
    </div>
  </main>
</template>

<script>

import DownloadCard from '@/components/DownloadCard';
import axios from 'axios';

export default {
  name: 'Download',
  components: {
    DownloadCard,
  },
  data() {
    return {
      downloadProgressMessage: '',
      downloadComplete: false,
    };
  },
  methods: {
    downloadInstaller() {
      axios({
        url: 'http://api.veloxal.io/get-windows-installer',
        method: 'GET',
        responseType: 'blob',
        onDownloadProgress: (progressEvent) => {
          const percentage = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          this.downloadProgressMessage = `Downloading File: ${percentage}%`;
          if (percentage === 100) {
            this.downloadComplete = true;
            this.downloadProgressMessage = 'File Downloaded Successfully';
          }
        },
      }).then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'veloxal-setup.exe');
        document.body.appendChild(link);
        link.click();
      });
    },
    downloadJar() {
      axios({
        url: 'http://api.veloxal.io/get-windows-jar',
        method: 'GET',
        responseType: 'blob',
        onDownloadProgress: (progressEvent) => {
          const percentage = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          this.downloadProgressMessage = `Downloading File: ${percentage}%`;
          if (percentage === 100) {
            this.downloadComplete = true;
            this.downloadProgressMessage = 'File Downloaded Successfully';
          }
        },
      }).then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'veloxal.jar');
        document.body.appendChild(link);
        link.click();
      });
    },
    downloadAlert() {
      return {
        'alert-success': this.downloadComplete === true,
        'alert-info': this.downloadComplete === false,
      };
    },
  },
};
</script>

<style scoped>

</style>
