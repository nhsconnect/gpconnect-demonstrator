'use strict';

angular.module('rippleDemonstrator')
  .factory('AdvancedSearch', function ($modal) {

    var isModalClosed = true;

    var openAdvancedSearch = function() {
      if (isModalClosed) {
        isModalClosed = false;

        var modalInstance = $modal.open({
          templateUrl: 'views/search/advanced-search-modal.html',
          size: 'lg',
          controller: 'AdvancedSearchController',
          resolve: {
            modal: function () {
              return {
                title: 'Advanced Search'
              };
            }
          }
        });
      }

      modalInstance.result.then(function() {
        isModalClosed = true;
      }, function() {
        isModalClosed = true;
      });
    };

    return {
      isModalClosed: isModalClosed,
      openAdvancedSearch: openAdvancedSearch
    }
  });
