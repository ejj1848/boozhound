(function() {
    'use strict';
    angular
        .module('boozhoundApp')
        .factory('Distiller', Distiller);

    Distiller.$inject = ['$resource'];

    function Distiller ($resource) {
        var resourceUrl =  'api/distillers/:id';

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
