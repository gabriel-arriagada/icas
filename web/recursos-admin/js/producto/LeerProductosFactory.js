angular.module('AppAdmin').factory('LeerProductosFactory', function ($http, $q) {
    return {
        leerTodo: leerTodo,
        leerParaVendedor: leerParaVendedor
    };

    function leerTodo() {
        var defered = $q.defer();
        var promise = defered.promise;

        $http.get('LeerProductoServlet')
                .success(function (data) {
                    defered.resolve(data);
                })
                .error(function (err) {
                    defered.reject(err);
                });

        return promise;
    }

    function leerParaVendedor() {
        var defered = $q.defer();
        var promise = defered.promise;

        $http.get('LeerProductoVendedorServlet')
                .success(function (data) {
                    defered.resolve(data);
                })
                .error(function (err) {
                    defered.reject(err);
                });

        return promise;
    }
});