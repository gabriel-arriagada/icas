(function () {
    'use strict';
    angular.module("AppAdmin").controller("VentaVendedorController", function ($mdDialog, $mdToast, CompartirVentaService, LeerVentasFactory) {
        var vm = this;
        leer();
        vm.seleccionado = [];
        vm.ventas = [];
        var ventaSeleccionada;        


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

        function leer() {
            vm.promise = LeerVentasFactory.leerTodo()
                    .then(function (respuesta) {
                        vm.ventas = respuesta;
                    })
                    .catch(function (respuesta) {
                        mostrarToast("Ha ocurrido un error al cargar los datos!");
                    });
        }
        
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
