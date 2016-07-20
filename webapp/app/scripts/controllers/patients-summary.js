'use strict';

angular.module('gpConnect')
  .controller('PatientsSummaryCtrl', function ($scope, $stateParams, $state, $rootScope, $location, $sce, usSpinnerService, PatientService) {

    $scope.patients = $stateParams.patientsList;

    PatientService.getSummary($stateParams.patientId).then(function (patientSummaryResponse) {
      
      // Default Page Content
      var text = '{"provider":"No Data","html":"No patient summary available for this patient."}';
      $scope.patientSummary = JSON.parse(text);
      $scope.patientSummary.html = $sce.trustAsHtml($scope.patientSummary.html);
      
      // Process Returned Data
      var fhirJSON = patientSummaryResponse.data;
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
                $scope.patientSummary.html = $sce.trustAsHtml(value.resource.section[0].text.div);
                $scope.patientSummary.provider = value.resource.section[0].code.text;
            }
        }
      });
      
      usSpinnerService.stop('patientSummary-spinner');
      
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.goToSection = function (section) {
      var requestHeader = {
        patientId: $stateParams.patientId
      };
    };

  });
