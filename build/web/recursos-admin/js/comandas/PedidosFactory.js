(function () {
    'use strict';
    angular.module('ComandasApp')
            .factory('PedidosFactory', function ($q, $http) {

                return {
                    getPedidosDelDia: getPedidosDelDia,
                    cambiarEstadoDePedido: cambiarEstadoDePedido,
                    getPedidoPorId: getPedidoPorId,
                    leerInformacionVenta: leerInformacionVenta,
                    entregarPedido: entregarPedido
                };

                function getPedidosDelDia() {
                    var defered = $q.defer();
                    var promise = defered.promise;
                    $http.get('LeerPedidosDelDiaServlet')
                            .success(function (data) {
                                defered.resolve(data);
                            })
                            .error(function (err) {
                                defered.reject(err);
                            });

                    return promise;
                }

                function cambiarEstadoDePedido(item, idEstado) {
                    var defered = $q.defer();
                    var promise = defered.promise;
                    var nuevoEstado = {idPedido: item.idPedido, idEstado: idEstado};
                    $http.post('EditarEstadoPedidoServlet', nuevoEstado)
                            .success(function (data) {
                                defered.resolve(data);
                            })
                            .error(function (err) {
                                defered.reject(err);
                            });

                    return promise;
                }

                function getPedidoPorId(idPedido) {
                    var defered = $q.defer();
                    var promise = defered.promise;
                    $http.get('LeerPedidosPorIdServlet', {params: {idPedido: idPedido}})
                            .success(function (data) {
                                defered.resolve(data);
                            })
                            .error(function (err) {
                                defered.reject(err);
                            });

                    return promise;
                }

                function leerInformacionVenta(idPedido) {
                    var defered = $q.defer();
                    var promise = defered.promise;
                    $http.get('BuscarInformacionVentaServlet', {params: {idPedido: idPedido}})
                            .success(function (data) {
                                defered.resolve(data);
                            })
                            .error(function (err) {
                                defered.reject(err);
                            });

                    return promise;
                }
                
                function entregarPedido(idPedido, idEstado, idVenta){
                    var defered = $q.defer();
                    var promise = defered.promise;                    
                    $http.post('EntregarPedidoVentaServlet', {idPedido: idPedido, idEstado: idEstado, idVenta: idVenta})
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