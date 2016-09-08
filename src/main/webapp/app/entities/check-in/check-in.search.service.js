(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .factory('CheckInSearch', CheckInSearch);

    CheckInSearch.$inject = ['$resource'];

    function CheckInSearch($resource) {
        var resourceUrl =  'api/_search/check-ins/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
