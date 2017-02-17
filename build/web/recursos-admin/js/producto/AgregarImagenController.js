angular.module('AppAdmin').controller('AgregarImagenController', function($scope, $mdDialog, CompartirProductoService, $http) {

    var vm = this;

    vm.mostrarCarga = false;
            
    var idProducto = CompartirProductoService.getProperty();

    vm.cerrarDialogo = function() {
        $mdDialog.cancel();
    };

    vm.ejecutarAccion = function() {
        vm.mostrarCarga = true;
        var formData = new FormData();
        formData.append('imagen', vm.files[0].lfFile); 
        formData.append('idProducto', idProducto);
        $http.post('GuardarImagenServlet', formData, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(function(resultado) {
            $mdDialog.hide(true);
        }, function(error) {
            $mdDialog.hide(false);
        });
    };
});

