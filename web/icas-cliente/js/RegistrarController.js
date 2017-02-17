(function() {
    'use strict';
    angular.module('LogInApp')
            .controller('RegistrarController', function(RegistrarFactory, $mdDialog) {
                var vm = this;
                vm.usuario = {};
                vm.usuarioCreado = false;              
                
                vm.cancelar = function() {
                    $mdDialog.cancel();
                };

                vm.enviarRegistro = function(form) {
                    if(form.$valid){
                        vm.creandoUsuario = true;
                        RegistrarFactory.setUsuario(vm.usuario)
                                .then(function (){
                                    vm.creandoUsuario = false;
                                    vm.usuarioCreado = true;
                                    vm.textoFinal = "¡Tu cuenta fue creada!";
                                })
                                .catch(function (error){
                                    console.log(error);
                                    vm.textoFinal = "¡Ha ocurrido un error!, la cuenta NO fue creada...";
                                });
                    }
                };

            });
})();


