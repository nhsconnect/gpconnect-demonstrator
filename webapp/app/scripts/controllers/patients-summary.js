'use strict';

angular.module('openehrPocApp')
  .controller('PatientsSummaryCtrl', function ($scope, $stateParams, PatientService) {

    // Side Bar - Toggle Panel
    $('.panel .toggle').click(function(){

      var thisPanel = $(this).closest('.panel')

      // Current State: Expanded -> Collapse
      if( $(thisPanel).hasClass('expanded') ){
        $(thisPanel).removeClass('expanded');
        $(thisPanel).addClass('collapsed');
        $(this).find('i').removeClass('fi-minus');
        $(this).find('i').addClass('fi-plus');
        $(thisPanel).find('.panel-body').hide();
      } else {
        $(thisPanel).removeClass('collapsed');
        $(thisPanel).addClass('expanded');
        $(this).find('i').removeClass('fi-plus');
        $(this).find('i').addClass('fi-minus');
        $(thisPanel).find('.panel-body').show();
      }

    });

  });
