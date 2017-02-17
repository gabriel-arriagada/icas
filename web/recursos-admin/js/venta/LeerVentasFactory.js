(function () {
    'use strict';
    angular.module('AppAdmin').factory('LeerVentasFactory', function ($http, $q) {
        return {
            leerTodo: leerTodo
        };

        function leerTodo() {
            var defered = $q.defer();
            var promise = defered.promise;

            $http.get('LeerVentaServlet')
                    .success(function (data) {
                        defered.resolve(data);
                    })
                    .error(function (err) {
                        defered.reject(err);
                    });

            return promise;
        }
    });
})();
