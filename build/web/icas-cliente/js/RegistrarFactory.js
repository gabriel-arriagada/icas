(function() {
    'use strict';
    angular.module('LogInApp').factory('RegistrarFactory', function($http, $q) {
        return {
            setUsuario: setUsuario            
        };

        function setUsuario(usuario) {
            var defered = $q.defer();
            var promise = defered.promise;
            $http.post('../AutoRegistroClienteServlet', usuario)
                    .success(function(data) {
                        defered.resolve(data);
                    })
                    .error(function(error) {
                        defered.reject(error)
                    });

            return promise;
        }
     
    });
})();

