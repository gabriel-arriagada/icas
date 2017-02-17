(function () {
    'use strict';
    angular.module('AppCatalogo', ['ngMaterial']).config(function ($mdThemingProvider, $mdIconProvider) {

        var cyanTextoBlanco = $mdThemingProvider.extendPalette('cyan', {
            'contrastDefaultColor': 'light'
        });
        $mdThemingProvider.definePalette('cyanTextoBlanco', cyanTextoBlanco);
        $mdThemingProvider.theme('default').primaryPalette('cyanTextoBlanco');
        $mdThemingProvider.theme('default').accentPalette('yellow');

        $mdIconProvider
                .icon("user", "recursos-cliente/privado/svg/usuario_24px.svg", 24)
                .icon("cuenta", "recursos-cliente/privado/svg/cuenta_24px.svg", 24)
                .icon("salir", "recursos-cliente/privado/svg/salir_24px.svg", 24)
                .icon("cerrar", "recursos-cliente/privado/svg/cerrar_24px.svg", 24)
                .icon("menu", "recursos-cliente/privado/svg/menu_24px.svg", 24)
                .icon("mas", "recursos-cliente/privado/svg/mas.svg", 24)
                .icon("cara_triste", "recursos-cliente/privado/svg/triste_24px.svg", 24)
                .icon("cara_feliz", "recursos-cliente/privado/svg/feliz_24px.svg", 24)
                .icon("desplegar", "recursos-cliente/privado/svg/drop_down_24px.svg", 24)
                .icon("buscar", "recursos-cliente/privado/svg/buscar_24px.svg", 24)
                .icon("cerrar_atras", "recursos-cliente/privado/svg/cerrar_24px.svg", 24);

    }).controller("ClienteController", function (ProductosFactory, $mdDialog, Carrito, UltimasCompras, PedidosPendientes, DatosUsuario) {

        new SideNav();
        var vm = this;

        var ws = new WebSocket("wss://i-cas.tk/icas/comandas");

        ws.onopen = function () {
            console.log("WebSocket: Conexión abierta");
        };

        ws.onclose = function (event) {
            console.log("WebSocket: Conexión cerrada");
            console.log(event);
        };

        ws.onerror = function (event) {
            console.log("WebSocket Error");
            console.log(event);
        };

        function sendComanda(idPedido, horaRetiro) {
            var comanda =
                    {
                        idPedido: idPedido,
                        nombre: DatosUsuario.getNombre(),
                        rut: DatosUsuario.getRut(),
                        hora: horaRetiro,
                        estado: 'Recibido'
                    };
            ws.send(JSON.stringify(comanda));
        }



        vm.menu = [
            {nombre: "Mis datos", url: "ClienteDatosServlet", icono: "user"},
            {nombre: "Saldo y compras", url: "ClienteComprasSaldoServlet", icono: "cuenta"}
        ];

        vm.iniciar = function (nombre, rut) {
            if (nombre != '' && rut != '') {
                DatosUsuario.setNombre(nombre);
                DatosUsuario.setRut(rut);
            }
        };

        vm.idCat = 0;
        vm.cargarProductos = function (idCategoria) {
            vm.productos = [];
            vm.idCat = idCategoria;
            document.querySelector(".spinner").style.display = "";
            document.getElementById("div__titulo").style.display = "none";
            ProductosFactory.leerTodo(idCategoria)
                    .then(function (data) {
                        vm.productos = data;
                        document.querySelector(".spinner").style.display = "none";
                        if (vm.idCat == 0 && vm.productos.length > 0) {
                            document.getElementById("div__titulo").style.display = "";
                        }
                    }).catch(function (error) {
                console.log(error);
            });
        };

        vm.verDialogo = function (producto) {
            producto = Carrito.buscarProducto(producto);
            $mdDialog.show({
                controller: AgregarProducto,
                controllerAs: "vm",
                template:
                        '<md-dialog aria-label="Agregar">' +
                        '<md-toolbar>' +
                        '<div class="md-toolbar-tools">' +
                        '<h3>¿Cuántos?</h2>' +
                        '<span flex></span>' +
                        '<md-button aria-label="b1" class="md-icon-button" ng-click="vm.cancelar()">' +
                        '<p class="material-icons">close</p>' +
                        '</md-button>' +
                        '</div>' +
                        '</md-toolbar>' +
                        '<md-dialog-content>' +
                        '<div class="md-dialog-content" style="margin-top:30px;">' +
                        '<md-slider id="_slider" md-discrete ng-model="vm.cantidad" min="0" max="{{vm.stock < 10 ? vm.stock : 10}}" step="1" aria-label="can" class="md-primary _md-active"></md-slider>' +
                        '</div>' +
                        '</md-dialog-content>' +
                        '<md-dialog-actions layout="row">' +
                        '<md-button aria-label="b2" ng-click="vm.ok()">ok</md-button>' +
                        '</md-dialog-actions>' +
                        '</md-dialog>',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                locals: {
                    producto: producto//Se envía producto a controlador AgregarProducto
                }
            }).then(function (r) {
                if (r.cantidad > 0) {
                    mostrarSnackBar('Se agregó ' + r.cantidad + ' ' + r.nombre + ' a la compra.');
                } else if (r.elimino) {
                    mostrarSnackBar('Se ha eliminado el producto de la compra.');
                }
                /*Abrir snackBar*/
                function mostrarSnackBar(mensaje) {
                    var snackbarContainer = document.querySelector('#demo-snackbar-example');
                    var data = {
                        message: mensaje,
                        timeout: 2000,
                        actionHandler: function () {
                        },
                        actionText: 'Listo'
                    };
                    if (screen.width < 479) {
                        document.querySelector(".mdl-button--floating-action").style.bottom = "80px";
                    }
                    snackbarContainer.MaterialSnackbar.showSnackbar(data);
                }

            }, function () {
            });
        };

        /*Controlador mdDialog para seleccionar cantidad*/
        function AgregarProducto($mdDialog, producto, Carrito) {
            var vm = this;
            if (producto.cantidad != null) {
                vm.cantidad = producto.cantidad;
            } else {
                vm.cantidad = 0;
            }
            vm.stock = producto.stock;
            vm.cancelar = function () {
                $mdDialog.cancel();
            };
            vm.ok = function () {
                producto.cantidad = vm.cantidad;
                var elimino = Carrito.agregarProducto(producto);
                $mdDialog.hide({nombre: producto.nombre, cantidad: vm.cantidad, elimino: elimino});
            };
        }
        ;

        vm.verCarrito = function (e) {
            if (Carrito.hayCarrito()) {
                window.location.href = "CarritoServlet";
            } else {
                //_md-transition-in(out)
                $mdDialog.show(
                        $mdDialog.alert()
                        .parent(angular.element(document.body))
                        .clickOutsideToClose(true)
                        .title('Mensaje')
                        .textContent('Aún no hay productos en el carrito')
                        .ariaLabel('Alert')
                        .ok('Ok!')
                        .targetEvent(e)
                        );
            }
        };

        vm.href = function (url) {
            window.location.href = url;
        };


        var compra;
        vm.setCompra = function (c) {
            compra = c;
        };

        vm.comprarEnUnClick = function () {
            $mdDialog.show({
                controller: CompraEnUnClick,
                controllerAs: "vm",
                template:
                        '<md-dialog aria-label="Compra en un click">' +
                        //Menu
                        '<md-toolbar>' +
                        '<div class="md-toolbar-tools">' +
                        '<h3>Compra en un click</h2>' +
                        '<span flex></span>' +
                        '<md-button ng-show="!vm.guardandoVenta && !vm.fin" aria-label="b1" class="md-icon-button" ng-click="vm.cancelar()">' +
                        '<p class="material-icons">close</p>' +
                        '</md-button>' +
                        '</div>' +
                        '</md-toolbar>' +
                        '<md-dialog-content>' +
                        //Dialogo inicial
                        '<div ng-if="!vm.guardandoVenta && !vm.fin" class="md-dialog-content" style="padding-top: 10px !important;">' +
                        '<md-subheader>Total de la venta: {{vm.total|currency:$:0}}</md-subheader>' +
                        '<md-subheader style="margin-top: 10px;">Hora de retiro: {{vm.horaRetiro}}</md-subheader>' +
                        '<div style="margin-top: 10px;">' +
                        '<md-subheader>¿Forma de pago?</md-subheader>' +
                        '<div style="padding-top: 25px;">' +
                        '<md-radio-group ng-model="vm.idFormaDePagoSeleccionada">' +
                        '<md-radio-button ng-repeat="forma in vm.formasDePago" ng-value="forma.id" ng-disabled="vm.total < forma.minimo" class="md-primary">{{forma.nombre}} ({{forma.descripcion}})</md-radio-button>' +
                        '</md-radio-group>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        //Guardando venta
                        '<div ng-if="vm.guardandoVenta" class="md-dialog-content" style="min-height: 200px;" layout="column" layout-align="center center">' +
                        '<md-progress-circular flex style="margin-top: 20px;" md-diameter="40" md-mode="indeterminate"></md-progress-circular>' +
                        '<p flex style="margin-top: 20px; color: grey;">Guardando venta</p>' +
                        '</div>' +
                        //Fin de la operación
                        '<div ng-if="vm.fin" class="md-dialog-content" style="min-height: 200px;">' +
                        //Exito true
                        '<div ng-if="vm.exito">' +
                        '<center>' +
                        '<md-icon md-svg-icon="cara_feliz" aria-label="Venta realizada" style="color: lightgrey;width: 80px; height: 80px;"></md-icon>' +
                        '<p>¡Tu compra se ha realizado con éxito!</p>' +
                        '<p>{{vm.textoExito}}</p>' +
                        '<p ng-if="vm.ventaPagada == true" style="font-weight: bold;">Nuevo saldo: {{vm.nuevoSaldo| currency:$:0}}</p>' +
                        '<p ng-if="vm.ventaPagada == false" style="font-weight: bold;">Recuerda que debes pagar {{vm.total| currency:$:0}} al retirar.</p>' +
                        '</center>' +
                        '</div>' +
                        //Exito false
                        '<div ng-if="vm.exito == false">' +
                        '<center>' +
                        '<md-icon md-svg-icon="cara_triste" aria-label="Error de stock" style="color: lightgrey;width: 80px; height: 80px;"></md-icon>' +
                        '<p ng-if="vm.stockInsuficiente == true">¡No hay stock para realizar la compra!</p>' +
                        '<p ng-if="vm.saldoInsuficiente == true">¡No tienes saldo suficiente para realizar la compra!</p>' +
                        '<p ng-if="vm.otroError == true">¡Ups... ha ocurrido un error al procesar la compra!</p>' +
                        '</center>' +
                        '</div>' +
                        '</div>' +
                        '</md-dialog-content>' +
                        '<md-dialog-actions layout="row">' +
                        //Botones dialogo inicial
                        '<div ng-if="!vm.guardandoVenta && !vm.fin">' +
                        '<md-button aria-label="b2" ng-click="vm.cancelar()">cancelar</md-button>' +
                        '<md-button class="md-primary md-raised" aria-label="b5" ng-click="vm.guardarVenta()" ng-disabled="vm.desactivar">comprar</md-button>' +
                        '</div>' +
                        //Boton fin
                        '<div ng-if="vm.fin">' +
                        '<md-button class="md-primary md-raised" aria-label="b8" ng-click="vm.ok()">ok</md-button>' +
                        '</div>' +
                        '</md-dialog-actions>' +
                        '</md-dialog>',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                locals: {
                    compra: compra
                }
            }).then(function (exito) {
                if (exito) {
                    vm.consultarPedidos();
                }
            });

            function CompraEnUnClick($http, compra) {
                var vm = this;
                vm.desactivar = true;
                vm.horaRetiro = "Obteniendo hora...";
                vm.total = compra.infoCompra.total;
                vm.idFormaDePagoSeleccionada = compra.infoCompra.idFormaPago;
                vm.formasDePago = [
                    {id: 1, descripcion: "Pagar ahora", nombre: "Pre pago", minimo: 0},
                    {id: 2, descripcion: "Pagar al retirar", nombre: "JUNAEB", minimo: 1300},
                    {id: 3, descripcion: "Pagar al retirar", nombre: "Efectivo", minimo: 0}
                ];

                $http.get('GetHoraServlet', {params: {masX: true}})
                        .then(function (response) {
                            vm.horaRetiro = response.data.hora + " hrs.";
                            vm.desactivar = false;
                        }, function () {
                            vm.horaRetiro = "Ha ocurrido un error al obtener la hora de retiro!.";
                            vm.desactivar = true;
                        });

                vm.cancelar = function () {
                    $mdDialog.cancel();
                };

                vm.guardarVenta = function () {
                    vm.guardandoVenta = true;
                    var datosVenta = {horaDeRetiro: vm.horaRetiro, idFormaPago: vm.idFormaDePagoSeleccionada};
                    var venta = [];
                    venta.push(datosVenta);
                    venta.push(compra.detalle);
                    $http.post('FinalizarVentaAppClienteServlet', venta)
                            .then(function (response) {
                                vm.guardandoVenta = false;
                                vm.fin = true;
                                vm.exito = response.data[0].exito;
                                vm.textoExito = 'Tienes el pedido n° ' + response.data[0].idPedido + ' y la hora de retiro es a las ' + response.data[0].horaRetiroPedido + '.';
                                vm.ventaPagada = response.data[0].ventaPagada;
                                if (vm.ventaPagada) {
                                    vm.nuevoSaldo = response.data[0].nuevoSaldoCliente;
                                }
                                sendComanda(response.data[0].idPedido, response.data[0].horaRetiroPedido);
                            }, function (error) {
                                if (error.status == 400) {
                                    vm.guardandoVenta = false;
                                    vm.fin = true;
                                    vm.exito = error.data[0].exito;
                                    if (error.data[0].razon == "stockInsuficiente") {
                                        vm.stockInsuficiente = true;
                                    } else if (error.data[0].razon == "saldoInsuficiente") {
                                        vm.saldoInsuficiente = true;
                                    }
                                } else {
                                    vm.fin = true;
                                    vm.exito = false;
                                    vm.guardandoVenta = false;
                                    vm.otroError = true;
                                }
                            });
                };

                vm.ok = function () {
                    $mdDialog.hide(vm.exito);
                };
            }
        };

        vm.consultarPedidos = function () {
            vm.cargandoPedidos = true;
            PedidosPendientes.getPedidos()
                    .then(function (data) {
                        vm.pedidos = data;
                        vm.cargandoPedidos = false;
                    }).catch(function (error) {

            });
        };

        vm.buscarProductos = function (busqueda) {
            var __busqueda = document.getElementById("busqueda");
            __busqueda.blur();
            vm.productos = [];
            if (vm.idCat == 0) {
                vm.idCat = null;
            }
            document.querySelector(".spinner").style.display = "";
            document.getElementById("div__titulo").style.display = "none";
            document.getElementById("div__noResultado").style.display = "none";
            ProductosFactory.buscarProductos(busqueda)
                    .then(function (data) {
                        vm.productos = data;
                        document.querySelector(".spinner").style.display = "none";
                        if (vm.idCat == 0 && vm.productos.length > 0) {
                            document.getElementById("div__titulo").style.display = "";
                        }
                        if (vm.productos.length == 0) {
                            document.getElementById("div__noResultado").style.display = "";
                        }
                    }).catch(function (error) {
                console.log(error);
            });
        };

        vm.volverAtras = function () {
            if (vm.idCat == null) {
                vm.idCat = 0;
            }
            document.getElementById("busqueda").blur();
            document.getElementById("div__noResultado").style.display = "none";
            vm.busqueda = null;
            vm.cargarProductos(vm.idCat);
        };

        angular.element(document).ready(function () {
            PedidosPendientes.getPedidos()
                    .then(function (data) {
                        vm.pedidos = data;
                        UltimasCompras.getCompras()
                                .then(function (data) {
                                    vm.compras = data;
                                    compra = vm.compras[0];
                                    vm.cargarProductos(0);
                                }).catch(function (error) {
                        });
                    }).catch(function (error) {
                        location.reload();
            });
        });

    }).factory('ProductosFactory', function ($http, $q) {

        return {
            leerTodo: leerTodo,
            buscarProductos: buscarProductos
        };

        function leerTodo(idCategoria) {
            var defered = $q.defer();
            var promise = defered.promise;

            $http.get('LeerProductosParaCatalogoServlet', {params: {idCategoria: idCategoria}})
                    .success(function (data) {
                        defered.resolve(data);
                    })
                    .error(function (err) {
                        defered.reject(err);
                    });

            return promise;
        }

        function buscarProductos(busqueda) {
            var defered = $q.defer();
            var promise = defered.promise;

            $http.get('BuscarProductosClienteServlet', {params: {busqueda: busqueda}})
                    .success(function (data) {
                        defered.resolve(data);
                    })
                    .error(function (err) {
                        defered.reject(err);
                    });

            return promise;
        }
        ;

    }).factory('Carrito', function () {

        return {
            agregarProducto: agregarProducto,
            buscarProducto: buscarProducto,
            hayCarrito: hayCarrito
        };

        function agregarProducto(producto) {
            var itemCarro =
                    {
                        idProducto: producto.idProducto,
                        nombre: producto.nombre,
                        cantidad: producto.cantidad,
                        precio: producto.precio,
                        subTotal: (producto.cantidad * producto.precio),
                        stockExcedido: false
                    };

            if (localStorage.getItem("carrito") == null) {
                if (itemCarro.cantidad > 0) {
                    var carrito = [];
                    carrito.push(itemCarro);
                    localStorage.setItem("carrito", JSON.stringify(carrito));
                }
            } else {
                var repetido = false;
                var seElimino = false;
                var carrito = JSON.parse(localStorage.getItem('carrito'));
                for (var i = 0; i < carrito.length; i++) {
                    if (carrito[i].idProducto == producto.idProducto) {
                        if (producto.cantidad == 0) {
                            carrito.splice(i, 1);
                            seElimino = true;
                            break;
                        } else {
                            carrito[i] = itemCarro;
                            repetido = true;
                            break;
                        }
                    }
                }
                if (repetido == false && seElimino == false) {
                    carrito.push(itemCarro);
                }

                if (carrito.length == 0) {
                    localStorage.removeItem("carrito");
                } else {
                    localStorage.setItem("carrito", JSON.stringify(carrito));
                }

            }
            return seElimino;
        }

        function buscarProducto(p) {
            if (localStorage.getItem("carrito") != null) {
                var i = 0;
                var carrito = JSON.parse(localStorage.getItem('carrito'));
                while (i < carrito.length) {
                    if (carrito[i].idProducto == p.idProducto) {
                        p.cantidad = carrito[i].cantidad;
                        break;
                    }
                    i++;
                }
            }
            return p;
        }

        function hayCarrito() {
            var r = false;
            if (localStorage.getItem("carrito") != null) {
                r = true;
            }
            return r;
        }
    }).factory("UltimasCompras", function ($q, $http) {
        return {
            getCompras: getCompras
        };

        function getCompras() {
            var defered = $q.defer();
            var promise = defered.promise;

            //LeerUltimasComprasConDetalleParaClienteServlet
            $http.get('LeerComprasFrecuentesClienteServlet')
                    .success(function (data) {
                        defered.resolve(data);
                    })
                    .error(function (err) {
                        defered.reject(err);
                    });

            return promise;
        }
    }).factory("PedidosPendientes", function ($q, $http) {
        return {
            getPedidos: getPedidos
        };

        function getPedidos() {
            var defered = $q.defer();
            var promise = defered.promise;

            $http.get('LeerPedidosPendientesClienteServlet')
                    .success(function (data) {
                        defered.resolve(data);
                    })
                    .error(function (err) {
                        defered.reject(err);
                    });

            return promise;
        }
    }).service("DatosUsuario", function () {
        var nombre;
        var rut;

        return {
            setNombre: setNombre,
            getNombre: getNombre,
            getRut: getRut,
            setRut: setRut
        };

        function setNombre(n) {
            nombre = n;
        }

        function getNombre() {
            return nombre;
        }

        function setRut(r) {
            rut = r;
        }

        function getRut() {
            return rut;
        }
    }).directive("noClickDerecho", function () {
        return {
            restrict: 'A',
            link: function (scope, elemento)
            {
                elemento.bind("contextmenu", function (e)
                {
                    e.preventDefault();
                });
            }
        };
    });
})();