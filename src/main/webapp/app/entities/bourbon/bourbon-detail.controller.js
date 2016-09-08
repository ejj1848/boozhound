(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .controller('BourbonDetailController', BourbonDetailController);

    BourbonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bourbon', 'Distiller', 'CheckIn'];

    function BourbonDetailController($scope, $rootScope, $stateParams, previousState, entity, Bourbon, Distiller, CheckIn) {
        var vm = this;

        vm.bourbon = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('boozhoundApp:bourbonUpdate', function(event, result) {
            vm.bourbon = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
