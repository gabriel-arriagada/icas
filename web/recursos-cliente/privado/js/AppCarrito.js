(function () {
    angular.module("AppCarrito", ['ngMaterial', 'ngMessages', 'ngWebSocket'
                , 'md.data.table', 'mdPickers', 'ngAnimate']).config(function ($mdThemingProvider, $mdIconProvider) {
        var cyanTextoBlanco = $mdThemingProvider.extendPalette('cyan', {
            'contrastDefaultColor': 'light'
        });
        $mdThemingProvider.definePalette('cyanTextoBlanco', cyanTextoBlanco);
        $mdThemingProvider.theme('default').primaryPalette('cyanTextoBlanco');

        $mdIconProvider
                .icon("cerrar", "recursos-cliente/privado/svg/cerrar_24px.svg", 24)
                .icon("eliminar", "recursos-cliente/privado/svg/eliminar_24px.svg", 24)
                .icon("carro", "recursos-cliente/privado/svg/no_carro_24px.svg", 24)
                .icon("reloj", "recursos-cliente/privado/svg/reloj_24px.svg", 24)
                .icon("cara_triste", "recursos-cliente/privado/svg/triste_24px.svg", 24)
                .icon("cara_feliz", "recursos-cliente/privado/svg/feliz_24px.svg", 24)
                .icon("atras", "recursos-cliente/privado/svg/atras_24px.svg", 24);

    }).controller("CarritoController", function (Carrito, $mdpTimePicker,
            $filter, $websocket, HoraFactory, DatosUsuario, $http) {

        var vm = this;
        vm.carro = [];
        vm.seleccionado = [];
        vm.total = 0;
        vm.permitidoPagar = false;
        var productoSeleccionado = null;
        var productosEliminados = [];
        vm.query = {order: 'subTotal', limit: 0, page: 1};
        vm.avanzarDeTabItem = function (tabItem) {
            vm.tabItemSeleccionado = tabItem;
        };

        var ws = $websocket('wss://i-cas.tk/icas/comandas');

        ws.onError(function (event) {
            console.log('WebSocket: Error de conexión');
            console.log(event);
        });

        ws.onClose(function (event) {
            console.log('WebSocket: Conexión cerrada');
            console.log(event);
        });

        ws.onOpen(function () {
            console.log('WebSocket: Conexión abierta');
        });

        function sendComanda(idPedido, horaRetiro) {
            var comanda =
                    {
                        idPedido: idPedido,
                        nombre: DatosUsuario.getNombre(),
                        rut: DatosUsuario.getRut(),
                        hora: horaRetiro,
                        estado: 'Recibido'
                    };
            console.log(comanda);
            ws.send(JSON.stringify(comanda));
        }



        vm.iniciar = function (nombre, rut) {
            if (nombre != '' && rut != '') {
                DatosUsuario.setNombre(nombre);
                DatosUsuario.setRut(rut);
            }
        };

        /*Formas de pago*/
        vm.idFormaDePagoSeleccionada = 0;
        vm.formasDePago = [
            {id: 1, descripcion: "Pagar ahora", nombre: "Pre pago", minimo: 0},
            {id: 2, descripcion: "Pagar al retirar (compras superiores a $1.300)", nombre: "JUNAEB", minimo: 1300},
            {id: 3, descripcion: "Pagar al retirar", nombre: "Efectivo", minimo: 0}
        ];

        /*Gestión carro de compras*/
        angular.element(document).ready(function () {
            getCarroDeCompras();
        });

        function getTotal(arreglo) {
            var total = 0;
            for (var i = 0; i < arreglo.length; i++) {
                total += arreglo[i].subTotal;
            }
            return total;
        }

        function getCarroDeCompras() {
            vm.carro = [];
            if (Carrito.getCarro() != null) {
                vm.carro = Carrito.getCarro();
            }
            vm.total = getTotal(vm.carro);
            vm.query.limit = vm.carro.length;
        }

        vm.cancelar = function () {
            window.location.href = "IndexClienteServlet";
        };

        vm.itemSeleccionado = function (item) {
            productoSeleccionado = item;
        };

        vm.eliminarItemDelCarro = function () {
            if (vm.seleccionado.length > 0) {
                Carrito.quitarProducto(productoSeleccionado);
                vm.carro.splice(vm.carro.indexOf(productoSeleccionado), 1);
                vm.seleccionado = [];
                productosEliminados.push(productoSeleccionado);
                vm.total = getTotal(vm.carro);
            }
        };
        /*Fin gestión carro de compras*/





        /*Selección de hora de retiro*/
        vm.buscandoHora = false;
        vm.mostrarTimePicker = function (event) {
            $mdpTimePicker(vm.horaDeRetiro, {
                targetEvent: event
            }).then(function (horaSeleccionada) {
                vm.buscandoHora = true;
                HoraFactory.getHora()
                        .then(function (objeto) {

                            var horaServer = objeto.hora;
                            var minimo = objeto.minimo;
                            var horaDeRetiro = $filter('date')(horaSeleccionada, 'HH:mm');
                            var diferenciaEntreHoras = moment.utc(moment(horaDeRetiro, "HH:mm")
                                    .diff(moment(horaServer, "HH:mm")))
                                    .format("HH:mm");

                            if (horaServer > horaDeRetiro || horaServer == horaDeRetiro) {

                                console.error("Error: La hora actual es mayor o igual que la hora de retiro seleccionada");
                                vm.calcularHoraDeRetiro();

                            } else if (horaServer < horaDeRetiro) {

                                if (diferenciaEntreHoras >= minimo) {

                                    vm.buscandoHora = false;
                                    vm.horaDeRetiro = horaDeRetiro;
                                    vm.permitidoPagar = true;

                                } else {

                                    console.error("Error: La hora seleccionada no está dentro del rango "
                                            + "mínimo de minutos aceptados.");
                                    vm.calcularHoraDeRetiro();
                                }

                            }

                        }).catch(function (error) {
                    console.log(error);
                });
            });
        };

        vm.calcularHoraDeRetiro = function () {
            vm.buscandoHora = true;
            HoraFactory.getHoraMasX()
                    .then(function (objeto) {
                        var horaMasX = objeto.hora;
                        vm.horaDeRetiro = horaMasX;
                        vm.permitidoPagar = true;
                        vm.buscandoHora = false;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
        };
        /*Fin de selección de hora de retiro*/




        vm.respuestaDePago = null;
        vm.otroError = false;
        vm.pagando = false;
        vm.pagar = function () {
            if (vm.carro.length > 0) {

                vm.pagando = true;
                var datosVenta = {
                    horaDeRetiro: vm.horaDeRetiro,
                    idFormaPago: vm.idFormaDePagoSeleccionada,
                    comentario: vm.comentario
                };
                var carrito = Carrito.getCarro()
                var venta = [];
                venta.push(datosVenta);
                venta.push(carrito);

                $http.post('FinalizarVentaAppClienteServlet', venta)
                        .then(function (response) {
                            vm.pagando = false;
                            vm.respuestaDePago = response.data[0];
                            vm.textoExitoTrue = 'Tienes el pedido n° '
                                    + response.data[0].idPedido
                                    + ' y la hora de retiro es a las '
                                    + response.data[0].horaRetiroPedido
                                    + 'hrs.';
                            vm.ventaPagada = response.data[0].ventaPagada;
                            if (vm.ventaPagada == true) {
                                vm.nuevoSaldo = response.data[0].nuevoSaldoCliente;
                            }
                            sendComanda(response.data[0].idPedido, response.data[0].horaRetiroPedido);
                            Carrito.eliminarCarro();
                        }, function (error) {
                            console.log(error);
                            if (error.status == 400) {
                                if (error.data[0].razon == "stockInsuficiente") {
                                    Carrito.reemplazarPorStockMaximo(error.data[1]);
                                }
                                vm.respuestaDePago = error.data[0];
                            } else {
                                vm.otroError = true;
                                vm.respuestaDePago = false;
                            }
                            vm.pagando = false;
                            vm.permitidoPagar = false;
                        });
            }
        };

        vm.reiniciarError = function () {
            vm.respuestaDePago = null;
            vm.avanzarDeTabItem(0);
            getCarroDeCompras();
        };

    }).factory("Carrito", function () {
        return {
            quitarProducto: quitarProducto,
            getCarro: getCarro,
            eliminarCarro: eliminarCarro,
            reemplazarPorStockMaximo: reemplazarPorStockMaximo
        };

        function getCarro() {
            return JSON.parse(localStorage.getItem('carrito'));
        }

        function quitarProducto(producto) {
            var carrito = JSON.parse(localStorage.getItem('carrito'));
            for (var i = 0; i < carrito.length; i++) {
                if (carrito[i].idProducto == producto.idProducto) {
                    carrito.splice(i, 1);
                    break;
                }
            }
            removerCarritoOrAgregarItem(carrito);
        }

        function removerCarritoOrAgregarItem(carrito) {
            if (carrito.length == 0) {
                localStorage.removeItem("carrito");
            } else {
                localStorage.setItem("carrito", JSON.stringify(carrito));
            }
        }

        function eliminarCarro() {
            localStorage.removeItem("carrito");
        }

        function reemplazarPorStockMaximo(listaDeStockMaximo) {
            var carrito = JSON.parse(localStorage.getItem('carrito'));
            for (var i = 0; i < listaDeStockMaximo.length; i++) {
                for (var j = 0; j < carrito.length; j++) {
                    if (listaDeStockMaximo[i].idProducto == carrito[j].idProducto) {
                        if (listaDeStockMaximo[i].stockMaximo == 0) {
                            carrito.splice(j, 1);
                            if (carrito.length == 0) {
                                eliminarCarro();
                            }
                        } else {
                            carrito[j].cantidad = listaDeStockMaximo[i].stockMaximo;
                            carrito[j].subTotal = carrito[j].precio * carrito[j].cantidad;
                            carrito[j].stockExcedido = true;
                        }
                    }
                }
            }
            localStorage.setItem("carrito", JSON.stringify(carrito));
        }

    }).factory("HoraFactory", function ($http, $q) {

        return {
            getHora: getHora,
            getHoraMasX: getHoraMasX
        };

        function getHora() {
            var deferd = $q.defer();
            var promise = deferd.promise;
            $http.get('GetHoraServlet')
                    .success(function (data) {
                        deferd.resolve(data);
                    })
                    .error(function (error) {
                        deferd.reject(error);
                    });
            return promise;
        }

        function getHoraMasX() {
            var deferd = $q.defer();
            var promise = deferd.promise;
            $http.get('GetHoraServlet', {params: {masX: true}})
                    .success(function (data) {
                        deferd.resolve(data);
                    })
                    .error(function (error) {
                        deferd.reject(error);
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