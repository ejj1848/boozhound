(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .controller('CheckInDetailController', CheckInDetailController);

    CheckInDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CheckIn', 'Location', 'Distiller', 'Bourbon', 'Person'];

    function CheckInDetailController($scope, $rootScope, $stateParams, previousState, entity, CheckIn, Location, Distiller, Bourbon, Person) {
        var vm = this;

        vm.checkIn = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('boozhoundApp:checkInUpdate', function(event, result) {
            vm.checkIn = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
