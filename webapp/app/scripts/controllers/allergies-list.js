'use strict';

angular.module('gpConnect')
  .controller('AllergiesListCtrl', function ($scope, $location, $stateParams, $sce, $modal, $state, usSpinnerService, PatientService, Allergy) {

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.cause).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.reaction).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    Allergy.findAllHTMLTables($stateParams.patientId).then(function (result) {

      // Default Page Content
      var text = '{"provider":"No Data","html":"No allergies data available for this patient."}';
      $scope.allergyTable = JSON.parse(text);
      $scope.allergyTable.html = $sce.trustAsHtml($scope.allergyTable.html);
      
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
                $scope.allergyTable.html = $sce.trustAsHtml(value.resource.section[0].text.div);
                $scope.allergyTable.provider = value.resource.section[0].code.text;
            }
        }
      });

      usSpinnerService.stop('allergySummary-spinner');
    });

    $scope.go = function (id, source) {
      $state.go('allergies-detail', {
        patientId: $scope.patient.id,
        allergyIndex: id,
        filter: $scope.query,
        page: $scope.currentPage,
        source: source
      });
    };

    $scope.selected = function ($index) {
      return $index === $stateParams.allergyIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/allergies/allergies-modal.html',
        size: 'lg',
        controller: 'AllergiesModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Allergy'
            };
          },
          allergy: function () {
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (allergy) {
        var toAdd = {
          sourceId: '',
          cause: allergy.cause,
          causeCode: allergy.causeCode,
          causeTerminology: allergy.causeTerminology,
          reaction: allergy.reaction,
          source: allergy.source
        };

        Allergy.create($scope.patient.id, toAdd).then(function () {
          setTimeout(function () {
            $state.go('allergies', {
              patientId: $scope.patient.id,
              filter: $scope.query,
              page: $scope.currentPage
            }, {
              reload: true
            });
          }, 2000);
        });
      });
    };

  });
