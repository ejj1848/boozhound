(function() {
    'use strict';
    angular
        .module('boozhoundApp')
        .factory('Bourbon', Bourbon);

    Bourbon.$inject = ['$resource'];

    function Bourbon ($resource) {
        var resourceUrl =  'api/bourbons/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
