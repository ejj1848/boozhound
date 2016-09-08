(function() {
    'use strict';
    angular
        .module('boozhoundApp')
        .factory('CheckIn', CheckIn);

    CheckIn.$inject = ['$resource'];

    function CheckIn ($resource) {
        var resourceUrl =  'api/check-ins/:id';

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
