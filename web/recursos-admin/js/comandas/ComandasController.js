(function () {
    'use strict';
    angular.module('ComandasApp')
            .controller('ComandasController', function (PedidosFactory, $websocket, $mdToast, $mdDialog) {
                var cc = this;
        
                var dataStream = $websocket('wss://i-cas.tk/icas/comandas');                
                cc.comandas = [];
                cc.detallePedido = [];
                cc.informacionVenta = {};
                cc.verPedido = false;
                var itemAnterior = null;
                cc.currentItem = null;

                dataStream.onOpen(function () {
                    console.log("WebSocket: Conexion establecida.");
                });

                dataStream.onClose(function (event){
                    console.log("WebSocket: Conexion cerrada.");
                    console.log(event);
                });
                
                dataStream.onError(function (event){
                    console.log("WebSocket: Error.");
                    console.log(event);
                });

                var audio = new Audio('recursos-admin/assets/audio/comanda.mp3');

                dataStream.onMessage(function (message) {
                    cc.comandas.push(JSON.parse(message.data));
                    audio.play();
                    mostrarToast("Has recibido un nuevo pedido.", 3000);
                });

                PedidosFactory.getPedidosDelDia()
                        .then(function (pedidos) {
                            cc.comandas = pedidos;
                        })
                        .catch(function () {

                        });

                cc.cargarPedido = function (item) {

                    if (itemAnterior != null && itemAnterior.estado != "Preparado") {
                        itemAnterior.estado = "Recibido";
                    }

                    if (item.estado != "Preparado") {
                        item.estado = "En preparación";
                    }

                    cc.currentItem = item;
                    cc.verPedido = true;
                    cc.buscandoPedido = true;
                    cc.numeroPedido = item.idPedido;

                    PedidosFactory.leerInformacionVenta(item.idPedido)
                            .then(function (infoVenta) {
                                cc.informacionVenta = infoVenta;
                                PedidosFactory.getPedidoPorId(item.idPedido)
                                        .then(function (pedido) {
                                            cc.buscandoPedido = false;                                            
                                            cc.comentario = pedido[0].comentario;
                                            cc.detallePedido = pedido[1];
                                        })
                                        .catch(function (error) {
                                            mostrarToast("Ha ocurrido un error al intentar recuperar los datos.", 3000);
                                        });
                            })
                            .catch(function (error) {
                                mostrarToast("Ha ocurrido un error al intentar recuperar los datos.", 3000);
                            });
                    itemAnterior = item;
                };


                cc.cambiarEstadoPedidoToPreparado = function () {
                    var idEstado = 3;/*Preparado*/
                    PedidosFactory.cambiarEstadoDePedido(cc.currentItem, idEstado)
                            .then(function (data) {
                                cc.currentItem.estado = "Preparado";
                                mostrarToast("El estado del pedido ha sido cambiado.", 3000);
                            })
                            .catch(function (error) {
                                console.log(error);
                                cc.currentItem.estado = "En preparación";
                                mostrarToast("Ha ocurrido un error al realizar la operación.", 3000);
                            });
                };



                cc.cambiarEstadoPedidoToEntregado = function () {
                    var idEstado = 4;/*Entregado*/
                    switch (cc.informacionVenta.formaPago) {
                        case "Pre pago":
                            PedidosFactory.cambiarEstadoDePedido(cc.currentItem, idEstado)
                                    .then(function (data) {
                                        cc.comandas.splice(cc.comandas.indexOf(cc.currentItem), 1);
                                        cc.verPedido = false;
                                        cc.currentItem.estado = "Entregado";
                                        mostrarToast("El estado del pedido ha sido cambiado.", 3000);
                                    })
                                    .catch(function (error) {
                                        console.log(error);
                                        cc.currentItem.estado = "Preparado";
                                        mostrarToast("Ha ocurrido un error al realizar la operación.", 3000);
                                    });
                            break;
                        default :
                            var datosEntrega = {formaPago: cc.informacionVenta.formaPago, total: cc.informacionVenta.total, idPedido: cc.currentItem.idPedido, idEstado: idEstado, idVenta: cc.informacionVenta.idVenta};
                            entregarPedido(datosEntrega);
                            break;
                    }
                };



                function entregarPedido(datosEntrega) {
                    $mdDialog.show({
                        controller: EjecutarEntrega,
                        controllerAs: "vm",
                        template: '<md-dialog aria-label="Agregar">' +
                                '<md-toolbar>' +
                                '<div class="md-toolbar-tools">' +
                                '<h2 ng-if="vm.efectivo">Pago en efectivo, el cliente debe pagar</h2>' +
                                '<h2 ng-if="!vm.efectivo">Pago JUNAEB, el cliente debe pagar</h2>' +
                                '<span flex></span>' +
                                '<md-button aria-label="b1" class="md-icon-button" ng-click="vm.cancelar()">' +
                                '<md-icon md-svg-icon="cerrar"></md-icon>' +
                                '</md-button>' +
                                '</div>' +
                                '</md-toolbar>' +
                                '<md-dialog-content>' +
                                '<div layout="column" layout-align="center center" ng-if="!vm.efectivo" class="md-dialog-content">' +
                                '<h4>Opere tarjeta</h4>' +
                                '<md-icon md-svg-icon="formaPago" style="width: 150px; height: 150px;"></md-icon>' +
                                '<h4>Total: {{vm.total|currency:$:0}}</h4>' +
                                '</div>' +
                                '<div layout="column" layout-align="center center" ng-if="vm.efectivo" class="md-dialog-content">' +
                                '<h4>Calculadora de vuelto</h4>' +
                                '<h4>Total: {{vm.total|currency:$:0}}</h4>' +
                                '<md-input-container>' +
                                '<label>Efectivo</label>' +
                                '<input style="font-size: 20px;" ng-model="vm.pagaCon" type="number">' +
                                '</md-input-container>' +
                                '<h4 ng-if="vm.pagaCon != null && vm.pagaCon >= vm.total">Vuelto: {{vm.pagaCon - vm.total|currency:$:0}}</h4>' +
                                '</div>' +
                                '</md-dialog-content>' +
                                '<md-dialog-actions layout="row">' +
                                '<md-button aria-label="b2" ng-click="vm.cancelar()">cancelar</md-button>' +
                                '<md-button aria-label="b2" ng-click="vm.ok()">ok</md-button>' +
                                '</md-dialog-actions>' +
                                '</md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        locals: {
                            datosEntrega: datosEntrega
                        }
                    }).then(function (exito) {
                        if (exito) {
                            cc.comandas.splice(cc.comandas.indexOf(cc.currentItem), 1);
                            cc.verPedido = false;
                            cc.currentItem.estado = "Entregado";
                            mostrarToast("La venta fue entregada con éxito.", 3000);
                        } else {
                            mostrarToast("Ha ocurrido un error al realizar la operación.", 3000);
                        }
                    });

                    function EjecutarEntrega(datosEntrega, PedidosFactory) {
                        var vm = this;
                        vm.efectivo = false;
                        if (datosEntrega.formaPago == "Efectivo") {
                            vm.efectivo = true;
                        }
                        vm.total = datosEntrega.total;

                        vm.cancelar = function () {
                            $mdDialog.cancel();
                        };

                        vm.ok = function () {
                            PedidosFactory.entregarPedido(datosEntrega.idPedido, datosEntrega.idEstado, datosEntrega.idVenta)
                                    .then(function () {
                                        vm.exito = true;
                                        $mdDialog.hide(vm.exito);
                                    })
                                    .catch(function () {
                                        vm.exito = false;
                                        $mdDialog.hide(vm.exito);
                                    });
                        };
                    }
                }


                cc.buscarPorRut = function (textoDeBusqueda) {
                    var resultado = textoDeBusqueda ? cc.comandas.filter(filtro(textoDeBusqueda)) : cc.comandas, deferred;
                    return resultado;
                };

                function filtro(busqueda) {
                    return function porRut(pedido) {
                        return (pedido.rut.indexOf(busqueda) === 0);
                    };
                }

                function mostrarToast(mensaje, delay) {
                    $mdToast.show(
                            $mdToast.simple()
                            .textContent(mensaje)
                            .position('top right')
                            .hideDelay(delay)
                            );
                }

                cc.volver = function () {
                    window.history.back();
                };
            });
})();
