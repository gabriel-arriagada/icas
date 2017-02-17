(function () {
    angular.module("AppMisDatos", ['ngMaterial', 'ngMessages']).config(function ($mdThemingProvider, $mdIconProvider) {
        var cyanTextoBlanco = $mdThemingProvider.extendPalette('cyan', {
            'contrastDefaultColor': 'light'
        });
        $mdThemingProvider.definePalette('cyanTextoBlanco', cyanTextoBlanco);
        $mdThemingProvider.theme('default').primaryPalette('cyanTextoBlanco');

        $mdIconProvider.icon("atras", "recursos-cliente/privado/svg/atras_24px.svg", 24);

    }).controller("MisDatosController", function (Datos, $http, $timeout) {

        var vm = this;
        vm.cancelar = function () {
            window.location.href = "IndexClienteServlet";
        };
        vm.personales = {};
        angular.element(document).ready(function () {
            vm.editar = false;
            Datos.getDatos().then(function (data) {
                vm.personales = data;
            }).catch(function () {
            });
        });

        vm.editarCliente = function (form) {
            if (form.$valid) {
                vm.editandoDatos = true;
                Datos.editarCliente(vm.personales)
                        .then(function () {
                            vm.editandoDatos = false;
                            vm.datosEditados = true;
                            vm.textoDatos = "¡Datos editados!";
                            vm.editar = false;
                        })
                        .catch(function (error) {
                            vm.textoDatos = "¡Ha ocurrido un error!,";
                        });
            }
        };

        vm.claves = {};
        vm.cambiandoClave = false;
        vm.cambiarClave = function (form) {
            if (form.$valid) {
                document.getElementById("nuevaClaveB").blur();
                vm.cambiandoClave = true;
                $http.put('CambiarClaveClienteServlet', vm.claves)
                        .then(function () {
                            vm.cambiandoClave = false;
                            vm.claveFin = true;
                            vm.claves = {};
                            form.$setPristine();
                            form.$setUntouched();
                            vm.textoClave = "La clave se ha cambiado!";
                        }, function (error) {
                            vm.cambiandoClave = false;
                            vm.claveFin = true;
                            switch (error.status) {
                                case 400:
                                    vm.textoClave = "La clave actual NO es correcta";
                                    vm.claves.claveActual = "";
                                    document.getElementById("claveActual").focus();
                                    break;
                                case 409:
                                    vm.textoClave = "Ha ocurrido un error al cambiar la clave, inténtalo de nuevo";
                                    break;
                                case 500:
                                    vm.textoClave = "Ha ocurrido un error al cambiar la clave, inténtalo de nuevo";
                                    break;
                            }
                        });

                $timeout(function () {
                    vm.claveFin = false;
                }, 4000);
            }
        };

    }).factory("Datos", function ($q, $http) {
        return {
            getDatos: getDatos,
            editarCliente: editar
        };

        function getDatos() {
            var defered = $q.defer();
            var promise = defered.promise;

            $http.get('LeerDatosClienteServlet')
                    .success(function (data) {
                        defered.resolve(data);
                    })
                    .error(function (err) {
                        defered.reject(err);
                    });

            return promise;
        }

        function editar(cliente) {
            var defered = $q.defer();
            var promise = defered.promise;

            $http.put('AutoEditarClienteServlet', cliente)
                    .success(function (data) {
                        defered.resolve(data);
                    })
                    .error(function (err) {
                        defered.reject(err);
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