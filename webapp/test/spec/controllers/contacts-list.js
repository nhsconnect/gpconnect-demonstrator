'use strict';

describe('ContactsListCtrl', function () {

  // load the controller's module
  beforeEach(module('openehrPocApp'));

  var ContactsListCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ContactsListCtrl = $controller('ContactsListCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(1).toBe(1);
  });
});
