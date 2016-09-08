(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .controller('CheckInDialogController', CheckInDialogController);

    CheckInDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'CheckIn', 'Location', 'Distiller', 'Bourbon', 'Person'];

    function CheckInDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, CheckIn, Location, Distiller, Bourbon, Person) {
        var vm = this;

        vm.checkIn = entity;
        vm.clear = clear;
        vm.save = save;
        vm.locations = Location.query({filter: 'checkin-is-null'});
        $q.all([vm.checkIn.$promise, vm.locations.$promise]).then(function() {
            if (!vm.checkIn.location || !vm.checkIn.location.id) {
                return $q.reject();
            }
            return Location.get({id : vm.checkIn.location.id}).$promise;
        }).then(function(location) {
            vm.locations.push(location);
        });
        vm.distillers = Distiller.query({filter: 'checkin-is-null'});
        $q.all([vm.checkIn.$promise, vm.distillers.$promise]).then(function() {
            if (!vm.checkIn.distiller || !vm.checkIn.distiller.id) {
                return $q.reject();
            }
            return Distiller.get({id : vm.checkIn.distiller.id}).$promise;
        }).then(function(distiller) {
            vm.distillers.push(distiller);
        });
        vm.bourbons = Bourbon.query({filter: 'checkin-is-null'});
        $q.all([vm.checkIn.$promise, vm.bourbons.$promise]).then(function() {
            if (!vm.checkIn.bourbon || !vm.checkIn.bourbon.id) {
                return $q.reject();
            }
            return Bourbon.get({id : vm.checkIn.bourbon.id}).$promise;
        }).then(function(bourbon) {
            vm.bourbons.push(bourbon);
        });
        vm.people = Person.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.checkIn.id !== null) {
                CheckIn.update(vm.checkIn, onSaveSuccess, onSaveError);
            } else {
                CheckIn.save(vm.checkIn, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('boozhoundApp:checkInUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
