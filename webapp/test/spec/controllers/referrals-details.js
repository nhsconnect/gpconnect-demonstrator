'use strict';

describe('ReferralsDetailCtrl', function () {

  // load the controller's module
  beforeEach(module('rippleDemonstrator'));

  var ReferralsDetailCtrl, scope, $location, $stateParams, $state, PatientService, Contact, $modal;
  var fakeModal = {
        result: {
        then: function(confirmCallback, cancelCallback) {
            this.confirmCallBack = confirmCallback;
            this.cancelCallback = cancelCallback;
        }
    },
    close: function( item ) {
        this.result.confirmCallBack( item );
    },
    dismiss: function( type ) {
        this.result.cancelCallback( type );
    }
    };

  beforeEach(inject(function ($rootScope, $controller, _$location_, _$stateParams_, _$modal_, _$state_, _PatientService_, _Contact_) {
    scope = $rootScope.$new();
    $location = _$location_;
    $stateParams = _$stateParams_;
    $state = _$state_;
    PatientService = _PatientService_;
    Contact = _Contact_;
    $modal = _$modal_;

    ReferralsDetailCtrl = $controller('ReferralsDetailCtrl', {
      '$scope': scope,
    });
  }));

  describe('Initial state', function () {
    it('should instantiate the controller properly', function () {
      expect(ReferralsDetailCtrl).not.toBeUndefined();
    });

    it('should close the modal with result "true" when accepted', function () {
      spyOn($modal, 'open');
      $modal.open.andCallFake(function () {
        return fakeModal;
      });

      scope.edit();
      expect($modal.open).toHaveBeenCalled();
    });
  });

});
