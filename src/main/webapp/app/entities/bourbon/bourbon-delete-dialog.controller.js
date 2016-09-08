(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .controller('BourbonDeleteController',BourbonDeleteController);

    BourbonDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bourbon'];

    function BourbonDeleteController($uibModalInstance, entity, Bourbon) {
        var vm = this;

        vm.bourbon = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bourbon.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
