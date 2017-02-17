angular.module('AppAdmin').factory('LeerEmpleadosFactory', function ($http, $q){
    return {
        leerTodo: leerTodo
    };

    function leerTodo(idRol) {
        var defered = $q.defer();
        var promise = defered.promise;

        $http.get('LeerEmpleadoServlet', {params: {idRol: idRol}})/*Hacer la acción para vendedores y clientes*/
                .success(function(data) {
                    defered.resolve(data);
                })
                .error(function(err) {
                    defered.reject(err);
                });

        return promise;
    }
});
