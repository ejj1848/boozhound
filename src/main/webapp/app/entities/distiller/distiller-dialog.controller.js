(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .controller('DistillerDialogController', DistillerDialogController);

    DistillerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Distiller', 'CheckIn'];

    function DistillerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Distiller, CheckIn) {
        var vm = this;

        vm.distiller = entity;
        vm.clear = clear;
        vm.save = save;
        vm.checkins = CheckIn.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.distiller.id !== null) {
                Distiller.update(vm.distiller, onSaveSuccess, onSaveError);
            } else {
                Distiller.save(vm.distiller, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('boozhoundApp:distillerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
