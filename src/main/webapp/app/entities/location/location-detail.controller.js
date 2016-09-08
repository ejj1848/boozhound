(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .controller('LocationDetailController', LocationDetailController);

    LocationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Location', 'Distiller', 'CheckIn'];

    function LocationDetailController($scope, $rootScope, $stateParams, previousState, entity, Location, Distiller, CheckIn) {
        var vm = this;

        vm.location = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('boozhoundApp:locationUpdate', function(event, result) {
            vm.location = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
