'use strict';

angular.module('gpConnect')
  .controller('ObservationsListCtrl', function ($scope, $location, $stateParams, $sce, $modal, usSpinnerService, $state, PatientService, Observation) {

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.testName).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.sampleTaken).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.dateCreated).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient.data;
    });

    Observation.findAllHTMLTables($stateParams.patientId).then(function (result) {

      // Default Page Content
      var text = '{"provider":"No Data","html":"No observation data available for this patient."}';
      $scope.observationTable = JSON.parse(text);
      $scope.observationTable.html = $sce.trustAsHtml($scope.observationTable.html);
      
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
                $scope.observationTable.html = $sce.trustAsHtml(value.resource.section[0].text.div);
                $scope.observationTable.provider = value.resource.section[0].code.text;
            }
        }
      });
      
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (sourceId, source) {
      $state.go('observations-detail', {
        patientId: $scope.patient.id,
        resultIndex: sourceId,
        source: source,
        filter: $scope.query,
        page: $scope.currentPage
      });
    };

    $scope.selected = function (resultIndex) {
      return resultIndex === $stateParams.resultIndex;
    };

  });
