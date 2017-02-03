'use strict';

angular.module('gpConnect')
  .controller('ReferralsListCtrl', function ($scope, $location, $stateParams, $sce, $modal, $state, usSpinnerService, PatientService, Referral) {

    $scope.currentPage = 1;
    $scope.toDateValue = moment().format('YYYY-MM-DD');
    $scope.fromDateValue = moment().subtract(3, 'years').format('YYYY-MM-DD');

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
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

    $scope.search = function (row) {
      return (
        angular.lowercase(row.dateOfReferral).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.referralFrom).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.referralTo).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }
    var loadHTML = function () {
    Referral.findAllHTMLTables($stateParams.patientId, $scope.fromDateValue, $scope.toDateValue).then(function (result) {

      // Default Page Content
      var text = '{"provider":"No Data","html":"No referrals data available for this patient."}';
      $scope.referralTable = JSON.parse(text);
      $scope.referralTable.html = $sce.trustAsHtml($scope.referralTable.html);
      
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
                $scope.referralTable.html = $sce.trustAsHtml(value.resource.section[0].text.div);
                $scope.referralTable.provider = value.resource.section[0].code.text;
            }
        }
      });
      
      usSpinnerService.stop('patientSummary-spinner');
    });
    };

    $scope.go = function (id) {
      $state.go('referrals-detail', {
        patientId: $scope.patient.id,
        referralId: id,
        filter: $scope.query,
        page: $scope.currentPage
      });
    };

    $scope.selected = function (referralId) {
      return referralId === $stateParams.referralId;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/referrals/referrals-modal.html',
        size: 'lg',
        controller: 'ReferralsModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Referral'
            };
          },
          referral: function () {
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (referral) {
        referral.dateOfReferral = new Date(referral.dateOfReferral);
        referral.dateOfReferral.setMinutes(referral.dateOfReferral.getMinutes() - referral.dateOfReferral.getTimezoneOffset());

        var toAdd = {
          author: referral.author,
          clinicalSummary: referral.clinicalSummary,
          dateCreated: new Date(referral.dateCreated),
          dateOfReferral: referral.dateOfReferral,
          reason: referral.reason,
          referralFrom: referral.referralFrom,
          referralTo: referral.referralTo
        };

        Referral.create($scope.patient.id, toAdd).then(function () {
          setTimeout(function () {
            $state.go('referrals', {
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
 loadHTML();
  });
