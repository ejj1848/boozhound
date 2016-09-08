'use strict';

describe('Controller Tests', function() {

    describe('Bourbon Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBourbon, MockDistiller, MockCheckIn;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBourbon = jasmine.createSpy('MockBourbon');
            MockDistiller = jasmine.createSpy('MockDistiller');
            MockCheckIn = jasmine.createSpy('MockCheckIn');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Bourbon': MockBourbon,
                'Distiller': MockDistiller,
                'CheckIn': MockCheckIn
            };
            createController = function() {
                $injector.get('$controller')("BourbonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'boozhoundApp:bourbonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
