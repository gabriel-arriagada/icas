angular.module('AppAdmin').controller('EditarClienteController', function ($mdDialog, CompartirClienteService){
    var vm = this;
    vm.cliente = {};
    var clienteCompartido = CompartirClienteService.getProperty();
    
    vm.cliente.accion = 200;
    vm.cliente.rut = clienteCompartido.rut;
    vm.cliente.rol = clienteCompartido.rol;
    vm.cliente.nombre = clienteCompartido.nombre;
    vm.cliente.apellido = clienteCompartido.apellido;
    vm.cliente.correo = clienteCompartido.correo;
    vm.cliente.vigente = clienteCompartido.vigente;
            
    vm.cerrarDialogo = function() {
        $mdDialog.cancel();
    };

    vm.ejecutarAccion = function() {
        $mdDialog.hide(vm.cliente);
    };
    
});
