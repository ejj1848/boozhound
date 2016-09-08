(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .controller('BourbonDialogController', BourbonDialogController);

    BourbonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bourbon', 'Distiller', 'CheckIn'];

    function BourbonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bourbon, Distiller, CheckIn) {
        var vm = this;

        vm.bourbon = entity;
        vm.clear = clear;
        vm.save = save;
        vm.distillers = Distiller.query();
        vm.checkins = CheckIn.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bourbon.id !== null) {
                Bourbon.update(vm.bourbon, onSaveSuccess, onSaveError);
            } else {
                Bourbon.save(vm.bourbon, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('boozhoundApp:bourbonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
