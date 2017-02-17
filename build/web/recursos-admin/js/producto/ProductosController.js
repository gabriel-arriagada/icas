(function () {
    'use strict';
    angular.module('AppAdmin')
            .controller('ProductoController', function ($mdDialog, LeerProductosFactory, $http, $mdToast, CompartirProductoService) {
                var vm = this;
                leer();
                vm.seleccionado = [];
                vm.productos = [];
                var productoSeleccionado;
                vm.modes = [];

                vm.itemSeleccionado = function (item) {
                    productoSeleccionado = item;
                    CompartirProductoService.setProperty(productoSeleccionado);
                };

                vm.ordenSeleccionado = function (orden) {
                    //console.log('Ordenado por: ', orden);
                };

                vm.query = {
                    order: 'nombreProducto',
                    limit: 5,
                    page: 1
                };

                function mostrarToast(mensaje) {
                    $mdToast.show(
                            $mdToast.simple()
                            .textContent(mensaje)
                            .position('bottom right')
                            .hideDelay(6000)
                            );
                }

                function reiniciarSeleccionado() {
                    vm.seleccionado = [];
                }

                function eliminarProductoDeArray(objetoParaEliminar) {
                    vm.productos.splice(vm.productos.indexOf(objetoParaEliminar), 1);
                }

                function reemplazarProductoEnArray(objectoParaReemplazar) {
                    vm.productos[vm.productos.indexOf(productoSeleccionado)] = objectoParaReemplazar;
                }

                /*
                 * CRUD
                 */
                //Leer
                function leer() {
                    vm.promise = LeerProductosFactory.leerTodo()
                            .then(function (data) {
                                vm.productos = data;
                            })
                            .catch(function (err) {
                                mostrarToast("Ha ocurrido un error al cargar los datos!");
                            });
                }


                //Eliminar
                vm.eliminar = function (ev) {
                    var confirmacion = $mdDialog.confirm()
                            .title('Eliminar')
                            .textContent("¿Estás seguro de eliminar el producto '" + productoSeleccionado.nombre + "'?")
                            .ariaLabel('Eliminar producto')
                            .targetEvent(ev)
                            .ok('Eliminar!')
                            .cancel('Cancelar');
                    $mdDialog.show(confirmacion).then(function () {
                        $http.delete('EliminarProductoServlet', {params: {idProducto: productoSeleccionado.idProducto}})
                                .then(
                                        function (respuesta) {
                                            if (respuesta.status == 200)
                                            {
                                                mostrarToast("El producto fue eliminado!");
                                                reiniciarSeleccionado();
                                                eliminarProductoDeArray(productoSeleccionado);
                                            }
                                        }, function (respuesta) {
                                    mostrarToast("Ha ocurrido un error en la operación,\n"
                                            + "el producto no fue eliminado!");

                                });
                    }, function () {
                    });
                };

                //Crear
                vm.crear = function (event) {
                    $mdDialog.show({
                        controller: "CrearProductoController",
                        controllerAs: "vm",
                        templateUrl: 'recursos-admin/vistas/producto/agregar-producto.template.html',
                        parent: angular.element(document.body),
                        targetEvent: event,
                        clickOutsideToClose: false
                    }).then(function (producto) {
                        $http.post('CrearProductoServlet', producto)
                                .then(function (respuesta) {
                                    if (respuesta.status == 200)
                                    {
                                        mostrarToast("El producto fue agregado!");
                                        leer();
                                    }
                                }, function () {
                                    mostrarToast("Ha ocurrido un error en la operación,\n"
                                            + "el producto NO ha sido creado!");
                                });
                    }, function () {

                    });
                };

                //Editar
                vm.editar = function (event) {
                    $mdDialog.show({
                        controller: "EditarProductoController",
                        controllerAs: "vm",
                        templateUrl: 'recursos-admin/vistas/producto/editar-producto.template.html',
                        parent: angular.element(document.body),
                        targetEvent: event,
                        clickOutsideToClose: false
                    }).then(function (producto) {
                        $http.put('EditarProductoServlet', producto)
                                .then(function (respuesta) {
                                    if (respuesta.status == 200) {
                                        mostrarToast("El producto fue modificado");
                                        reiniciarSeleccionado();
                                        //reemplazarProductoEnArray(producto);
                                        leer();
                                    }
                                }, function (respuesta) {
                                    mostrarToast("Ha ocurrido un error en la operación"
                                            + "el producto NO ha sido editado!");
                                });
                    });
                };

                //Cambiar estado
                vm.cambiarEstado = function (item) {
                    var estadoUno = item.vigente;
                    var vigente = item.vigente;
                    var idProducto = item.idProducto;
                    if (vigente == true) {
                        vigente = false;
                    } else {
                        vigente = true;
                    }
                    var producto = {accion: 100, idProducto: idProducto, vigente: vigente};
                    $http.put('EditarProductoServlet', producto)
                            .then(function (respuesta) {
                                if (respuesta.status == 200) {
                                    mostrarToast("Se ha cambiado el estado del producto!");
                                }
                            }, function (respuesta) {
                                item.vigente = estadoUno;
                                vm.productos[vm.productos.indexOf(item)] = item;
                                mostrarToast("Ha ocurrido un error en la operación"
                                        + "el producto NO ha cambiado de estado!");
                            });
                };

                //Ver y Agregar imagenes
                vm.gestionImagen = function (ver, idProducto, urlImagen) {
                    var template = "";
                    if (ver === true) {
                        template = 'recursos-admin/vistas/producto/ver-imagen.template.html';
                        CompartirProductoService.setProperty(urlImagen);
                        $mdDialog.show({
                            controller: "VerImagenController",
                            controllerAs: "vm",
                            templateUrl: template,
                            parent: angular.element(document.body),
                            targetEvent: event,
                            clickOutsideToClose: false
                        });
                    }
                    else
                    {
                        template = 'recursos-admin/vistas/producto/agregar-imagen.template.html';
                        CompartirProductoService.setProperty(idProducto);
                        $mdDialog.show({
                            controller: "AgregarImagenController",
                            controllerAs: "vm",
                            templateUrl: template,
                            parent: angular.element(document.body),
                            targetEvent: event,
                            clickOutsideToClose: false
                        }).then(function (resultado) {
                            if (resultado != null) {
                                if (resultado == true) {
                                    mostrarToast("La imagen ha sido guardada!");
                                    leer();
                                } else {
                                    mostrarToast("Ha ocurrido un problema en la operación!\n"
                                            + "La imagen NO ha sido guardada!");
                                }
                            }
                        });
                    }
                };

                vm.reiniciarQueryPage = function () {
                    vm.query.page = 1;
                };

            });
})();
