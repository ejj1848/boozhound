(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .controller('CheckInDeleteController',CheckInDeleteController);

    CheckInDeleteController.$inject = ['$uibModalInstance', 'entity', 'CheckIn'];

    function CheckInDeleteController($uibModalInstance, entity, CheckIn) {
        var vm = this;

        vm.checkIn = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CheckIn.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
