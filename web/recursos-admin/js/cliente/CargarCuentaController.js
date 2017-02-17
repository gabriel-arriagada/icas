(function () {
    'use strict';
    angular.module('AppAdmin').controller('CargarCuentaController', function (verificar, $mdDialog, $http, $route) {
        var vm = this;

        vm.buscando = false;
        vm.cliente = {};
        vm.clienteExiste = false;
        vm.tallaIcono = "width:200px;height:200px;";

        function buscarCuenta() {
            $http.get("LeerCuentaServlet", {params: {rut: vm.rut}})
                    .then(function (cuenta) {
                        vm.clienteExiste = true;
                        vm.tallaIcono = "width:128px;height:128px;";
                        vm.cliente.nombre = cuenta.data.nombre;
                        vm.cliente.apellido = cuenta.data.apellido;
                        vm.cliente.saldo = cuenta.data.saldo;
                        vm.cliente.recargaMaxima = cuenta.data.recargaMaxima;
                        vm.buscando = false;
                    }, function (data) {
                        vm.buscando = false;
                        if (data.status == 404) {
                            mostrarMensaje("El rut ingresado no está asociado a ningún cliente");
                        }
                    });
        }

        function generarRecarga(claveEmpleado) {
            var datos = {clave: claveEmpleado, rut: vm.rut, montoCarga: vm.carga};
            $http.put("CargarCuentaServlet", datos)
                    .then(function () {
                        mostrarMensaje("Se a realizado una recarga de $" + vm.carga
                                + " al cliente " + vm.cliente.nombre + " " + vm.cliente.apellido + "."
                                + " El nuevo saldo es: $" + vm.nuevoSaldo);
                        reiniciarControlador();
                    }, function (error) {
                        switch (error.status) {
                            case 401:
                                mostrarMensaje("ERROR: La clave ingresada NO es correcta");
                                break;
                            default:
                                mostrarMensaje("Ha ocurrido un error mientras se realizaba la transacción."
                                        + "La recarga NO fue realizada.");
                                break;
                        }
                    });
        }

        vm.verificarRut = function () {
            if (vm.rut != null) {
                var retorno = verificar.validarRut(vm.rut);
                if (retorno != "ok") {
                    vm.rut = null;
                    mostrarMensaje(retorno);
                } else {
                    vm.buscando = true;
                    buscarCuenta();
                }
            }
        };

        vm.calcularSaldo = function () {
            vm.nuevoSaldo = vm.cliente.saldo + vm.carga;
        };


        function ObtenerClaveController($mdDialog) {
            var vm = this;
            vm.cancelar = function () {
                $mdDialog.cancel();
            };
            vm.ok = function () {
                $mdDialog.hide(vm.claveEmpleado);
            };
        }

        vm.guardarCarga = function () {
            if (vm.carga > vm.cliente.recargaMaxima) {
                mostrarMensaje("El monto de carga no puede ser superior al monto máximo indicado");
            } else if (vm.carga < 0) {
                mostrarMensaje("El monto de carga debe ser mayor a cero");
            } else {
                $mdDialog.show({
                    controller: ObtenerClaveController,
                    controllerAs: "vm",
                    template:
                            '<md-dialog aria-label="Obtener clave de empleado">' +
                            '<form name="formClave">' +
                            '<md-toolbar>' +
                            '<div class="md-toolbar-tools">' +
                            '<h3>Ingresa tu clave de acceso</h2>' +
                            '<span flex></span>' +
                            '<md-button aria-label="b1" class="md-icon-button" ng-click="vm.cancelar()">' +
                            '<md-icon md-svg-icon="cerrar"></md-icon>' +
                            '</md-button>' +
                            '</div>' +
                            '</md-toolbar>' +
                            '<md-dialog-content>' +
                            '<div class="md-dialog-content" layout="column">' +
                            '<md-input-container>' +
                            '<label>Clave actual</label>' +
                            '<input name="claveEmpleado" ng-model="vm.claveEmpleado" required ng-minlength="4" maxlength="10" type="password">' +
                            '<div ng-messages="formClave.claveEmpleado.$error" md-auto-hide="false" ng-show="formClave.claveEmpleado.$touched" ng-messages-multiple>' +
                            '<div ng-message="required">Ingresa tu actual clave</div>' +
                            '<div ng-message="minlength">La clave debe contener como mínimo 4 dígitos</div>' +
                            '</div>' +
                            '</md-input-container>' +
                            '</div>' +
                            '</md-dialog-content>' +
                            '<md-dialog-actions layout="row">' +
                            '<md-button aria-label="b12" ng-click="vm.cancelar()">cancelar</md-button>' +
                            '<md-button class="md-primary md-raised" aria-label="b2" ng-disabled="formClave.$invalid" ng-click="vm.ok()">continuar con la recarga</md-button>' +
                            '</md-dialog-actions>' +
                            '</form>' +
                            '</md-dialog>',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false
                }).then(function (clave) {
                    generarRecarga(clave);
                });
            }
        };

        function reiniciarControlador() {
            $route.reload();
        }

        vm.cancelar = function () {
            reiniciarControlador();
        };

        function mostrarMensaje(texto) {
            $mdDialog.show(
                    $mdDialog.alert()
                    .clickOutsideToClose(true)
                    .title('Mensaje del sistema')
                    .textContent(texto)
                    .ariaLabel('Left to right demo')
                    .ok('Aceptar')
                    );
        }

    });
})();
