'use strict';

describe('OrdersDetailCtrl', function () {

  // load the controller's module
  beforeEach(module('openehrPocApp'));

  var OrdersDetailCtrl, scope, $modal;
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

  beforeEach(inject(function ($rootScope, $controller, _$modal_) {
    scope = $rootScope.$new();
    $modal = _$modal_;

    OrdersDetailCtrl = $controller('OrdersDetailCtrl', {
      '$scope': scope,
    });
  }));

  describe('Initial state', function () {
    it('should instantiate the controller properly', function () {
      expect(OrdersDetailCtrl).not.toBeUndefined();
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
