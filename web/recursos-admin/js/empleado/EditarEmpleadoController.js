angular.module('AppAdmin').controller('EditarEmpleadoController', function ($mdDialog, CompartirEmpleadoService){
    var vm = this;
    vm.empleado = {};
    var empleadoCompartido = CompartirEmpleadoService.getProperty();
    
    vm.empleado.accion = 200;
    vm.empleado.rut = empleadoCompartido.rut;
    vm.empleado.rol = empleadoCompartido.rol;
    vm.empleado.nombre = empleadoCompartido.nombre;
    vm.empleado.apellido = empleadoCompartido.apellido;
    vm.empleado.correo = empleadoCompartido.correo;
    vm.empleado.vigente = empleadoCompartido.vigente;
            
    vm.cerrarDialogo = function() {
        $mdDialog.cancel();
    };

    vm.ejecutarAccion = function() {
        $mdDialog.hide(vm.empleado);
    };
    
});
