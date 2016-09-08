(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .controller('DistillerDeleteController',DistillerDeleteController);

    DistillerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Distiller'];

    function DistillerDeleteController($uibModalInstance, entity, Distiller) {
        var vm = this;

        vm.distiller = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Distiller.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
