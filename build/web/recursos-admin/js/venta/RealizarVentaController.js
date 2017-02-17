(function () {
    'use strict';
    angular.module('AppAdmin').controller('RealizarVentaController', function (BuscarProductoFactory,
            CompartirRutService, $mdToast, $http, $mdDialog, $filter, $route, verificar) {

        var vm = this;
        vm.detalles = [];
        vm.seleccionado = [];
        vm.total = 0;
        vm.excedidos = [];
        vm.rutClienteJUNAEB = null;
        vm.rutClienteEfectivo = null;
        vm.escanear = false;




        /*CONTTROL TAB ITEMS*/
        vm.tabItems = {itemSeleccionado: 0, escanearProductoDisabled: true, formaDePagoDisabled: true, finalizarDisabled: true};
        vm.avanzarDeTabItem = function (indice) {
            switch (indice) {
                case 1:
                    vm.tabItems.formaDePagoDisabled = false;
                    break;
                case 2:
                    vm.tabItems.finalizarDisabled = false;
                    break;
            }
            vm.tabItems.itemSeleccionado = indice;
        };
        /*FIN CONTROL TAB ITEMS*/






        /*CONTROL FORMAS DE PAGO*/
        vm.idFormaDePagoSeleccionada = 0;
        vm.formasDePago = [
            {id: 1, descripcion: "Cuenta casino", nombre: "Prepago", minimo: 0},
            {id: 2, descripcion: "Ticket o tarjeta Junaeb", nombre: "Junaeb", minimo: 1300},
            {id: 3, descripcion: "Billetes y monedas", nombre: "Efectivo", minimo: 0}
        ];

        vm.setFormaDePago = function (forma) {
            vm.nombreFormaDePago = " - pago " + forma;
        };
        /*FIN CONTROL FORMAS DE PAGO*/







        /*CONTROL FINALIZAR VENTA EFECTIVO*/
        vm.vuelto = 0;
        vm.buscando = false;
        /*FIN CONTROL FINALIZAR VENTA EFECTIVO*/






        /*CONTROL FINALIZAR VENTA PAGO PREPAGO*/
        vm.tallaIcono = "width:200px;height:200px;";
        vm.rutClientePrepago = null;
        vm.saldoInsuficiente = false;
        vm.buscandoCliente = false;
        vm.clienteExiste = false;
        vm.usuario = {};
        vm.verificarRut = function () {
            if (vm.rutClientePrepago != null) {
                var retorno = verificar.validarRut(vm.rutClientePrepago);
                if (retorno != "ok") {
                    vm.rutClientePrepago = null;
                    $mdDialog.show(
                            $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .title('Mensaje del sistema')
                            .textContent(retorno)
                            .ariaLabel('Mesaje del sistema')
                            .ok('Aceptar')
                            ).then(function () {
                    });
                } else {
                    vm.buscandoCliente = true;
                    buscarCuenta();
                }
            }
        };

        function buscarCuenta() {
            $http.get("LeerCuentaServlet", {params: {rut: vm.rutClientePrepago}})
                    .then(function (cuenta) {
                        vm.clienteExiste = true;
                        vm.tallaIcono = "width:128px;height:128px;";
                        vm.usuario.nombre = cuenta.data.nombre;
                        vm.usuario.apellido = cuenta.data.apellido;
                        vm.usuario.saldo = cuenta.data.saldo;
                        vm.buscandoCliente = false;
                        calcularNuevoSaldo();
                    }, function (data) {
                        vm.buscandoCliente = false;
                        if (data.status == 404) {
                            $mdDialog.show(
                                    $mdDialog.alert()
                                    .clickOutsideToClose(true)
                                    .title('Mensaje del sistema')
                                    .textContent("El rut ingresado no está asociado a ningún cliente")
                                    .ariaLabel('Mesaje del sistema')
                                    .ok('Aceptar')
                                    ).then(function () {
                            });
                        }
                    });
        }

        function calcularNuevoSaldo() {
            var nuevoSaldo = (vm.usuario.saldo - vm.total);
            if (nuevoSaldo < 0) {
                vm.saldoInsuficiente = true;
            } else {
                vm.saldoInsuficiente = false;
                vm.nuevoSaldo = nuevoSaldo;
            }
        }
        /*FIN CONTROL FINALIZAR VENTA PREPAGO*/







        /*GESTIÖN VENTA (DE BUSCAR PRODUCTOS, AGREGARLOS A LA TABLA Y GUARDAR LA VENTA)*/

        vm.quitarProductoEscaneado = function () {
            var indice = 0;
            while (indice < vm.seleccionado.length) {
                for (var i = 0; i < vm.detalles.length; i++) {
                    if(vm.seleccionado[indice].idProducto == vm.detalles[i].idProducto){
                        vm.detalles.splice(i, 1);
                    }
                }
                indice++;
            }
            vm.seleccionado = [];
            sumarTotal();
        };

        function sumarTotal() {
            var total = 0;
            for (var i = 0; i < vm.detalles.length; i++) {
                total = total + vm.detalles[i].subTotal;
            }
            vm.total = total;
        }

        function agregarProductoEnTabla(producto) {
            var indice = existeEnTabla(producto.idProducto);
            if (indice > -1) {
                vm.detalles[indice].cantidad = vm.detalles[indice].cantidad + 1;
                vm.detalles[indice].subTotal = vm.detalles[indice].precio * vm.detalles[indice].cantidad;
            } else {
                var nuevoItem = {};
                nuevoItem.idProducto = producto.idProducto;
                nuevoItem.nombre = producto.nombre;
                nuevoItem.precio = producto.precio;
                nuevoItem.cantidad = 1;
                nuevoItem.subTotal = producto.precio;
                vm.detalles.push(nuevoItem);
                vm.excedidos.push(false);
            }
            sumarTotal();
        }

        function existeEnTabla(idProducto) {
            var indice = -1;
            for (var i = 0; i < vm.detalles.length; i++) {
                if (vm.detalles[i].idProducto == idProducto) {
                    indice = i;
                    break;
                }
            }
            return indice;
        }

        function extraerCantidad(codigo) {
            var cantidad = 0;
            var indice = existeEnTabla(codigo);
            if (indice > -1) {
                cantidad = vm.detalles[indice].cantidad + 1;
            } else {
                cantidad = 1;
            }
            return cantidad;
        }

        vm.buscarProducto = function (codigo) {

            var cantidad = extraerCantidad(codigo);

            vm.promise = BuscarProductoFactory.buscarProducto(codigo, cantidad)
                    .then(function (respuesta) {
                        agregarProductoEnTabla(respuesta.data);
                    })
                    .catch(function (respuesta) {
                        switch (respuesta.status) {
                            case 404:
                                mostrarToast("El producto no está registrado en la base de datos!");
                                break;
                            case 409:
                                mostrarToast("El stock del producto no es suficiente!");
                                break;
                            default:
                                mostrarToast("Ha ocurrido un error al buscar el producto!");
                                break;
                        }
                    });
        };

        function reemplazarStockPorStockMaximo(productos) {
            for (var i = 0; i < productos.length; i++) {
                for (var j = 0; j < vm.detalles.length; j++) {
                    if (vm.detalles[j].idProducto == productos[i].idProducto) {
                        vm.detalles[j].cantidad = productos[i].stockMaximo;
                        vm.detalles[j].subTotal = vm.detalles[j].cantidad * vm.detalles[j].precio;
                        vm.excedidos[j] = true;
                    }
                }
            }
            sumarTotal();
        }

        vm.guardarVenta = function () {
            var rutCliente = "null";
            switch (vm.idFormaDePagoSeleccionada) {
                case 1://Prepago
                    if (vm.rutClientePrepago != null) {
                        rutCliente = vm.rutClientePrepago;
                    }
                    break;
                case 2://JUNAEB
                    if (vm.rutClienteJUNAEB != null) {
                        rutCliente = vm.rutClienteJUNAEB;
                    }
                    break;
                case 3://Efectivo
                    if (vm.rutClienteEfectivo != null) {
                        rutCliente = vm.rutClienteEfectivo;
                    }
                    break;
            }

            var venta = {rutCliente: rutCliente, idFormaPago: vm.idFormaDePagoSeleccionada};

            var data = [venta, vm.detalles];

            $http.post('FinalizarVentaAppAdmin', data)
                    .then(function (respuesta) {

                        mostrarMensaje("¡La venta se ha sido exitosa!");

                    }, function (error) {

                        if (error.status == 409) {

                            $mdDialog.show(
                                    $mdDialog.alert()
                                    .clickOutsideToClose(true)
                                    .title('Mensaje del sistema')
                                    .textContent("No tienes permiso para realizar auto ventas!")
                                    .ariaLabel('Mesaje del sistema')
                                    .ok('Aceptar')
                                    ).then(function () {
                            });

                        } else {
                            var razonError = error.data[0].razon;
                            if (razonError == "stockInsuficiente") {

                                mensajeDeStockSobrepasado(error.data[1]);

                            } else if (razonError == "saldoInsuficiente") {

                                $mdDialog.show(
                                        $mdDialog.alert()
                                        .clickOutsideToClose(true)
                                        .title('Mensaje del sistema')
                                        .textContent("¡El saldo del cliente es insuficiente!")
                                        .ariaLabel('Mesaje del sistema')
                                        .ok('Aceptar')
                                        ).then(function () {
                                });

                            } else {
                                mostrarMensaje("¡Ha ocurrido un error al guardar la venta!. La venta NO se guardó.");
                            }
                        }

                    });
        };



        function mensajeDeStockSobrepasado(data) {
            var confirmacion = $mdDialog.confirm()
                    .title('¡Stock excedido!')
                    .textContent("Se ha modificado el stock de algunos productos debido a que\n"
                            + "se han realizado compras a través de la aplicación móvil. A continuación\n"
                            + "se reemplazarán automáticamente las cantidades seleccionadas por el número\n"
                            + "máximo de unidades disponibles para los productos en conflicto.")
                    .ariaLabel('Stock excedido')
                    .targetEvent()
                    .ok('Ok!')
                    .cancel('Salir sin guardar la venta');

            $mdDialog.show(confirmacion)
                    .then(function () {
                        reemplazarStockPorStockMaximo(data);
                        vm.tabItems.itemSeleccionado = 0;
                    }, function () {
                        vm.cancelarVenta();
                    });
        }
        /*FIN DE GESTIÖN DE VENTA*/







        /*MËTODOS GENËRICOS*/
        vm.cancelarVenta = function () {
            reiniciarControlador();
        };

        function reiniciarControlador() {
            $route.reload();
        }

        function mostrarToast(mensaje) {
            $mdToast.show(
                    $mdToast.simple()
                    .textContent(mensaje)
                    .position('bottom right')//.position('top right')
                    .hideDelay(6000)
                    );
        }

        function mostrarMensaje(texto) {
            $mdDialog.show(
                    $mdDialog.alert()
                    .clickOutsideToClose(true)
                    .title('Mensaje del sistema')
                    .textContent(texto)
                    .ariaLabel('Mesaje del sistema')
                    .ok('Aceptar')
                    ).then(function () {
                vm.cancelarVenta();
            });
        }
        /*FIN GENËRECIOS*/

        vm.textoScan = "Toca y comienza a escanear:   ";
        vm.enviarFocoTabVenta = function (escanear) {
            if (!escanear) {
                vm.textoScan = "Listo para escanear productos:   ";
            } else {
                vm.textoScan = "Toca y comienza a escanear:   ";
            }            
        };

        vm.desenfocado = function () {
            vm.textoScan = "Toca y comienza a escanear:   ";
            vm.escanear = false;            
        };

    });
})();
