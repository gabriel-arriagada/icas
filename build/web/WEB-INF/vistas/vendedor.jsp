<!DOCTYPE html>
<html lang="en" >
    <head>
        <noscript>
        <meta http-equiv="refresh" content="0;url=error/no-script.html">
        </noscript>
        <title>Panel de ventas</title>
        <link rel="shortcut icon" href="img/icas.ico" />
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel='stylesheet' href='font/roboto.css'>
        <link rel="stylesheet" href="css/angular-material.min.css">
        <link rel="stylesheet" href="css/md-data-table.min.css">
        <link rel="stylesheet" href="css/lf-ng-md-file-input.min.css">
        <link rel="stylesheet" href="recursos-admin/assets/css/style.css">
        <style>
            .__container{
                margin-top: 1%;
            }
            @media(min-width:700px){
                .__container{
                    padding-left: 12%;
                    padding-right: 12%;
                }
            }
        </style>
    </head>

    <body no-click-derecho ng-app="AppAdmin" 
          layout="row" 
          ng-controller="VendedorController as vm"            
          ng-cloak 
          ng-bloquear-enter
          ng-init="vm.obtenerRut('${usuario.rut}')">

        <div flex layout="column" class="relative" layout-fill>
            <!--Boton de menu-->
            <md-toolbar class="animate-show md-medium-tall md-whiteframe-10dp">
                <div class="md-toolbar-tools">
                    <h3>Bienvenido(a) ${usuario.nombre} ${usuario.apellido}</h3>
                    <span flex></span>   
                    <md-button aria-label="Ver comandas" class="md-icon-button" href="VerComandaServlet">
                        <md-icon  md-svg-icon="comanda" aria-label="Comanda"></md-icon>
                    </md-button>
                    <md-menu md-position-mode="target-right target">                         
                        <md-button aria-label="Configuración" class="md-icon-button" ng-click="$mdOpenMenu()">
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
                <span flex></span>
                <md-tabs md-stretch-tabs="always">
                    <md-tab ng-repeat="tab in vm.tabsTexto" md-on-select="vm.cambiarTab($index)">
                        {{tab.texto}}
                    </md-tab>
                </md-tabs>
            </md-toolbar>
            <md-content flex layout="column" id="content" >
                <div class="__container">
                    <ng-view></ng-view>
                </div>                
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
        <script src="recursos-admin/js/VerificarRutService.js" charset="utf-8"></script>
        <script src="recursos-admin/js/CompartirRutService.js" charset="utf-8"></script>    
        <script src="recursos-admin/js/VendedorController.js" charset="utf-8"></script>    
        <script src="recursos-admin/js/producto/BuscarProductoFactory.js" charset="utf-8"></script>
        <script src="recursos-admin/js/cliente/CargarCuentaController.js" charset="utf-8"></script>
        <script src="recursos-admin/js/venta/RealizarVentaController.js" charset="utf-8"></script>
        <script src="recursos-admin/js/venta/VentaVendedorController.js" charset="utf-8"></script>
        <script src="recursos-admin/js/venta/VerDetalleController.js" charset="utf-8"></script>
        <script src="recursos-admin/js/venta/CompartirVentaService.js" charset="utf-8"></script>
        <script src="recursos-admin/js/venta/LeerVentasFactory.js" charset="utf-8"></script>               
        <script src="recursos-admin/js/producto/LeerProductosFactory.js" charset="utf-8"></script>
        <script src="recursos-admin/js/producto/ProductosVendedorController.js" charset="utf-8"></script>        
        <script src="recursos-admin/js/empleado/CambiarClaveAccesoController.js" charset="utf-8"></script>        
        <script src="recursos-admin/js/Directivas.js" charset="utf-8"></script>
    </body>
</html>