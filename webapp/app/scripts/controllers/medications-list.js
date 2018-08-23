'use strict';

angular.module('gpConnect')
.controller('MedicationsListCtrl', function ($scope, $location, $stateParams, $modal, $state, $sce, usSpinnerService, PatientService, Medication) {
  
  $scope.query = {};
  $scope.queryBy = '$';
  
  $scope.currentPage = 1;
  $scope.toDateValue = moment().format('YYYY-MM-DD');
  $scope.fromDateValue = moment().subtract(3, 'years').format('YYYY-MM-DD');
  
  $scope.pageChangeHandler = function (newPage) {
    $scope.currentPage = newPage;
  };
  
  if ($stateParams.page) {
    $scope.currentPage = $stateParams.page;
  }
  
  if ($stateParams.filter) {
    $scope.query.$ = $stateParams.filter;
  }
  
  $scope.openDatePicker = function ($event, name) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.toDate = false;
    $scope.fromDate = false;
    $scope[name] = true;
  };
  
  $scope.dateChanged = function() {
    var newDate = new Date($scope.toDateValue);
    $scope.toDateValue = moment(newDate).format('YYYY-MM-DD');
    newDate = new Date($scope.fromDateValue);
    $scope.fromDateValue = moment(newDate).format('YYYY-MM-DD');
    loadHTML();
  };
  
  var loadHTML = function () {
    Medication.findAllHTMLTables($stateParams.patientId, $scope.fromDateValue, $scope.toDateValue).then(function (result) {
      console.log($scope.fromDateValue);
      console.log($scope.toDateValue);
      // Default Page Content
      var text = '{"provider":"No Data","html":"No medication data available for this patient."}';
      $scope.medicationTable = JSON.parse(text);
      $scope.medicationTable.html = $sce.trustAsHtml($scope.medicationTable.html);
      
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
            $scope.medicationTable.html = $sce.trustAsHtml(value.resource.section[0].text.div);
            $scope.medicationTable.provider = value.resource.section[0].code.text;
          }
        }
      });
      
      usSpinnerService.stop('medicationSummary-spinner');
    });
  }
  
  loadHTML();
  
  $scope.go = function (id, source) {
    $state.go('medications-detail', {
      patientId: $scope.patient.id,
      medicationIndex: id,
      filter: $scope.query.$,
      page: $scope.currentPage,
      source: source
    });
  };
  
  $scope.selected = function (medicationIndex) {
    return medicationIndex === $stateParams.medicationIndex;
  };
  
  $scope.create = function () {
    var modalInstance = $modal.open({
      templateUrl: 'views/medications/medications-modal.html',
      size: 'lg',
      controller: 'MedicationsModalCtrl',
      resolve: {
        modal: function () {
          return {
            title: 'Create Medication'
          };
        },
        medication: function () {
          return {};
        },
        patient: function () {
          return $scope.patient;
        }
      }
    });
    
    modalInstance.result.then(function (medication) {
      medication.startDate = new Date(medication.startDate);
      medication.startTime = new Date(medication.startTime.valueOf() - medication.startTime.getTimezoneOffset() * 60000);
      
      var toAdd = {
        doseAmount: medication.doseAmount,
        doseDirections: medication.doseDirections,
        doseTiming: medication.doseTiming,
        medicationCode: medication.medicationCode,
        medicationTerminology: medication.medicationTerminology,
        name: medication.name,
        route: medication.route,
        startDate: medication.startDate,
        startTime: medication.startTime,
        author: medication.author,
        dateCreated: medication.dateCreated
      };
      
      Medication.create($scope.patient.id, toAdd).then(function () {
        setTimeout(function () {
          $state.go('medications', {
            patientId: $scope.patient.id,
            filter: $scope.query.$,
            page: $scope.currentPage
          }, {
            reload: true
          });
        }, 2000);
      });
    });
  };
  
});
