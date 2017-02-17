(function () {
    'use strict';
    angular.module('AppAdmin').controller('EmpleadoController',
            function (LeerEmpleadosFactory, $mdToast, $mdDialog, $http, CompartirEmpleadoService, $route) {
                var vm = this;

                var datosDeCarga = $route.current.$$route.paramExample;
                var idRolDeCarga = datosDeCarga.idRol;
                vm.tituloMantenedor = datosDeCarga.titulo;


                vm.seleccionado = [];
                vm.empleado = [];
                var empleadoSeleccionado;
                leer();

                vm.itemSeleccionado = function (item) {
                    empleadoSeleccionado = item;
                    CompartirEmpleadoService.setProperty(item);
                };

                vm.ordenSeleccionado = function (orden) {
                };

                vm.query = {order: 'rol', limit: 5, page: 1};


                function reemplazarEmpleadoEnArray(objectoParaReemplazar) {
                    vm.empleados[vm.empleados.indexOf(empleadoSeleccionado)] = objectoParaReemplazar;
                }

                function eliminarEmpleadoDeArray(objetoParaEliminar) {
                    vm.empleados.splice(vm.empleados.indexOf(objetoParaEliminar), 1);
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
                    vm.promise = LeerEmpleadosFactory.leerTodo(idRolDeCarga)
                            .then(function (data) {
                                vm.empleados = data;
                            })
                            .catch(function (err) {
                                mostrarToast("Ha ocurrido un error al cargar los datos!");
                            });
                }

                //Crear
                vm.crear = function (event) {
                    $mdDialog.show({
                        controller: "CrearEmpleadoController",
                        controllerAs: "vm",
                        templateUrl: 'recursos-admin/vistas/empleado/agregar-empleado.template.html',
                        parent: angular.element(document.body),
                        targetEvent: event,
                        clickOutsideToClose: false
                    })
                            .then(function (empleado) {
                                $http.post('CrearEmpleadoServlet', empleado)
                                        .then(
                                                function (respuesta) {
                                                    if (respuesta.status == 200)
                                                    {
                                                        mostrarToast("El vendedor fue agregado!");
                                                        leer();
                                                    }
                                                }, function (respuesta) {
                                            mostrarToast("Ha ocurrido un error en la operación,\n"
                                                    + "el empleado NO ha sido creado!");

                                        });
                            }, function () {

                            });
                };

                
                vm.setVigencia = function (empleado) {
                    $mdDialog.show({
                        controller: VigenciaEmpleado,
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
                            empleado: empleado
                        }
                    }).then(function (idRazon) {
                        var postData = {idRazon: idRazon, vigente: empleado.vigente, rutVendedor: empleado.rut};
                        $http.post("CrearVigenciaEmpleadoServlet", postData)
                                .then(function (response) {
                                    mostrarToast("Vigencia cambiada!");
                                }, function (error){
                                    empleado.vigente = !empleado.vigente;
                                    mostrarToast("Ha ocurrido un problema al cambiar la vigencia!");
                                });
                    }, function () {
                        empleado.vigente = !empleado.vigente;
                    });

                    /*Controlador*/
                    function VigenciaEmpleado(empleado, $mdDialog, $http) {
                        var vm = this;

                        var razonesPositivas = !empleado.vigente;

                        $http.get("LeerRazonesVigenciaEmpleadoServlet", {params: {razonesPositivas: razonesPositivas}})
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


                vm.setAutoVenta = function (empleado) {
                    $mdDialog.show({
                        controller: AutoVenta,
                        controllerAs: "vm",
                        template:
                                '<md-dialog aria-label="Agregar">' +
                                '<form name="form" ng-submit="vm.guardar()">' +
                                '<md-toolbar>' +
                                '<div class="md-toolbar-tools">' +
                                '<h3>Selecciona un motivo para el cambio de permiso</h2>' +
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
                            empleado: empleado
                        }
                    }).then(function (idRazon) {
                        var postData = {idRazon: idRazon, autoVenta: empleado.autoVenta, rutVendedor: empleado.rut};
                        $http.post("CrearAutoVentaServlet", postData)
                                .then(function (response) {
                                    mostrarToast("Permiso cambiado!");
                                }, function (error){
                                    empleado.autoVenta = !empleado.autoVenta;
                                    mostrarToast("Ha ocurrido un problema al cambiar el permiso!");
                                });
                    }, function () {
                        empleado.autoVenta = !empleado.autoVenta;
                    });

                    /*Controlador*/
                    function AutoVenta(empleado, $mdDialog, $http) {
                        var vm = this;

                        var razonesPositivas = !empleado.autoVenta;

                        $http.get("LeerRazonesAutoVentaServlet", {params: {razonesPositivas: razonesPositivas}})
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
                            .textContent("¿Estás seguro de eliminar al empleado '"
                                    + empleadoSeleccionado.nombre + " " + empleadoSeleccionado.apellido + "'?")
                            .ariaLabel('Eliminar empleado')
                            .targetEvent(event)
                            .ok('Eliminar!')
                            .cancel('Cancelar');
                    $mdDialog.show(confirmacion).then(function () {
                        $http.delete('EliminarEmpleadoServlet', {params: {rut: empleadoSeleccionado.rut}}).then(
                                function (respuesta) {
                                    if (respuesta.status == 200)
                                    {
                                        mostrarToast("El vendedor fue eliminado!");
                                        reiniciarSeleccionado();
                                        eliminarEmpleadoDeArray(empleadoSeleccionado);
                                    }
                                }, function (respuesta) {
                            mostrarToast("Ha ocurrido un error en la operación,\n"
                                    + "el vendedor no fue eliminado!");

                        });
                    });
                };


                //Editar
                vm.editar = function (event) {
                    $mdDialog.show({
                        controller: "EditarEmpleadoController",
                        controllerAs: "vm",
                        templateUrl: 'recursos-admin/vistas/empleado/editar-empleado.template.html',
                        parent: angular.element(document.body),
                        targetEvent: event,
                        clickOutsideToClose: false
                    }).then(function (empleado) {
                        $http.put('EditarEmpleadoServlet', empleado).then(
                                function (respuesta) {
                                    if (respuesta.status == 200) {
                                        mostrarToast("El registro fue modificado");
                                        reiniciarSeleccionado();
                                        reemplazarEmpleadoEnArray(empleado);
                                    }
                                }, function (respuesta) {
                            mostrarToast("Ha ocurrido un error en la operación"
                                    + "el empleado NO ha sido editado!");
                        });

                    });
                };

            });
})();
