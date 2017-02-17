angular.module('AppAdmin').controller('VerImagenController', function($mdDialog, CompartirProductoService) {

    var vm = this;
    
    vm.urlImagen = CompartirProductoService.getProperty();

    vm.cerrarDialogo = function() {
        $mdDialog.cancel();
    };

    vm.ejecutarAccion = function() {
        $mdDialog.hide();
    };
});

