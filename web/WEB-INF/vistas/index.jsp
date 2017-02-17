<%@page import="icas.dominio.Categoria"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>        
        <noscript>
        <meta http-equiv="refresh" content="0;url=error/no-script.html">
        </noscript>
        <meta name="viewport" content="width=device-width,minimum-scale=1">
        <meta name="theme-color" content="#00BCD4"/>
        <title>Catálogo</title>
        <link rel="shortcut icon" href="img/icas.ico" />
        <link href="font/material-icons.css" rel="stylesheet">
        <link rel='stylesheet' href='font/roboto.css'>
        <link rel="stylesheet" href="recursos-cliente/css/material-design-lite.css"/>
        <link href="css/angular-material.min.css" rel="stylesheet"> 
        <link href="recursos-cliente/publico/css/style.css" rel="stylesheet">    
    </head>
    <body no-click-derecho ng-app="AppPublica" ng-controller="IndexController as vm">
        <div class="wrapper-nav">            
            <div class="header-menu">
                <div layout="row">
                    <button class="js-menu-show header__menu-toggle material-icons">menu</button>                
                    <div style="padding-top: 15px; padding-left: 20px;">
                        <p style="font-size: 23px;">Cafetería</p>
                    </div>                
                </div>                
            </div>
            <div class="header-tabs">
                <ul class="tabs">                    
                    <!--Listar categorías-->
                    <li class="tab" ng-click="vm.cargarProductos(0)">Todo</li>                    
                        <%
                            if (request.getAttribute("categorias") != null) {
                                ArrayList<Categoria> categorias = (ArrayList<Categoria>) request.getAttribute("categorias");
                                for (Categoria categoria : categorias) {
                        %>                    
                    <li class="tab" ng-click="vm.cargarProductos(<% out.print(categoria.getIdCategoria()); %>)">                        
                        <% out.print(categoria.getCategoria()); %>                        
                    </li>                    
                    <%
                            }
                        }
                    %>                    
                    <li class="tab slider"></li>                    
                </ul>
            </div>
        </div>

        <aside class="js-side-nav side-nav" style="z-index:1001;">
            <nav class="js-side-nav-container side-nav__container gradient__mdl">
                <button class="js-menu-hide side-nav__hide material-icons">close</button>                
                <header class="side-nav__header__icon" style="margin-top: 50px;">
                    <i class="icono-side-nav__header material-icons">account_circle</i>
                    ¡Debes iniciar sesión!
                </header>                
                <div class="side-nav__content__sin-sesion">
                    <form action="icas-cliente/" method="POST">
                        <input name="irAlCarrito" type="text" hidden ng-value="vm.tieneProductosEnElCarrito">
                        <button type="submit"                        
                                class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored mdl-color-text--cyan-50">
                            Iniciar sesión
                        </button>
                    </form>    
                    <form action="icas-cliente/#/registrar" method="POST">
                        <input name="irAlCarrito" type="text" hidden ng-value="vm.tieneProductosEnElCarrito">
                        <button 
                            style="margin-top: 15px;"
                            class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored mdl-color-text--cyan-50">
                            Crear una cuenta
                        </button>
                    </form>
                </div>
            </nav>
        </aside>               

        <div layout="column" layout-xs="column" class="contenedor"style="margin-top: 2%;">                
            <!--Buscar-->
            <form name="formBusqueda" ng-submit="vm.buscarProductos(vm.busqueda)">
                <md-card flex class="md-whiteframe-3dp" ng-cloak>                    
                    <div layout="row">                            
                        <md-button ng-disabled="formBusqueda.$invalid" class="md-primary" style="margin: 0px;" type="submit">
                            <md-icon md-svg-icon="buscar" aria-label="Icono_buscar" type="submit" style="width: 30px; height: 30px;"></md-icon>
                        </md-button>
                        <input                                 
                            flex 
                            maxlength="50" 
                            required type="search" 
                            placeholder="Buscar" 
                            style="margin-top: 15px; margin-bottom: 15px; width: 100%; border: none; font-size: 22px; outline: none;"
                            ng-model="vm.busqueda"
                            name="busqueda"
                            id="busqueda">                        
                        <md-button ng-show="vm.busqueda != null" ng-click="vm.volverAtras()" ng-disabled="formBusqueda.$invalid" class="md-icon-button" style="margin-top: 8px; margin-left: 0px;">                                 
                            <md-icon md-svg-icon="cerrar_atras" aria-label="Icono_cerrar"></md-icon>
                        </md-button>                            
                    </div>
                </md-card>
            </form>
            <!--Fin buscar-->
        </div>




    <center>                
        <div class="spinner">
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1" width="24px" height="24px" viewBox="0 0 28 28">            
            <g class="qp-circular-loader">
            <path class="qp-circular-loader-path" fill="none" d="M 14,1.5 A 12.5,12.5 0 1 1 1.5,14" stroke-linecap="round" />
            </g>
            </svg>
        </div>
    </center>

    <div ng-cloak class="mdl-grid contenedor">                        
        <div ng-repeat="producto in vm.productos"
             class="mdl-card mdl-cell mdl-cell--3-col-desktop mdl-cell--4-col-tablet mdl-cell--2-col-phone mdl-shadow--2dp mdl-cell--middle">
            <figure class="mdl-card__media" style="background-color: white;"><!--ng-show="producto.show"-->
                <img ng-src="{{producto.imagen}}" alt="{{producto.nombre}}"/><!--mostrar-on-load-->
            </figure>
            <div class="mdl-card__supporting-text get-off-margin">
                <h5>{{producto.nombre}}</h5>{{producto.precio|currency:$:0}}                    
            </div>
            <div class="mdl-card__actions">                                              
                <a ng-click="vm.verDialogo(producto)" 
                   class="mdl-button mdl-button--colored mdl-js-button">Agregar</a>
                <div class="mdl-layout-spacer"></div>             
            </div>
        </div>                                           
    </div>

    <div id="resultadoBusqueda" style="display: none;">
        <center>
            <p>No se han encontrado resultados para tu búsqueda.</p>
        </center>
    </div>


    <button ng-click="vm.abrirSideNavConBotonCarrito()" class="mdl-button mdl-js-button mdl-button--fab mdl-button--colored mdl-button--floating-action">
        <i class="material-icons">shopping_cart</i>
    </button>                 

    <div id="demo-snackbar-example" class="mdl-js-snackbar mdl-snackbar">
        <div class="mdl-snackbar__text"></div>
        <button class="mdl-snackbar__action" type="button"></button>
    </div>  

    <script src="recursos-cliente/js/jquery.js"></script>
    <script src='recursos-cliente/js/scripts.js'></script>
    <script src='recursos-cliente/js/detabinator.js'></script>
    <script src='recursos-cliente/js/side-nav.js'></script>
    <script src='recursos-cliente/js/mdl.js'></script> 
    <script src="js/angular.min.js"></script>
    <script src="js/angular-animate.min.js"></script>
    <script src="js/angular-aria.min.js"></script>   
    <script src="js/angular-material.min.js"></script>  
    <script src="recursos-cliente/publico/js/AppPublica.js"></script>
</body>
</html>



