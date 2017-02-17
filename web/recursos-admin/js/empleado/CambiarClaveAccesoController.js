(function () {
    'use strict';
    angular.module("AppAdmin").controller("CambiarClaveAccesoController", function ($mdDialog, $http, $timeout) {
        var vm = this;
        vm.claves = {};

        vm.cancelar = function () {
            $mdDialog.cancel();
        };

        vm.listo = function (){
            $mdDialog.hide();
        };

        vm.claves = {};
        vm.cambiandoClave = false;
        vm.exito = false;
        vm.textoError = null;

        vm.cambiarClave = function (form) {
            if (form.$valid) {
                document.getElementById("nuevaClaveB").blur();
                vm.cambiandoClave = true;
                $http.put('CambiarClaveEmpleadoServlet', vm.claves)
                        .then(function () {
                            vm.cambiandoClave = false;
                            vm.exito = true;                                                        
                        }, function (error) {
                            vm.cambiandoClave = false;
                            vm.exito = false;
                            switch (error.status) {
                                case 400:
                                    vm.textoError = "La clave actual NO es correcta";
                                    vm.claves.claveActual = "";
                                    document.getElementById("claveActual").focus();
                                    break;
                                case 401:
                                    vm.textoError = "Error, no tienes autorización para realizar esta acción.";
                                    break;
                                case 409:
                                    vm.textoError = "Ha ocurrido un error al cambiar la clave, inténtalo de nuevo";
                                    break;
                                case 500:
                                    vm.textoError = "Ha ocurrido un error al cambiar la clave, inténtalo de nuevo";
                                    break;
                            }
                            
                            $timeout(function (){
                                vm.textoError = null;
                            }, 4000);
                            
                        });               
            }
        };

    });
})();


