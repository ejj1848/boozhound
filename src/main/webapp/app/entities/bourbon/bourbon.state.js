(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bourbon', {
            parent: 'entity',
            url: '/bourbon',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Bourbons'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bourbon/bourbons.html',
                    controller: 'BourbonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('bourbon-detail', {
            parent: 'entity',
            url: '/bourbon/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Bourbon'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bourbon/bourbon-detail.html',
                    controller: 'BourbonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Bourbon', function($stateParams, Bourbon) {
                    return Bourbon.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bourbon',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bourbon-detail.edit', {
            parent: 'bourbon-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bourbon/bourbon-dialog.html',
                    controller: 'BourbonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bourbon', function(Bourbon) {
                            return Bourbon.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bourbon.new', {
            parent: 'bourbon',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bourbon/bourbon-dialog.html',
                    controller: 'BourbonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                bourbonName: null,
                                proof: null,
                                year: null,
                                bourbonRating: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bourbon', null, { reload: 'bourbon' });
                }, function() {
                    $state.go('bourbon');
                });
            }]
        })
        .state('bourbon.edit', {
            parent: 'bourbon',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bourbon/bourbon-dialog.html',
                    controller: 'BourbonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bourbon', function(Bourbon) {
                            return Bourbon.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bourbon', null, { reload: 'bourbon' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bourbon.delete', {
            parent: 'bourbon',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bourbon/bourbon-delete-dialog.html',
                    controller: 'BourbonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bourbon', function(Bourbon) {
                            return Bourbon.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bourbon', null, { reload: 'bourbon' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
