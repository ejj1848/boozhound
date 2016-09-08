(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('check-in', {
            parent: 'entity',
            url: '/check-in?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CheckIns'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/check-in/check-ins.html',
                    controller: 'CheckInController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('check-in-detail', {
            parent: 'entity',
            url: '/check-in/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CheckIn'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/check-in/check-in-detail.html',
                    controller: 'CheckInDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'CheckIn', function($stateParams, CheckIn) {
                    return CheckIn.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'check-in',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('check-in-detail.edit', {
            parent: 'check-in-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/check-in/check-in-dialog.html',
                    controller: 'CheckInDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CheckIn', function(CheckIn) {
                            return CheckIn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('check-in.new', {
            parent: 'check-in',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/check-in/check-in-dialog.html',
                    controller: 'CheckInDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                notes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('check-in', null, { reload: 'check-in' });
                }, function() {
                    $state.go('check-in');
                });
            }]
        })
        .state('check-in.edit', {
            parent: 'check-in',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/check-in/check-in-dialog.html',
                    controller: 'CheckInDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CheckIn', function(CheckIn) {
                            return CheckIn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('check-in', null, { reload: 'check-in' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('check-in.delete', {
            parent: 'check-in',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/check-in/check-in-delete-dialog.html',
                    controller: 'CheckInDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CheckIn', function(CheckIn) {
                            return CheckIn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('check-in', null, { reload: 'check-in' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
