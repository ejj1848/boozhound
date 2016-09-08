(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .controller('DistillerDetailController', DistillerDetailController);

    DistillerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Distiller', 'CheckIn'];

    function DistillerDetailController($scope, $rootScope, $stateParams, previousState, entity, Distiller, CheckIn) {
        var vm = this;

        vm.distiller = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('boozhoundApp:distillerUpdate', function(event, result) {
            vm.distiller = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
