'use strict';

angular.module('rippleDemonstrator')
  .controller('AllergiesModalCtrl', function ($scope, $modalInstance, allergy, UserService, patient, modal) {

    $scope.currentUser = UserService.getCurrentUser();
    $scope.allergy = allergy;
    $scope.patient = patient;
    $scope.modal = modal;

    if (modal.title === 'Create Allergy') {
      $scope.allergy.dateCreated = new Date().toISOString().slice(0, 10);
      $scope.allergy.causeCode = '1239085';
      $scope.allergy.terminologyCode = '12393890';
    } else {
      // $scope.allergy.dateSubmitted = new Date().toISOString().slice(0, 10);
      $scope.allergy.dateCreated = new Date($scope.allergy.dateCreated).toISOString().slice(0, 10);
    }

    $scope.ok = function (allergyForm, allergy) {
      $scope.formSubmitted = true;

      if (allergyForm.$valid) {
        $modalInstance.close(allergy);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.openDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

  });
