(function () {
    'use strict';
    angular.module('AppAdmin').controller('ClienteController',
            function (LeerClientesFactory, $mdToast, $mdDialog, $http, CompartirClienteService) {
                var vm = this;
                vm.seleccionado = [];
                vm.clientes = [];
                var clienteSeleccionado;
                leer();

                vm.itemSeleccionado = function (item) {
                    clienteSeleccionado = item;
                    CompartirClienteService.setProperty(item);
                };

                vm.ordenSeleccionado = function (orden) {
                };

                vm.query = {order: 'rol', limit: 5, page: 1};


                function reemplazarClienteEnArray(objectoParaReemplazar) {
                    vm.clientes[vm.clientes.indexOf(clienteSeleccionado)] = objectoParaReemplazar;
                }

                function eliminarClienteDeArray(objetoParaEliminar) {
                    vm.clientes.splice(vm.clientes.indexOf(objetoParaEliminar), 1);
                }

                function reiniciarSeleccionado() {
                    vm.seleccionado = [];
                }

                function mostrarToast(mensaje) {
                    $mdToast.show(
                            $mdToast.simple()
                            .textContent(mensaje)
                            .position('bottom right')
                            .hideDelay(6000)
                            );
                }

                function leer() {
                    vm.promise = LeerClientesFactory.leerTodo()
                            .then(function (data) {
                                vm.clientes = data;
                            })
                            .catch(function (err) {
                                mostrarToast("Ha ocurrido un error al cargar los datos!");
                            });
                }

                //Crear
                vm.crear = function (event) {
                    $mdDialog.show({
                        controller: "CrearClienteController",
                        controllerAs: "vm",
                        templateUrl: 'recursos-admin/vistas/cliente/agregar-cliente.template.html',
                        parent: angular.element(document.body),
                        targetEvent: event,
                        clickOutsideToClose: false
                    })
                            .then(function (cliente) {
                                $http.post('CrearClienteServlet', cliente)
                                        .then(
                                                function (respuesta) {
                                                    if (respuesta.status == 200)
                                                    {
                                                        mostrarToast("El cliente fue agregado!");
                                                        leer();
                                                    }
                                                }, function (respuesta) {
                                            mostrarToast("Ha ocurrido un error en la operación,\n"
                                                    + "el cliente NO ha sido creado!");

                                        });
                            }, function () {

                            });
                };


                vm.setVigencia = function (cliente) {
                    $mdDialog.show({
                        controller: VigenciaCliente,
                        controllerAs: "vm",
                        template:
                                '<md-dialog aria-label="Agregar">' +
                                '<form name="form" ng-submit="vm.guardar()">' +
                                '<md-toolbar>' +
                                '<div class="md-toolbar-tools">' +
                                '<h3>Selecciona un motivo para el cambio de vigencia</h2>' +
                                '<span flex></span>' +
                                '<md-button aria-label="b1" class="md-icon-button" ng-click="vm.cancelar()">' +
                                '<md-icon md-svg-src="cerrar" aria-label="Cerrar dialogo"></md-icon>' +
                                '</md-button>' +
                                '</div>' +
                                '</md-toolbar>' +
                                '<md-dialog-content>' +
                                '<div class="md-dialog-content" style="margin-top:30px;">' +
                                '<md-input-container class="ancho-mitad" required>' +
                                '<label>Motivos</label>' +
                                '<md-select required name="razon" ng-model="vm.idRazon">' +
                                '<md-option ng-value="razon.idRazon" ng-repeat="razon in vm.razones track by $index">' +
                                '    {{razon.razon}}' +
                                '</md-option>' +
                                ' </md-select>' +
                                '<div ng-messages="form.razon.$error">' +
                                '   <div ng-message="required">Debes seleccionar una razón</div>' +
                                ' </div>' +
                                '</md-input-container>' +
                                '</div>' +
                                '</md-dialog-content>' +
                                '<md-dialog-actions layout="row">' +
                                '<md-button aria-label="b2" ng-click="vm.cancelar()">cancelar</md-button>' +
                                '<md-button type="submit" ng-disabled="form.$invalid" aria-label="b3">guardar</md-button>' +
                                '</md-dialog-actions>' +
                                '</form>' +
                                '</md-dialog>',
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        locals: {
                            cliente: cliente
                        }
                    }).then(function (idRazon) {
                        var postData = {idRazon: idRazon, vigente: cliente.vigente, rutCliente: cliente.rut};
                        $http.post("CrearVigenciaClienteServlet", postData)
                                .then(function (response) {
                                    mostrarToast("Vigencia cambiada!");
                                }, function (error) {
                                    cliente.vigente = !cliente.vigente;
                                    mostrarToast("Ha ocurrido un problema al cambiar la vigencia!");
                                });
                    }, function () {
                        cliente.vigente = !cliente.vigente;
                    });

                    /*Controlador*/
                    function VigenciaCliente(cliente, $mdDialog, $http) {
                        var vm = this;

                        var razonesPositivas = !cliente.vigente;

                        $http.get("LeerRazonesVigenciaClienteServlet", {params: {razonesPositivas: razonesPositivas}})
                                .then(function (response) {
                                    vm.razones = response.data;
                                }, function (response) {
                                    console.log(response);
                                });


                        vm.cancelar = function () {
                            $mdDialog.cancel();
                        };

                        vm.guardar = function () {
                            $mdDialog.hide(vm.idRazon);
                        };
                    }

                };



                //Eliminar
                vm.eliminar = function (event) {
                    var confirmacion = $mdDialog.confirm()
                            .title('Eliminar')
                            .textContent("¿Estás seguro de eliminar al cliente '"
                                    + clienteSeleccionado.nombre + " " + clienteSeleccionado.apellido + "'?")
                            .ariaLabel('Eliminar cliente')
                            .targetEvent(event)
                            .ok('Eliminar!')
                            .cancel('Cancelar');
                    $mdDialog.show(confirmacion).then(function () {
                        $http.delete('EliminarClienteServlet', {params: {rut: clienteSeleccionado.rut}})
                                .then(
                                        function (respuesta) {
                                            if (respuesta.status == 200)
                                            {
                                                mostrarToast("El cliente fue eliminado!");
                                                reiniciarSeleccionado();
                                                eliminarClienteDeArray(clienteSeleccionado);
                                            }
                                        }, function (respuesta) {
                                    mostrarToast("Ha ocurrido un error en la operación,\n"
                                            + "el cliente no fue eliminado!");

                                });
                    });
                };


                //Editar
                vm.editar = function (event) {
                    $mdDialog.show({
                        controller: "EditarClienteController",
                        controllerAs: "vm",
                        templateUrl: 'recursos-admin/vistas/cliente/editar-cliente.template.html',
                        parent: angular.element(document.body),
                        targetEvent: event,
                        clickOutsideToClose: false
                    })
                            .then(function (cliente) {
                                $http.put('EditarClienteServlet', cliente)
                                        .then(
                                                function (respuesta) {
                                                    if (respuesta.status == 200) {
                                                        mostrarToast("El cliente fue modificado");
                                                        reiniciarSeleccionado();
                                                        reemplazarClienteEnArray(cliente);
                                                    }
                                                }, function (respuesta) {
                                            mostrarToast("Ha ocurrido un error en la operación"
                                                    + "el cliente NO ha sido editado!");
                                        });

                            });
                };

                vm.reiniciarQueryPage = function () {
                    vm.query.page = 1;
                };

            });
})();
