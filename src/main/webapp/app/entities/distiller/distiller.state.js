(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('distiller', {
            parent: 'entity',
            url: '/distiller',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Distillers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/distiller/distillers.html',
                    controller: 'DistillerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('distiller-detail', {
            parent: 'entity',
            url: '/distiller/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Distiller'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/distiller/distiller-detail.html',
                    controller: 'DistillerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Distiller', function($stateParams, Distiller) {
                    return Distiller.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'distiller',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('distiller-detail.edit', {
            parent: 'distiller-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/distiller/distiller-dialog.html',
                    controller: 'DistillerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Distiller', function(Distiller) {
                            return Distiller.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('distiller.new', {
            parent: 'distiller',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/distiller/distiller-dialog.html',
                    controller: 'DistillerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                distillerName: null,
                                distillerRating: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('distiller', null, { reload: 'distiller' });
                }, function() {
                    $state.go('distiller');
                });
            }]
        })
        .state('distiller.edit', {
            parent: 'distiller',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/distiller/distiller-dialog.html',
                    controller: 'DistillerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Distiller', function(Distiller) {
                            return Distiller.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('distiller', null, { reload: 'distiller' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('distiller.delete', {
            parent: 'distiller',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/distiller/distiller-delete-dialog.html',
                    controller: 'DistillerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Distiller', function(Distiller) {
                            return Distiller.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('distiller', null, { reload: 'distiller' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
