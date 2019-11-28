(function (window) {
	window.__env = window.__env || {};
	
	//Base url
	window.__env.baseUrl = '/gpconnect-demonstrator/v0_7/';

	// for something behind a reverse proxy this is the base *before* the proxy has remapped it ie the externally visible url
	//window.__env.baseUrl = '/v0_7/';
} (this));
