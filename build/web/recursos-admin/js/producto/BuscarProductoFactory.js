angular.module('AppAdmin').factory('BuscarProductoFactory', function ($http, $q){
    return {
        buscarProducto : buscarProducto
    };
    
    function buscarProducto(idProducto, cantidad){
        var defered = $q.defer();
        var promise = defered.promise;

        $http.get('BuscarProductoServlet', {params: {idProducto: idProducto, cantidad: cantidad}})
                .then(
                function(data) {
                    defered.resolve(data);
                }
                ,function(err) {                        
                    defered.reject(err);
                });

        return promise;
    }
});
