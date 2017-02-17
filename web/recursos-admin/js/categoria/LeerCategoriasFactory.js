angular.module('AppAdmin').factory('LeerCategoriasFactory', function($http, $q) {
    return {
        leerTodo: leerTodo
    }

    function leerTodo() {
        var defered = $q.defer();
        var promise = defered.promise;

        $http.get('LeerCategoriaServlet')
                .success(function(data) {
                    defered.resolve(data);
                })
                .error(function(err) {
                    defered.reject(err)
                });

        return promise;
    }
});