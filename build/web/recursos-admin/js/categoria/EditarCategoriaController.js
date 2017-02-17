angular.module('AppAdmin').controller('EditarCategoriaController', function($mdDialog, CompartirCategoriaService) {

    var vm = this;
    var categoriaCompartida = CompartirCategoriaService.getProperty();
    vm.idCategoria = categoriaCompartida.idCategoria;
    vm.categoria = categoriaCompartida.categoria;

    vm.cerrarDialogo = function() {
        $mdDialog.cancel();
    };

    vm.ejecutarAccion = function() {
        var categoria = {idCategoria: vm.idCategoria, categoria: vm.categoria};
        if (vm.categoria != null && vm.idCategoria != null)
        {
            $mdDialog.hide(categoria);
        }
    };

});