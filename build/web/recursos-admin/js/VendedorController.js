(function () {
    'use strict';
    angular.module('AppAdmin').controller('VendedorController', function (CompartirRutService, $location, $mdDialog) {
        var vm = this;

        vm.tabsTexto = [
            {texto: "vender"},
            {texto: "cargar cuenta"},
            {texto: "ventas"},
            {texto: "productos"}];
        
        vm.cambiarTab = function (indice) {
            switch (indice) {
                case 0:
                    $location.path('/realizar-venta');
                    break;
                case 1:
                    $location.path('/cargar-cuenta');
                    break;
                case 2:
                    $location.path('/ventas-ven');
                    break;
                case 3:
                    $location.path('/productos-ven');
                    break;
            }
        };

        vm.obtenerRut = function (rut) {
            CompartirRutService.setProperty(rut);
        };

        vm.abrirPantallaCambiarClave = function () {
            $mdDialog.show({
                controller: "CambiarClaveAccesoController",
                controllerAs: "vm",
                templateUrl: 'recursos-admin/vistas/empleado/cambiar-clave-acceso.template.html',
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: false
            });
        };

    });
})();
