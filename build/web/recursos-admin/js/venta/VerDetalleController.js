angular.module('AppAdmin').controller('VerDetalleController', function ($mdDialog, CompartirVentaService, $http){
    
    var vm = this;
    vm.detalles = [];
    vm.idVenta = CompartirVentaService.getProperty();
    leerDetalles();
    
    vm.cerrarDialogo = function (){
        $mdDialog.cancel();
    };
    
    function leerDetalles(){        
         $http.get('LeerDetalleServlet', {params: {idVenta: vm.idVenta}})
                .success(function(data) {
                    vm.detalles = data;
                })
                .error(function(err) {
                    
                });
    }
    
});