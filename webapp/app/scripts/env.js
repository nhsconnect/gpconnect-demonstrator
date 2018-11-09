(function (window) {
  window.__env = window.__env || {};

  // Base url
  window.__env.baseUrl = '/gpconnect-demonstrator/v1/';

  // for something behind a reverse proxy this is the base *before* the proxy has remapped it ie the externally visible url
  //window.__env.baseUrl = '/v1/';

  //Modal url for structured 
  window.__env.templateUrl = '../v1/views/access-record/modal.html';
}(this));
