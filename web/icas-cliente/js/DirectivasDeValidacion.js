(function () {
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
            .directive('validaUsuario', function ($q, $http) {
                return {
                    restrict: 'A',
                    require: 'ngModel',
                    link: function ($scope, $element, $attrs, ngModel) {
                        ngModel.$asyncValidators.usuarioExiste = function (rut) {
                            return $http.get('../BuscarClientePorRutServlet', {params: {rut: rut}})
                                    .then(function resolved() {
                                        return $q.reject();
                                    }, function rejected() {
                                        return true;
                                    });

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