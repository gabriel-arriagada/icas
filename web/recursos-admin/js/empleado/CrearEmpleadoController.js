angular.module('AppAdmin').controller('CrearEmpleadoController', function($mdDialog, verificar, $route) {
    var vm = this;    
    
    var datosDeCreación = $route.current.$$route.paramExample;        
    
    vm.empleado = {};    
    vm.empleado.idRol = datosDeCreación.idRol;
    
    
    vm.svgVer = "nover";
    vm.typeClave = "password";
    vm.verClave = function (){
        if(vm.svgVer == "ver"){
            vm.svgVer = "nover";
            vm.typeClave = "password";
        }else{
            vm.svgVer = "ver";
            vm.typeClave = "text";
        }        
    };
    
    vm.generarClave = function (){
        var mayuscula = 65;
        //var minuscula = 97;
        var indice = 0;
        var clave = "";
        while(indice < 5){
            var digito = Math.floor(Math.random() * (10 - 1)) + 1;
            if(digito > 2 && digito < 5){
                var digitoMayuscula = mayuscula + Math.floor(Math.random() * (25 - 1)) + 1;
                clave = clave + String.fromCharCode(digitoMayuscula); 
            }else{
                clave = clave + digito.toString();
            }
            indice = indice + 1;
        }      
        vm.empleado.clave = clave;
    };
    
    vm.validarRut = function()
    {
        var valido = verificar.validarRut(vm.empleado.rut);
        if (valido != "ok") {
            vm.empleado.rut = "";
        }
    };

    vm.cerrarDialogo = function() {
        $mdDialog.cancel();
    };

    vm.ejecutarAccion = function() {
        $mdDialog.hide(vm.empleado);
    };
});