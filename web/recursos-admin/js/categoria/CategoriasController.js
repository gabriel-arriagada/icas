angular.module('AppAdmin')
        .controller('CategoriasController', function($mdDialog, LeerCategoriasFactory, $http, $mdToast, CompartirCategoriaService) {
            var vm = this;
            leer();
            vm.seleccionado = [];
            vm.categorias = [];
            var categoriaSeleccionada;//objeto que contiene el item de categoria seleccionado
            //vm.modes = [];/*Para carga circular*/


            vm.itemSeleccionado = function(item) {
                categoriaSeleccionada = item;//item u objeto
                CompartirCategoriaService.setProperty(categoriaSeleccionada);
            };

            vm.ordenSeleccionado = function(orden) {
                //console.log('Ordenado por: ', orden);
            };

            vm.query = {
                order: 'idCategoria',
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

            function eliminarCategoriaDeArray(objetoParaEliminar) {
                vm.categorias.splice(vm.categorias.indexOf(objetoParaEliminar), 1);
            }

            function reemplazarCategoriaEnArray(objectoParaReemplazar) {
                vm.categorias[vm.categorias.indexOf(categoriaSeleccionada)] = objectoParaReemplazar;
            }

            /*
             * CRUD
             */
            //Leer
            function leer() {
                vm.promise = LeerCategoriasFactory.leerTodo()
                        .then(function(respuesta) {
                            vm.categorias = respuesta;
                        })
                        .catch(function(respuesta) {
                            mostrarToast("Ha ocurrido un error al cargar los datos!");
                        });
            }

            //Eliminar
            vm.eliminar = function(ev) {
                var confirmacion = $mdDialog.confirm()
                        .title('Eliminar')
                        .textContent("¿Estás seguro de eliminar la categoría '" + categoriaSeleccionada.categoria + "'?")
                        .ariaLabel('Eliminar categoría')
                        .targetEvent(ev)
                        .ok('Eliminar!')
                        .cancel('Cancelar');
                $mdDialog.show(confirmacion).then(function() {
                    /* Confirma la eliminación  
                     * ->Los parámetros debieron ser enviados con la etiqueta params para ser
                     * correctamente recibidos por el servidors. 
                     */
                    $http.delete('EliminarCategoriaServlet', {params: {idCategoria: categoriaSeleccionada.idCategoria}})
                            .then(
                                    function(respuesta) {
                                        //éxito callback
                                        if (respuesta.status == 200)
                                        {
                                            mostrarToast("La categoría fue eliminada!");
                                            reiniciarSeleccionado();
                                            //leer();                                                 
                                            eliminarCategoriaDeArray(categoriaSeleccionada);
                                        }
                                    }, function(respuesta) {
                                //error callback
                                mostrarToast("Ha ocurrido un error en la operación,\n"
                                        + "la categoría no fue eliminada!");

                            });
                }, function() {
                    /*Cancela la eliminación;
                     * -> Por el momento no se hace nada;
                     */
                });
            };

            //Crear
            vm.crear = function(event) {
                $mdDialog.show({
                    controller: "CrearCategoriaController",
                    controllerAs: "vm",
                    templateUrl: 'recursos-admin/vistas/categoria/agregar-categoria.template.html',
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: false
                })
                        .then(function(categoria)/*Si presiono botón guardar*/ {
                            /*Aquí se ejecuta la acción POST
                             * (El header por default es Json)
                             */
                            $http.post('CrearCategoriaServlet', categoria)
                                    .then(
                                            function(respuesta) {
                                                //éxito callback
                                                if (respuesta.status == 200)
                                                {
                                                    mostrarToast("La categoría fue agregada!");
                                                    leer();
                                                }
                                            }, function(respuesta) {
                                        //error callback
                                        mostrarToast("Ha ocurrido un error en la operación,\n"
                                                + "la categoría NO ha sido creada!");

                                    });
                        }, function() /*Si presiono botón cancelar*/ {
                            
                        });
            };

            //Editar
            vm.editar = function(event) {
                $mdDialog.show({
                    controller: "EditarCategoriaController",
                    controllerAs: "vm",
                    templateUrl: 'recursos-admin/vistas/categoria/editar-categoria.template.html',
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: false
                })
                        .then(function(categoria) {
                            $http.put('EditarCategoriaServlet', categoria)
                                    .then(
                                            function(respuesta) {
                                                if (respuesta.status == 200) {
                                                    mostrarToast("La categoría fue modificada");
                                                    reiniciarSeleccionado();
                                                    //leer();                                                    
                                                    reemplazarCategoriaEnArray(categoria);
                                                }
                                            }, function(respuesta) {
                                        mostrarToast("Ha ocurrido un error en la operación"
                                                + "La categoría NO ha sido editada!");
                                    });

                        });
            };
});