/*Este controlador se usa para definir la estructura de la interfaz de adminstrador.jsp*/
angular.module('AppAdmin')
        .controller('AdminController', function ($mdSidenav, CompartirRutService, $location, $mdDialog) {
            var vm = this;

            vm.toggleSideNav = function () {
                $mdSidenav('left').toggle();
            };

            vm.menu = [
                {nombre: 'Panel principal', icon: 'dashboard', url: '#/realizar-venta'},
                {nombre: 'Cargar cuenta', icon: 'moneda', url: '#/cargar-cuenta'},
                {nombre: 'Ventas', icon: 'productos', url: '#/ventas'},
                {nombre: 'Categorías', icon: 'productos', url: '#/categorias'},
                {nombre: 'Productos', icon: 'productos', url: '#/productos'},
                {nombre: 'Clientes', icon: 'usuarios', url: '#/clientes'},
                {nombre: 'Vendedores', icon: 'usuarios', url: '#/vendedores'}
            ];

            vm.irMenu = function (menu, event) {
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