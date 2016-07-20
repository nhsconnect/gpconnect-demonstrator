'use strict';

angular.module('gpConnect')
  .controller('AdminItemsListCtrl', function ($scope, $location, $stateParams, $modal, $state, $sce, PatientService, usSpinnerService, AdminItem) {

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    AdminItem.findAllHTMLTables($stateParams.patientId).then(function (result) {

      // Default Page Content
      var text = '{"provider":"No Data","html":"No administrative items data available for this patient."}';
      $scope.adminItemTable = JSON.parse(text);
      $scope.adminItemTable.html = $sce.trustAsHtml($scope.adminItemTable.html);
      
      // Process Returned Data
      var fhirJSON = result.data;
      var entryObj = fhirJSON.entry;
      $.each(entryObj, function(key,value) {
        if (value.resource.resourceType == "Patient") { // Find Patient Entry
            //alert(value.resource.identifier[0].value);
        }
        if (value.resource.resourceType == "Composition" && value.resource.type.coding[0].code == "425173008") { // Find Care Record Entry
            // Check if the requested section exists, if it does not in the back end it will not be passed to the front end
            if (value.resource.section != undefined){
                // We are only going to ever request one setion within the care record entry, this will be the section for displaying on the page 
                // so we can assume the first section is the one we want to display.
                $scope.adminItemTable.html = $sce.trustAsHtml(value.resource.section[0].text.div);
                $scope.adminItemTable.provider = value.resource.section[0].code.text;
            }
        }
      });

      usSpinnerService.stop('patientSummary-spinner');
    });
  });
