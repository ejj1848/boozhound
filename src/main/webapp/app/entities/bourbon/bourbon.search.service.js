(function() {
    'use strict';

    angular
        .module('boozhoundApp')
        .factory('BourbonSearch', BourbonSearch);

    BourbonSearch.$inject = ['$resource'];

    function BourbonSearch($resource) {
        var resourceUrl =  'api/_search/bourbons/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
