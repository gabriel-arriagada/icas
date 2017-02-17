(function() {
    'use strict';
    angular.module('LogInApp', ['ngMaterial', 'ngMessages', 'ngRoute'])
            .config(function($mdThemingProvider, $mdIconProvider, $routeProvider) {
                var cyanTextoBlanco = $mdThemingProvider.extendPalette('cyan', {
                    'contrastDefaultColor': 'light'
                });
                $mdThemingProvider.definePalette('cyanTextoBlanco', cyanTextoBlanco);
                $mdThemingProvider.theme('default')
                        .primaryPalette('cyanTextoBlanco');

                $mdIconProvider
                        .icon("usuario", "../icas-cliente/svg/ic_account_circle_24px.svg", 24)
                        .icon("cerrar", "../icas-cliente/svg/ic_close_24px.svg", 24);

                $routeProvider
                        .when("/registrar", {
                            controller: "RegistrarController",
                            controllerAs: "vm",
                            templateUrl: "../icas-cliente/vistas/registro.html"
                        })
                        .when("/autenticacion", {
                            controller: "LogInController",
                            controllerAs: "vm",
                            templateUrl: "../icas-cliente/vistas/autenticacion.html"
                        })
                        .otherwise({
                            controller: "LogInController",
                            controllerAs: "vm",
                            templateUrl: "../icas-cliente/vistas/autenticacion.html"
                        });
            }).controller("PantallaIncialController", function (IrAlCarrito){
                var vm = this;
                
                vm.inicio = function (ir){
                    if(ir !=  null){
                        IrAlCarrito.setIr(ir);
                    }
                };
                
            }).service("IrAlCarrito", function (){
                var ir;
                
                return {
                    setIr: setIr,
                    getIr: getIr
                };
                
                function setIr(_ir){
                    ir = _ir;
                }
                
                function getIr(){
                    return ir;
                }
            });
})();