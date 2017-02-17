(function () {
    'use strict';
    angular.module("AppPublica", ['ngMaterial']).config(function ($mdThemingProvider, $mdIconProvider) {

        var cyanTextoBlanco = $mdThemingProvider.extendPalette('cyan', {
            'contrastDefaultColor': 'light'
        });
        $mdThemingProvider.definePalette('cyanTextoBlanco', cyanTextoBlanco);
        $mdThemingProvider.theme('default').primaryPalette('cyanTextoBlanco');
        $mdThemingProvider.theme('default').accentPalette('yellow');

        $mdIconProvider
                .icon("cerrar_atras", "recursos-cliente/publico/svg/cerrar_24px.svg", 24)
                .icon("buscar", "recursos-cliente/publico/svg/buscar_24px.svg", 24);

    }).controller("IndexController", function (ProductosFactory, $mdDialog, Carrito) {
        var sideNav = new SideNav();
        var vm = this;
        vm.tieneProductosEnElCarrito = false;

        vm.cargarProductos = function (idCategoria) {
            vm.productos = [];
            $(".spinner").css({display: ""});
            vm.idCat = idCategoria;
            ProductosFactory.leerTodo(idCategoria)
                    .then(function (data) {
                        vm.productos = data;
                        $(".spinner").css({display: "none"});
                    }).catch(function (error) {
                console.log(error);
            });
        }

        vm.verDialogo = function (producto) {
            producto = Carrito.buscarProducto(producto);
            $mdDialog.show({
                controller: AgregarProducto,
                controllerAs: "vm",
                template:
                        '<md-dialog aria-label="Agregar">' +
                        '<md-toolbar>' +
                        '<div class="md-toolbar-tools">' +
                        '<h3>¿Cuántos?</h2>' +
                        '<span flex></span>' +
                        '<md-button aria-label="b1" class="md-icon-button" ng-click="vm.cancelar()">' +
                        '<p class="material-icons">close</p>' +
                        '</md-button>' +
                        '</div>' +
                        '</md-toolbar>' +
                        '<md-dialog-content>' +
                        '<div class="md-dialog-content" style="margin-top:30px;">' +
                        '<md-slider id="_slider" md-discrete ng-model="vm.cantidad" min="0" max="{{vm.stock < 10 ? vm.stock : 10}}" step="1" aria-label="can" class="md-primary _md-active"></md-slider>' +
                        '</div>' +
                        '</md-dialog-content>' +
                        '<md-dialog-actions layout="row">' +
                        '<md-button aria-label="b2" ng-click="vm.ok()">ok</md-button>' +
                        '</md-dialog-actions>' +
                        '</md-dialog>',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                locals: {
                    producto: producto//Se envía producto a controlador AgregarProducto
                }
            }).then(function (r) {
                if (r.cantidad > 0) {
                    mostrarSnackBar('Se agregó ' + r.cantidad + ' ' + r.nombre + ' a la compra.');
                } else if (r.elimino) {
                    mostrarSnackBar('Se ha eliminado el producto de la compra.');
                }
                /*Abrir snackBar*/
                function mostrarSnackBar(mensaje) {
                    var snackbarContainer = document.querySelector('#demo-snackbar-example');
                    var data = {
                        message: mensaje,
                        timeout: 2000,
                        actionHandler: function () {
                        },
                        actionText: 'Listo'
                    };
                    if (screen.width < 479) {
                        document.querySelector(".mdl-button--floating-action").style.bottom = "80px";
                    }
                    snackbarContainer.MaterialSnackbar.showSnackbar(data);
                }

            }, function () {
            });
        };

        /*Controlador mdDialog para seleccionar cantidad*/
        function AgregarProducto($mdDialog, producto, Carrito) {
            var vm = this;
            if (producto.cantidad != null) {
                vm.cantidad = producto.cantidad;
            } else {
                vm.cantidad = 0;
            }
            vm.stock = producto.stock;
            vm.cancelar = function () {
                $mdDialog.cancel();
            };
            vm.ok = function () {
                producto.cantidad = vm.cantidad;
                var elimino = Carrito.agregarProducto(producto);
                $mdDialog.hide({nombre: producto.nombre, cantidad: vm.cantidad, elimino: elimino});
            };
        }
        ;

        vm.buscarProductos = function (busqueda) {
            var __busqueda = document.getElementById("busqueda");
            __busqueda.blur();
            vm.productos = [];
            if (vm.idCat == 0) {
                vm.idCat = null;
            }
            vm.productos = [];
            $(".spinner").css({display: ""});
            ProductosFactory.buscarProductos(busqueda)
                    .then(function (data) {
                        vm.productos = data;
                        $(".spinner").css({display: "none"});
                        if (vm.productos.length == 0) {
                            $("#resultadoBusqueda").css({display: ""});
                        }
                    }).catch(function (error) {
                console.log(error);
            });
        };

        vm.volverAtras = function () {
            if (vm.idCat == null) {
                vm.idCat = 0;
            }
            document.getElementById("busqueda").blur();
            $("#resultadoBusqueda").css({display: "none"});
            vm.busqueda = null;
            vm.cargarProductos(vm.idCat);
        };

        angular.element(document).ready(function () {
            vm.cargarProductos(0);
        });

        vm.abrirSideNavConBotonCarrito = function (e) {
            if (Carrito.hayCarrito()) {
                vm.tieneProductosEnElCarrito = true;
                sideNav.showSideNav();                
            } else {
                vm.tieneProductosEnElCarrito = false;
                $mdDialog.show(
                        $mdDialog.alert()
                        .parent(angular.element(document.body))
                        .clickOutsideToClose(true)
                        .title('Mensaje')
                        .textContent('Aún no hay productos en el carrito')
                        .ariaLabel('Alert')
                        .ok('Ok!')
                        .targetEvent(e)
                        );
            }
        };

    }).factory('ProductosFactory', function ($http, $q) {
        return {
            leerTodo: leerTodo,
            buscarProductos: buscarProductos
        };

        function leerTodo(idCategoria) {
            var defered = $q.defer();
            var promise = defered.promise;

            $http.get('LeerProductosParaCatalogoServlet', {params: {idCategoria: idCategoria}})
                    .success(function (data) {
                        defered.resolve(data);
                    })
                    .error(function (err) {
                        defered.reject(err);
                    });

            return promise;
        }

        function buscarProductos(busqueda) {
            var defered = $q.defer();
            var promise = defered.promise;

            $http.get('BuscarProductosClienteServlet', {params: {busqueda: busqueda}})
                    .success(function (data) {
                        defered.resolve(data);
                    })
                    .error(function (err) {
                        defered.reject(err);
                    });

            return promise;
        }

    }).factory('Carrito', function () {

        return {
            agregarProducto: agregarProducto,
            buscarProducto: buscarProducto,
            hayCarrito: hayCarrito
        };

        function agregarProducto(producto) {
            var itemCarro =
                    {
                        idProducto: producto.idProducto,
                        nombre: producto.nombre,
                        cantidad: producto.cantidad,
                        precio: producto.precio,
                        subTotal: (producto.cantidad * producto.precio),
                        stockExcedido: false
                    };

            if (localStorage.getItem("carrito") == null) {
                if (itemCarro.cantidad > 0) {
                    var carrito = [];
                    carrito.push(itemCarro);
                    localStorage.setItem("carrito", JSON.stringify(carrito));
                }
            } else {
                var repetido = false;
                var seElimino = false;
                var carrito = JSON.parse(localStorage.getItem('carrito'));
                for (var i = 0; i < carrito.length; i++) {
                    if (carrito[i].idProducto == producto.idProducto) {
                        if (producto.cantidad == 0) {
                            carrito.splice(i, 1);
                            seElimino = true;
                            break;
                        } else {
                            carrito[i] = itemCarro;
                            repetido = true;
                            break;
                        }
                    }
                }
                if (repetido == false && seElimino == false) {
                    carrito.push(itemCarro);
                }

                if (carrito.length == 0) {
                    localStorage.removeItem("carrito");
                } else {
                    localStorage.setItem("carrito", JSON.stringify(carrito));
                }

            }
            return seElimino;
        }

        function buscarProducto(p) {
            if (localStorage.getItem("carrito") != null) {
                var i = 0;
                var carrito = JSON.parse(localStorage.getItem('carrito'));
                while (i < carrito.length) {
                    if (carrito[i].idProducto == p.idProducto) {
                        p.cantidad = carrito[i].cantidad;
                        break;
                    }
                    i++;
                }
            }
            return p;
        }

        function hayCarrito() {
            var r = false;
            if (localStorage.getItem("carrito") != null) {
                r = true;
            }
            return r;
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
    });/*.directive("mostrarOnLoad", function () {
     return {
     link: function (scope, element) {
     element.on("load", function () {
     scope.$apply(function () {
     scope.producto.show = true;
     });
     });
     }
     };
     })*/
    ;
})();

