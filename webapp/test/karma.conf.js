// Karma configuration
// http://karma-runner.github.io/0.12/config/configuration-file.html
// Generated on 2014-07-25 using
// generator-karma 0.8.3

module.exports = function(config) {
  'use strict';

  config.set({
    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,

    // base path, that will be used to resolve files and exclude
    basePath: '../',

    // testing framework to use (jasmine/mocha/qunit/...)
    frameworks: ['jasmine'],

    // list of files / patterns to load in the browser
    files: [
      'bower_components/angular/angular.js',
      'bower_components/angular-mocks/angular-mocks.js',
      'bower_components/angular-resource/angular-resource.js',
      'bower_components/angular-touch/angular-touch.js',
      'bower_components/angular-animate/angular-animate.js',
      'bower_components/angular-animate/angular-animate.js',
      'bower_components/angular-ui-router/src/*.js',
      'bower_components/angular-ui-bootstrap-bower/*.js',
      'bower_components/angular-loading-bar/src/*',
      'bower_components/angular-growl-notifications/dist/angular-growl-notifications.js',
      'bower_components/angular-utils-pagination/dirPagination.js',
      'bower_components/angular-utils-pagination/dirPagination.js',
      'bower_components/jquery/dist/jquery.js',
      'bower_components/jquery-timepicker-jt/jquery.timepicker.js',
      'bower_components/angular-jquery-timepicker/src/timepickerdirective.js',
      'bower_components/angular-ui-calendar/src/calendar.js',
      //'bower_components/jquery/src/jquery.js',
      //'bower_components/**/*.js',
      //'bower_components/**/**/*.js',
      'app/scripts/**/*.js',
      'app/scripts/app.js',
      'test/spec/**/*.js'
    ],

    // list of files / patterns to exclude
    exclude: [
       'bower_components/**/gruntFile.js',
       'bower_components/**/Gruntfile.js',
       'bower_components/jquery/src/**/*',
       'bower_components/angular-touch/index.js'
    ],

    // web server port
    port: 8080,

    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: [
      'Chrome'
    ],

    // Which plugins to enable
    plugins: [
      'karma-phantomjs-launcher',
      'karma-chrome-launcher',
      'karma-jasmine'
    ],

    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false,

    colors: true,

    // level of logging
    // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
    logLevel: config.LOG_INFO,

    // Uncomment the following lines if you are using grunt's server to run the tests
    // proxies: {
    //   '/': 'http://localhost:9000/'
    // },
    // URL root prevent conflicts with the site root
    // urlRoot: '_karma_'
  });
};
