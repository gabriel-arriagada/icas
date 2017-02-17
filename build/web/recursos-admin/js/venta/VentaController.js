(function () {
    'use strict';
    angular.module('AppAdmin').controller('VentaController', function ($mdDialog, $http, $mdToast, CompartirVentaService, LeerVentasFactory) {
        var vm = this;
        leer();
        vm.seleccionado = [];
        vm.ventas = [];
        var ventaSeleccionada;//objeto que contiene el item de categoria seleccionado
        //vm.modes = [];/*Para carga circular*/


        vm.itemSeleccionado = function (item) {
            ventaSeleccionada = item;//item u objeto
        };

        vm.ordenSeleccionado = function (orden) {
            //console.log('Ordenado por: ', orden);
        };

        vm.query = {
            order: 'idVenta',
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

        function eliminarVentaDeArray(objetoParaEliminar) {
            for (var i = 0; i < vm.ventas.length; i++) {
                if (vm.ventas[i].idVenta == objetoParaEliminar.idVenta) {
                    vm.ventas.splice(i, 1);
                    break;
                }
            }
        }

        function leer() {
            vm.promise = LeerVentasFactory.leerTodo()
                    .then(function (respuesta) {
                        vm.ventas = respuesta;
                    })
                    .catch(function (respuesta) {
                        mostrarToast("Ha ocurrido un error al cargar los datos!");
                    });
        }

        vm.eliminar = function (ev) {
            var confirmacion = $mdDialog.confirm()
                    .title('Eliminar')
                    .textContent("¿Estás seguro de eliminar la venta n° " + ventaSeleccionada.idVenta + "?")
                    .ariaLabel('Eliminar categoría')
                    .targetEvent(ev)
                    .ok('Eliminar!')
                    .cancel('Cancelar');
            $mdDialog.show(confirmacion).then(function () {
                $http.delete('EliminarVentaServlet', {params: {idVenta: ventaSeleccionada.idVenta}})
                        .then(
                                function (respuesta) {
                                    if (respuesta.status == 200)
                                    {
                                        mostrarToast("La venta fue eliminada!");
                                        reiniciarSeleccionado();
                                        eliminarVentaDeArray(ventaSeleccionada);
                                    }
                                }, function (respuesta) {
                            mostrarToast("Ha ocurrido un error en la operación,\n"
                                    + "la venta NO fue eliminada!");

                        });
            }, function () {
            });
        };

        vm.verDetalle = function () {
            CompartirVentaService.setProperty(ventaSeleccionada.idVenta);
            $mdDialog.show({
                controller: "VerDetalleController",
                controllerAs: "vm",
                templateUrl: 'recursos-admin/vistas/venta/ver-detalle.template.html',
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true
            });
        };

        vm.reiniciarQueryPage = function (){
            vm.query.page = 1;
        };

    });
})();
