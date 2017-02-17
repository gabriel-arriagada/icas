
angular.module('AppAdmin', ['ngMaterial', 'ngRoute', 'md.data.table', 'ngMessages', 'lfNgMdFileInput', 'verificaRut'])
        .config(function ($mdThemingProvider, $mdIconProvider, $routeProvider) {

            var customBlueMap = $mdThemingProvider.extendPalette('light-blue', {
                'contrastDefaultColor': 'light',
                'contrastDarkColors': ['50'],
                '50': 'ffffff'
            });
            $mdThemingProvider.definePalette('customBlue', customBlueMap);
            $mdThemingProvider.theme('default')
                    .primaryPalette('customBlue', {
                        'default': '500',
                        'hue-1': '50'
                    })
                    .accentPalette('orange');
            $mdThemingProvider.theme('input', 'default')
                    .primaryPalette('grey');

            $mdIconProvider
                    .icon("menu", "recursos-admin/assets/svg/ic_menu_24px.svg", 24)
                    .icon("avatar", "recursos-admin/assets/svg/avatar.svg", 24)
                    .icon("usuarios", "recursos-admin/assets/svg/ic_group_24px.svg", 24)
                    .icon("dashboard", "recursos-admin/assets/svg/ic_dashboard_24px.svg", 24)
                    .icon("productos", "recursos-admin/assets/svg/ic_content_paste_24px.svg", 24)
                    .icon("crear", "recursos-admin/assets/svg/ic_add_24px.svg", 24)
                    .icon("eliminar", "recursos-admin/assets/svg/ic_delete_black_24px.svg", 24)
                    .icon("editar", "recursos-admin/assets/svg/ic_mode_edit_black_24px.svg", 24)
                    .icon("cerrar", "recursos-admin/assets/svg/ic_clear_black_24px.svg", 24)
                    .icon("imagen", "recursos-admin/assets/svg/ic_insert_photo_24px.svg", 24)
                    .icon("barcode", "recursos-admin/assets/svg/ic_view_week_24px.svg", 24)
                    .icon("buscar", "recursos-admin/assets/svg/ic_search_24px.svg", 24)
                    .icon("detalle", "recursos-admin/assets/svg/ic_list_black_24px.svg", 24)
                    .icon("moneda", "recursos-admin/assets/svg/ic_attach_money_24px.svg", 24)
                    .icon("ver", "recursos-admin/assets/svg/ic_visibility_24px.svg", 24)
                    .icon("nover", "recursos-admin/assets/svg/ic_visibility_off_24px.svg", 24)
                    .icon("candado", "recursos-admin/assets/svg/ic_lock_24px.svg", 24)
                    .icon("mas", "recursos-admin/assets/svg/ic_more_vert_24px.svg", 24)
                    .icon("salir", "recursos-admin/assets/svg/ic_directions_run_24px.svg", 24)
                    .icon("cliente", "recursos-admin/assets/svg/ic_person_24px.svg", 24)
                    .icon("clienteCirculo", "recursos-admin/assets/svg/ic_account_circle_48px.svg", 48)
                    .icon("comanda", "recursos-admin/assets/svg/ic_comment_white_24px.svg", 24)
                    .icon("dedoArriba", "recursos-admin/assets/svg/ic_thumb_up_24px.svg", 24);

            $routeProvider
                    .when("/realizar-venta", {
                        controller: "RealizarVentaController",
                        controllerAs: "vm",
                        templateUrl: "recursos-admin/vistas/venta/realizar-venta.html"
                    })
                    .when("/cargar-cuenta", {
                        controller: "CargarCuentaController",
                        controllerAs: "vm",
                        templateUrl: "recursos-admin/vistas/cliente/cargar-cuenta.html"
                    })
                    .when("/ventas", {
                        controller: "VentaController",
                        controllerAs: "vm",
                        templateUrl: "recursos-admin/vistas/venta/ventas.html"
                    })
                    .when("/productos", {
                        controller: "ProductoController",
                        controllerAs: "vm",
                        templateUrl: "recursos-admin/vistas/producto/productos.html"
                    })
                    .when("/categorias", {
                        controller: "CategoriasController",
                        controllerAs: "vm",
                        templateUrl: "recursos-admin/vistas/categoria/categorias.html"
                    })
                    .when("/clientes", {
                        controller: "ClienteController",
                        controllerAs: "vm",
                        templateUrl: "recursos-admin/vistas/cliente/clientes.html"
                    })
                    .when("/vendedores", {
                        controller: "EmpleadoController",
                        controllerAs: "vm",
                        templateUrl: "recursos-admin/vistas/empleado/empleados.html",
                        paramExample: {titulo: 'Vendedores', idRol: 2}/*id rol vendedor*/
                    })
                    .when("/ventas-ven", {
                        controller: "VentaVendedorController",
                        controllerAs: "vm",
                        templateUrl: "recursos-admin/vistas/venta/ventas-vendedor.html"
                    })
                    .when("/productos-ven", {
                        controller: "ProductosVendedorController",
                        controllerAs: "vm",
                        templateUrl: "recursos-admin/vistas/producto/productos-vendedor.html"
                    })
                    .otherwise({
                        controller: "RealizarVentaController",
                        controllerAs: "vm",
                        templateUrl: "recursos-admin/vistas/venta/realizar-venta.html"
                    });

        });
