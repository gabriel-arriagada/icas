(function () {
    'use strict';
    angular.module("AppAdmin").controller("ProductosVendedorController", function ($mdDialog, $http, $mdToast, LeerProductosFactory) {
        var vm = this;

        var vm = this;
        leerProductos();
        vm.seleccionado = [];
        vm.productos = [];
        var productoSeleccionado;
        vm.modes = [];

        vm.itemSeleccionado = function (item) {
            productoSeleccionado = item;
        };

        vm.query = {
            order: 'nombreProducto',
            limit: 5,
            page: 1
        };

        function mostrarToast(mensaje) {
            $mdToast.show(
                    $mdToast.simple()
                    .textContent(mensaje)
                    .position('top right')
                    .hideDelay(6000)
                    );
        }

        function leerProductos() {
            vm.promise = LeerProductosFactory.leerParaVendedor()
                    .then(function (data) {
                        vm.productos = data;
                    })
                    .catch(function (error) {
                        mostrarToast("Ha ocurrido un error al cargar los datos!");
                    });
        }

        function AumentarStockController(producto, $mdDialog, $http, $timeout) {
            var vm = this;
            vm.cambiandoStock = false;
            vm.textoError = null;
            vm.producto = producto;
            vm.cancelar = function () {
                $mdDialog.cancel();
            };
            vm.guardarCambios = function (form) {
                if (form.$valid) {
                    vm.cambiandoStock = true;
                    $http.put("AumentarStockProductoServlet", {idProducto: vm.producto.idProducto, stock: vm.nuevoStock})
                            .then(function () {
                                $mdDialog.hide({exito: true, stock: vm.nuevoStock});
                            }, function () {
                                vm.cambiandoStock = false;
                                vm.textoError = "Error, la operación no se realizó.";
                                $timeout(function () {
                                    vm.textoError = null;
                                }, 4000);
                            });
                }
            };
        }

        vm.cambiarStock = function () {
            $mdDialog.show({
                controller: AumentarStockController,
                controllerAs: "vm",
                template:
                        '<md-dialog aria-label="Aumentar stock">' +
                        '<form name="formStock" ng-submit="vm.guardarCambios(formStock)">' +
                        '<md-toolbar>' +
                        '<div class="md-toolbar-tools">' +
                        '<h3>Aumenta el stock del producto "{{vm.producto.nombre}}"</h2>' +
                        '<span flex></span>' +
                        '<md-button aria-label="b1" class="md-icon-button" ng-click="vm.cancelar()">' +
                        '<md-icon md-svg-icon="cerrar"></md-icon>' +
                        '</md-button>' +
                        '</div>' +
                        '</md-toolbar>' +
                        '<md-dialog-content>' +
                        '<div class="md-dialog-content" layout="column">' +
                        '<p>Stock actual: {{vm.producto.stock}} unidades.</p>' +
                        '<md-input-container>' +
                        '<label>Nuevo stock</label>' +
                        '<input name="nuevoStock" ng-model="vm.nuevoStock" min="{{vm.producto.stock + 1}}" required maxlength="10" type="number">' +
                        '<div ng-messages="formStock.nuevoStock.$error" md-auto-hide="false" ng-show="formStock.nuevoStock.$touched" ng-messages-multiple>' +
                        '<div ng-message="required">Debes ingresar stock para el producto</div>' +
                        '<div ng-message="min">El nuevo stock debe ser superior al stock actual del producto</div>' +
                        '</div>' +
                        '</md-input-container>' +
                        '</div>' +
                        '</md-dialog-content>' +
                        '<md-dialog-actions layout="row">' +
                        '<md-progress-circular ng-if="vm.cambiandoStock && vm.textoError == null" md-mode="indeterminate" md-diameter="20"></md-progress-circular>' +
                        '<md-button ng-if="!vm.cambiandoStock && vm.textoError == null" aria-label="b12" ng-click="vm.cancelar()">cancelar</md-button>' +
                        '<md-button ng-if="vm.textoError == null" class="md-primary md-raised" aria-label="b2" ng-disabled="vm.cambiandoStock" type="submit">guardar cambios</md-button>' +
                        '<h5 ng-if="vm.textoError != null" style="color: red;">{{vm.textoError}}</h5>' +
                        '</md-dialog-actions>' +
                        '</form>' +
                        '</md-dialog>',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                locals: {
                    producto: productoSeleccionado
                }
            }).then(function (resultado) {
                if (resultado.exito) {
                    mostrarToast("El stock del producto ha sido aumentado!");
                    productoSeleccionado.stock = resultado.stock;
                }
            });
        };

    });
})();