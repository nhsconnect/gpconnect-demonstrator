'use strict';

angular.module('gpConnect')
  .controller('ProblemListCtrl', function ($scope, $state, $stateParams, $location, $sce, $modal, usSpinnerService, PatientService, Problem) {

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.problem).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.dateOfOnset).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient.data;
    });

    Problem.findAllHTMLTables($stateParams.patientId).then(function (result) {
      
      // Default Page Content
      var text = '{"provider":"No Data","html":"No problems data available for this patient."}';
      $scope.problemTable = JSON.parse(text);
      $scope.problemTable.html = $sce.trustAsHtml($scope.problemTable.html);
      
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
                $scope.problemTable.html = $sce.trustAsHtml(value.resource.section[0].text.div);
                $scope.problemTable.provider = value.resource.section[0].code.text;
            }
        }
      });
      
      usSpinnerService.stop('problemSummary-spinner');
    });

    $scope.go = function (id, problemSource) {
      $state.go('problem-detail', {
        patientId: $scope.patient.id,
        problemIndex: id,
        filter: $scope.query,
        page: $scope.currentPage,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType,
        source: problemSource
      });
    };

    $scope.selected = function (problemIndex) {
      return problemIndex === $stateParams.problemIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/problem/problem-modal.html',
        size: 'lg',
        controller: 'ProblemModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Problem'
            };
          },
          problem: function () {
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (problem) {
        problem.dateOfOnset = new Date(problem.dateOfOnset);

        var toAdd = {
          code: problem.code,
          dateOfOnset: problem.dateOfOnset,
          description: problem.description,
          problem: problem.problem,
          terminology: problem.terminology
        };

        Problem.create($scope.patient.id, toAdd).then(function () {
          setTimeout(function () {
            $state.go('problem-list', {
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
