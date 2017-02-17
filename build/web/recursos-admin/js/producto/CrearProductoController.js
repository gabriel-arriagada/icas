angular.module('AppAdmin').controller('CrearProductoController', function($mdDialog, LeerCategoriasFactory) {
    var vm = this;
    vm.producto = {};
    vm.categorias = [];

    LeerCategoriasFactory.leerTodo()
            .then(function(data) {
                vm.categorias = data;
            })
            .catch(function(err) {
                mostrarToast("Ha ocurrido un error al cargar los datos!");
            });


    vm.cerrarDialogo = function() {
        $mdDialog.cancel();
    };

    function cajasLlenas() {
        var retorno = false;
        if (vm.producto.idProducto != null && vm.producto.idCategoria != null &&
                vm.producto.nombre != null && vm.producto.stock != null && vm.producto.precioCompra != null &&
                vm.producto.precioVenta != null)
        {
            retorno = true;
        }
        return retorno;
    }

    vm.ejecutarAccion = function() {
        if (cajasLlenas() == true) {
            $mdDialog.hide(vm.producto);
        }
    };

});