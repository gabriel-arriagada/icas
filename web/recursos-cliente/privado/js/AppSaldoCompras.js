(function () {
    'use strict';
    angular.module("AppSaldoCompras", ['ngMaterial', 'md.data.table']).config(function ($mdThemingProvider, $mdIconProvider) {
        var cyanTextoBlanco = $mdThemingProvider.extendPalette('cyan', {
            'contrastDefaultColor': 'light'
        });
        $mdThemingProvider.definePalette('cyanTextoBlanco', cyanTextoBlanco);
        $mdThemingProvider.theme('default').primaryPalette('cyanTextoBlanco');

        $mdIconProvider.icon("atras", "recursos-cliente/privado/svg/atras_24px.svg", 24);

    }).controller("SaldoComprasController", function (Cliente) {
        var vm = this;

        vm.cancelar = function () {
            window.location.href = "IndexClienteServlet";
        };

        vm.seleccionado = [];
        vm.query = {order: '', limit: 5, page: 1};



        angular.element(document).ready(function () {

            Cliente.getInfoCuenta()
                    .then(function (data) {
                        vm.saldo = data[0].saldo;
                        vm.fechaRecarga = data[1].fecha;
                        vm.horaRecarga = data[1].hora;
                        vm.montoRecarga = data[1].monto;
                    }).catch(function () {
            });

        });

        vm.promise = Cliente.getCompras()
                .then(function (data) {
                    vm.compras = data;
                }).catch(function () {

        });

    }).factory("Cliente", function ($http, $q) {
        return {
            getInfoCuenta: getInfoCuenta,
            getCompras: getCompras
        };

        function getInfoCuenta() {
            var defered = $q.defer();
            var promise = defered.promise;

            $http.get('LeerInfoCuentaClienteServlet')
                    .success(function (data) {
                        defered.resolve(data);
                    })
                    .error(function (err) {
                        defered.reject(err);
                    });

            return promise;
        }

        function getCompras() {
            var defered = $q.defer();
            var promise = defered.promise;
            $http.get('LeerComprasParalClienteServlet')
                    .success(function (data) {
                        defered.resolve(data);
                    })
                    .error(function (error) {
                        defered.reject(error);
                    });
            return promise;
        }

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

