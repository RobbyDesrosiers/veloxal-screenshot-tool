// import axios from 'axios';

// eslint-disable-next-line import/prefer-default-export
export const downloads = {
  downloadInstaller() {
    // axios({
    //   url: 'http://localhost:5000/get-windows-installer',
    //   method: 'GET',
    //   responseType: 'blob',
    // }).then((response) => {
    //   const url = window.URL.createObjectURL(new Blob([response.data]));
    //   const link = document.createElement('a');
    //   link.href = url;
    //   link.setAttribute('download', 'veloxal-setup.exe');
    //   document.body.appendChild(link);
    //   link.click();
    // });
  },
  downloadJar() {
  //   axios({
  //     url: 'http://localhost:5000/get-windows-jar',
  //     method: 'GET',
  //     responseType: 'blob',
  //   }).then((response) => {
  //     const url = window.URL.createObjectURL(new Blob([response.data]));
  //     const link = document.createElement('a');
  //     link.href = url;
  //     link.setAttribute('download', 'veloxal.jar');
  //     document.body.appendChild(link);
  //     link.click();
  //   });
  },
};
