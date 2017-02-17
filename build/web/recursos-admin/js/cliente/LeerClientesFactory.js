angular.module('AppAdmin').factory('LeerClientesFactory', function ($http, $q){
    return {
        leerTodo: leerTodo
    };

    function leerTodo() {
        var defered = $q.defer();
        var promise = defered.promise;

        $http.get('LeerClienteServlet')
                .success(function(data) {
                    defered.resolve(data);
                })
                .error(function(err) {
                    defered.reject(err);
                });

        return promise;
    }
});
