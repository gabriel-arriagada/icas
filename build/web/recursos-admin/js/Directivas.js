(function () {
    angular.module('AppAdmin').directive('ngBloquearEnter', function () {
        return {
            restrict: 'A',
            link: function (scope, elemento)
            {
                elemento.bind("keydown keypress", function (event)
                {
                    if (event.which === 13) {
                        event.preventDefault();
                    }
                });
            }
        };
    }).directive('ngLeerCodigoBarra', function ($interval) {
        return {
            restrict: 'E',
            scope: {
                escanearInfo: '=info',
                retornarCodigo: '=method'
            },
            link: function (scope, elemento)
            {
                var inicio = false;
                var timer = null;
                var contador = 0;
                var codigo = [];

                function iniciar() {
                    timer = $interval(function () {
                        contador = contador + 1;
                        if (contador == 40) {
                            $interval.cancel(timer);
                            contador = 0;
                            inicio = false;
                            timer = null;
                            scope.retornarCodigo(codigo.join(""));
                            codigo = [];
                        }
                    }, 10);
                }

                elemento.bind("keypress", function (event) {
                    if (scope.escanearInfo) {
                        if (event.which >= 48 && event.which <= 57) {
                            if (inicio == false) {
                                iniciar();
                                inicio = true;
                            }
                            codigo.push(String.fromCharCode(event.which));
                        } else {
                            event.preventDefault();
                        }
                    }
                });

            }
        };
    }).directive('validaRut', function (VerificarRutService) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function ($scope, $element, $attrs, ngModel) {
                ngModel.$validators.rutInvalido = function (rut) {
                    return VerificarRutService.verificar(rut);
                };
            }
        };
    }).directive('validaCliente', function ($q, $http) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function ($scope, $element, $attrs, ngModel) {
                ngModel.$asyncValidators.clienteNoExiste = function (rut) {
                    if (rut != null) {
                        return $http.get('BuscarClientePorRutServlet', {params: {rut: rut}}).then(
                                function () {
                                    return true;
                                }, function () {
                            return $q.reject();
                        });
                    } else {
                        return $q(function (resolve, reject) {
                            setTimeout(function () {
                                resolve();
                            }, 100);
                        });
                    }
                }
            }
        };
    }).directive('buscarProducto', function ($q, $http) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function ($scope, $element, $attrs, ngModel) {
                ngModel.$asyncValidators.productoExiste = function (idProducto) {
                    return $http.get('BuscarProductoServlet', {params: {idProducto: idProducto, cantidad: 0}})
                            .then(function () {
                                return $q.reject();
                            }, function () {
                                return true;
                            });
                }
            }
        };
    }).directive('buscarCliente', function ($q, $http) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function ($scope, $element, $attrs, ngModel) {
                ngModel.$asyncValidators.clienteExiste = function (rut) {
                    return $http.get('BuscarClientePorRutServlet', {params: {rut: rut}})
                            .then(function resolved() {
                                return $q.reject();
                            }, function rejected() {
                                return true;
                            });

                };
            }
        };
    }).directive('buscarEmpleado', function ($q, $http) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function ($scope, $element, $attrs, ngModel) {
                ngModel.$asyncValidators.empleadoExiste = function (rut) {
                    return $http.get('BuscarEmpleadoPorRutServlet', {params: {rut: rut}})
                            .then(function resolved() {
                                return $q.reject();
                            }, function rejected() {
                                return true;
                            });

                };
            }
        };
    }).directive("noClickDerecho", function () {
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
