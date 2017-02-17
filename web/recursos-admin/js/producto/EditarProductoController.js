(function () {
    'use strict';
    angular.module('AppAdmin').controller('EditarProductoController', function ($mdDialog, CompartirProductoService, LeerCategoriasFactory) {

        var vm = this;

        vm.producto = {};
        vm.categorias = [];
        var productoCompartido = CompartirProductoService.getProperty();
        vm.producto.accion = 200;
        vm.producto.idProducto = productoCompartido.idProducto;
        vm.producto.idCategoria;
        vm.categoria = productoCompartido.categoria;
        vm.producto.nombre = productoCompartido.nombre;
        vm.producto.stock = productoCompartido.stock;
        vm.producto.precioCompra = productoCompartido.precioCompra;
        vm.producto.precioVenta = productoCompartido.precioVenta;

        LeerCategoriasFactory.leerTodo()
                .then(function (data) {
                    vm.categorias = data;                    
                })
                .catch(function (err) {

                });
                
        vm.cerrarDialogo = function () {
            $mdDialog.cancel();
        };

        vm.ejecutarAccion = function () {
            if (vm.producto.idProducto != null && vm.producto.idCategoria != null &&
                    vm.producto.nombre != null && vm.producto.stock != null &&
                    vm.producto.precioCompra != null && vm.producto.precioVenta != null)
            {
                $mdDialog.hide(vm.producto);
            }
        };

    });
})();