<!DOCTYPE html>
<html lang="es" >
    <head>
        <noscript>
        <meta http-equiv="refresh" content="0;url=error/no-script.html">
        </noscript>
        <title>Panel de control</title>
        <link rel="shortcut icon" href="img/icas.ico" />
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!--<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Roboto:400,700'>-->
        <link rel='stylesheet' href='font/roboto.css'>
        <link rel="stylesheet" href="css/angular-material.min.css">
        <link rel="stylesheet" href="css/md-data-table.min.css">
        <link rel="stylesheet" href="css/lf-ng-md-file-input.min.css">
        <link rel="stylesheet" href="recursos-admin/assets/css/style.css">

    </head>

    <body ng-app="AppAdmin"
          layout="row"
          ng-controller="AdminController as vm"
          ng-cloak
          ng-bloquear-enter
          ng-init="vm.obtenerRut('${usuario.rut}')">


    <md-sidenav layout="column"
                class="md-whiteframe-z2"
                md-component-id="left"
                md-is-locked-open="$mdMedia('gt-sm')" ng-cloack ng-click="vm.toggleSideNav()">
        <!--Cabecera de SideNav-->
        <md-toolbar ng-cloak class="tool-bar-sidenav">
            <span flex></span>
            <div layout="row" layout-align="start center">
                <md-icon class="md-avatar" md-svg-icon="clienteCirculo"></md-icon>
                <span flex></span>
            </div>
            <span flex></span>
            <div>
                <!--Etiquetas de java signo_peso{algo}-->
                <div class="md-body-2" style="font-weight: 800;">${usuario.nombre} ${usuario.apellido} (${usuario.rut})</div>
                <div class="md-body-1" style="font-weight: 800;">${usuario.rol} </div>
            </div>
        </md-toolbar>

        <md-list ng-cloak >
            <md-list-item ng-repeat="item in vm.menu" href="{{item.url}}">
                <md-icon md-svg-icon="{{item.icon}}" aria-label="panel"></md-icon>
                <p>{{item.nombre}}</p>
            </md-list-item>
            <!--<md-divider></md-divider>
            <md-subheader class="md-no-sticky">Otros</md-subheader>
            <md-list-item >
                <md-icon md-svg-icon="" aria-label=""></md-icon>
                <p>Otros</p>
            </md-list-item>-->
        </md-list>
    </md-sidenav>


    <div flex layout="column">
        <!--Boton de menu-->
        <md-toolbar class="animate-show md-whiteframe-z1">
            <div class="md-toolbar-tools">
                <h3></h3>
                <md-button hide-gt-sm class="md-icon-button" ng-click="vm.toggleSideNav()" aria-label="Menu">
                    <md-icon md-svg-icon="menu"></md-icon>
                </md-button>
                <span flex></span>
                <md-button aria-label="Ver comandas" class="md-icon-button" href="VerComandaServlet">
                    <md-icon md-svg-icon="comanda" aria-label="Comanda"></md-icon>
                </md-button>
                <md-menu md-position-mode="target-right target">
                    <md-button aria-label="Configuración" class="md-icon-button" ng-click="$mdOpenMenu($event)">
                        <md-icon md-svg-icon="mas" aria-label="Más"></md-icon>
                    </md-button>
                    <md-menu-content width="2">
                        <md-menu-item>
                            <md-button href="CerrarSesionServlet">
                                <md-icon md-menu-align-target md-svg-icon="salir"></md-icon>
                                Cerrar sesión
                            </md-button>
                        </md-menu-item>
                        <md-menu-item>
                            <md-button ng-click="vm.abrirPantallaCambiarClave()">
                                <md-icon md-menu-align-target md-svg-icon="candado"></md-icon>
                                Cambiar clave de acceso
                            </md-button>
                        </md-menu-item>
                    </md-menu-content>
                </md-menu>
            </div>
        </md-toolbar>
        <md-content flex layout="column" id="content" class="md-padding">
            <ng-view></ng-view>
        </md-content>
    </div>

    <!-- Angular Material requires Angular.js Libraries -->
    <script src="js/angular.min.js"></script>
    <script src="js/angular-animate.min.js"></script>
    <script src="js/angular-aria.min.js"></script>
    <script src="js/angular-messages.min.js"></script>
    <script src="js/angular-route.min.js"></script>
    <script src="js/md-data-table.min.js"></script>
    <script src="js/angular-material.min.js"></script>
    <script src="js/lf-ng-md-file-input.min.js"></script>
    <script src="js/verificar-rut.js"></script>

    <script src="recursos-admin/js/App.js" ></script>
    <script src="recursos-admin/js/AdminController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/VerificarRutService.js" charset="utf-8"></script>
    <script src="recursos-admin/js/CompartirRutService.js" charset="utf-8"></script>

    <script src="recursos-admin/js/venta/RealizarVentaController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/venta/VentaController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/venta/VerDetalleController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/venta/CompartirVentaService.js" charset="utf-8"></script>
    <script src="recursos-admin/js/venta/LeerVentasFactory.js" charset="utf-8"></script>


    <script src="recursos-admin/js/producto/ProductosController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/producto/LeerProductosFactory.js" charset="utf-8"></script>
    <script src="recursos-admin/js/producto/BuscarProductoFactory.js" charset="utf-8"></script>
    <script src="recursos-admin/js/producto/CrearProductoController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/producto/EditarProductoController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/producto/CompartirProductoService.js" charset="utf-8"></script>
    <script src="recursos-admin/js/producto/AgregarImagenController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/producto/VerImagenController.js" charset="utf-8"></script>


    <script src="recursos-admin/js/categoria/CategoriasController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/categoria/LeerCategoriasFactory.js" charset="utf-8"></script>
    <script src="recursos-admin/js/categoria/CrearCategoriaController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/categoria/EditarCategoriaController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/categoria/CompartirCategoriaService.js" charset="utf-8"></script>


    <script src="recursos-admin/js/cliente/ClienteController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/cliente/LeerClientesFactory.js" charset="utf-8"></script>
    <script src="recursos-admin/js/cliente/CrearClienteController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/cliente/CargarCuentaController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/cliente/CompartirClienteService.js" charset="utf-8"></script>
    <script src="recursos-admin/js/cliente/EditarClienteController.js" charset="utf-8"></script>


    <script src="recursos-admin/js/empleado/EmpleadoController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/empleado/LeerEmpleadosFactory.js" charset="utf-8"></script>
    <script src="recursos-admin/js/empleado/CrearEmpleadoController.js" charset="utf-8"></script>    
    <script src="recursos-admin/js/empleado/CompartirEmpleadoService.js" charset="utf-8"></script>
    <script src="recursos-admin/js/empleado/EditarEmpleadoController.js" charset="utf-8"></script>
    <script src="recursos-admin/js/empleado/CambiarClaveAccesoController.js" charset="utf-8"></script>

    <script src="recursos-admin/js/Directivas.js" charset="utf-8"></script>    

</body>
</html>
