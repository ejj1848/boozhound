(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .factory('DistillerSearch', DistillerSearch);

    DistillerSearch.$inject = ['$resource'];

    function DistillerSearch($resource) {
        var resourceUrl =  'api/_search/distillers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
