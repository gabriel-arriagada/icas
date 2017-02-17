(function () {
    'use strict';
    angular.module('LogInApp')
            .directive('validaRut', function (VerificarRutService) {
                return {
                    restrict: 'A',
                    require: 'ngModel',
                    link: function ($scope, $element, $attrs, ngModel) {
                        ngModel.$validators.rutInvalido = function (rut) {
                            return VerificarRutService.verificar(rut);
                        };
                    }
                };
            })
            .directive("noClickDerecho", function () {
                return {
                    restrict: 'A',
                    link: function (scope, elemento)
                    {
                        elemento.bind("contextmenu", function (e)
                        {
                            e.preventDefault();
                        });
                    }
                };
            });
})();