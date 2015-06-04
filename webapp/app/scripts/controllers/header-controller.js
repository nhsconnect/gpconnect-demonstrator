'use strict';

angular.module('openehrPocApp')
  .controller('headerController', function ($scope, $rootScope, $state, $stateParams,PatientService) {

    var role = $stateParams.role;
    var email = $stateParams.email;

    // Set current user
    PatientService.setCurrentUser(role,email);

    // Get current user
    $scope.currentUser = PatientService.getCurrentUser();
    console.log($scope.currentUser);

    // Temporary default user
    if(!$scope.currentUser.role){
      $scope.currentUser.role = 'idcr';
      $scope.currentUser.email = 'example@email.com';
    }

    // Direct different roles to different pages at login
    switch($scope.currentUser.role) {
        case 'idcr':
          $state.go('patients-charts');
        break;
      case 'phr':
        $state.go('patients-summary', { patientId: 10 });  //id is hard coded
        break;
      default:
        $state.go('patients-summary', { patientId: 10 });  //id is hard coded
    }

    $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {

      var previousState = '';
      var pageHeader = '';
      var previousPage = '';

      var mainWidth = 0;
      var detailWidth = 0;

      switch(toState.name) {
        case 'patients-list':
          previousState = 'patients-charts';
          pageHeader = 'Patient Lists';
          previousPage = 'Patient Dashboard';
          mainWidth = 12;
          detailWidth = 0;
          break;
        case 'patients-charts':
          previousState = '';
          pageHeader = 'Patient Dashboard';
          previousPage = '';
          mainWidth = 12;
          detailWidth = 0;
          break;
        case 'patients-summary':
          previousState = 'patients-list';
          pageHeader = 'Patients Summary';
          previousPage = 'Patient Lists';
          mainWidth = 12;
          detailWidth = 0;
          break;
        case 'patients-lookup':
          previousState = '';
          pageHeader = '';
          previousPage = '';
          mainWidth = 6;
          detailWidth = 6;
          break;
        default:
          previousState = 'patients-list';
          pageHeader = 'Patients Details';
          previousPage = 'Patient Lists';
          mainWidth = 6;
          detailWidth = 6;
          break;
        }

      $scope.pageHeader = pageHeader;
      $scope.previousState = previousState;
      $scope.previousPage = previousPage;

      // Set bootstrap column sizes
      $scope.mainWidth = mainWidth;
      $scope.detailWidth = detailWidth;

      $scope.goBack = function() {
        $state.go(previousState);
      };

      if('user-context' in $state.current.views){
        $scope.userContextViewExists = true;
      }else{
        $scope.userContextViewExists = false;
      }

      if('actions' in $state.current.views){
        $scope.actionsExists = true;
      }else{
        $scope.actionsExists = false;
      }

      $scope.go = function (patient) {
        $state.go('patients-summary', { patientId: patient.id });
      };

      // Set title depending on user
      if($scope.currentUser.role === 'idcr'){
        $scope.title = 'IDCR POC';
      }
      if($scope.currentUser.role === 'phr'){
        $scope.title = 'PHR';
      }

      //Set home url depending on user
      $scope.goHome = function (){
        if($scope.currentUser.role === 'idcr'){
          $state.go('patients-charts');
        }
        if($scope.currentUser.role === 'phr'){
          $state.go('patients-summary', { patientId: 10 }); // Id is hardcoded
        }
      };

    });

  });


