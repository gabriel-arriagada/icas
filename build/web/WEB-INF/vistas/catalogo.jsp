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
        <link href="recursos-cliente/privado/css/style.css" rel="stylesheet">       
        <link href="css/angular-material.min.css" rel="stylesheet">        
        <script src="js/angular.min.js"></script>
        <style>                                   
            .alto-fijo{
                min-height: 100px;
            }

            @media (min-width: 800px) {
                .contenedor {
                    padding-left: 10%;
                    padding-right: 10%;                    
                }

                ::-webkit-scrollbar {
                    -webkit-appearance: none;            
                }

                ::-webkit-scrollbar:vertical {
                    width: 5px;
                }
                ::-webkit-scrollbar:horizontal {
                    height: 5px;
                }
                ::-webkit-scrollbar-thumb {
                    background-color: rgba(121, 121, 121, 0.3);               

                }
                ::-webkit-scrollbar-track {
                    border-radius: 10px;                
                    opacity: 0.1;
                }

                .alto-fijo{
                    //min-height: 250px;                    
                }
            }       

            @media (min-width: 2100px) {
                .contenedor {
                    padding-left: 20%;
                    padding-right: 20%;                    
                }
            }    

            .mdl-card__actions {
                padding-left: 4px;
            }

            .mdl-card__actions a{
                padding-right: 7px;
                padding-left: 7px;
            }

            .mdl-card__actions a:hover{
                color: #00BCD4;                
            }  

            .__titulo{
                margin-left: 10px;     
                margin-top: 20px;
                font-size: 20px;
                color: grey;
            }

            .titulo_compras-frecuentes{
                font-size: 24px !important; 
                margin-top: 30px !important; 
                color: #FFF !important;
                /*text-transform: uppercase;*/
            }

            .titulo_pedidos{
                font-size: 24px !important;                 
            }

            @media (max-width: 720px){
                .__titulo{
                    margin-left: 20px;                    
                }
                .titulo_compras-frecuentes{
                    font-size: 20px !important;
                }
                .titulo_pedidos{                    
                    font-size: 20px !important; 
                    padding-left: 9px;
                }                
            }       

        </style>
    </head>
    <body no-click-derecho ng-app="AppCatalogo" ng-controller="ClienteController as vm" ng-init="vm.iniciar('${usuario}', '${rut}')">

        <div class="contenedor__master">            
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

            <aside id="nav" class="js-side-nav side-nav" style="z-index:20">
                <nav class="js-side-nav-container side-nav__container">
                    <button class="js-menu-hide side-nav__hide material-icons" hidden>close</button>
                    <header class="tool-bar-sidenav" layout="column" layout-align="space-around start">                    
                        <div layout="row" layout-align="start center" class="layout-align-start-center layout-row">
                            <img class="circulo">
                            <span flex="" class="flex"></span>
                        </div>                    
                        <div>                        
                            <div class="md-body-1" style="font-size: 16px;">Bienvenido(a)</div>
                            <div class="md-body-2" style="font-size: 16px;">${usuario}</div>                        
                        </div>                    
                    </header>                
                    <md-list flex ng-cloak="">                    
                        <md-list-item ng-repeat="item in vm.menu" ng-click="vm.href(item.url)">
                            <md-icon md-svg-icon="{{item.icono}}" aria-label="panel"></md-icon>
                            <p>{{item.nombre}}</p>
                        </md-list-item>
                        <md-divider></md-divider>
                        <md-list-item ng-click="vm.href('CerrarSesionServlet')">
                            <md-icon md-svg-icon="salir" aria-label="panel"></md-icon>
                            <p>Salir</p>
                        </md-list-item>
                    </md-list>                
                </nav>
            </aside>          

            <!--Compra en un click y pedidos pendientes-->
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

                <md-card ng-show="vm.idCat == 0 && vm.pedidos.length > 0" flex class="md-whiteframe-3dp" ng-cloak="">
                    <div class="md-padding" style="margin-top: 0px; height: 36px; padding-top: 25px; margin-bottom: 0px;">                        
                        <span class="titulo_pedidos">Pedidos pendientes</span>                        
                    </div>
                    <md-card-content  ng-if="!vm.cargandoPedidos" class="alto-fijo" style="margin-top: -23px !important; padding-left: 0px !important; padding-right: 0px !important;">                        
                        <md-list>
                            <md-list-item ng-repeat="pedido in vm.pedidos" class="md-3-line" ng-click="null">
                                <span ng-if="pedido.estado == 'Preparado'" style="width: 40px; height: 40px; border-radius: 20px; line-height: 40px; font-size: 21px; margin-right: 15px;" class="mdl-chip__contact mdl-color--pink-300 mdl-color-text--white">P</span>                                
                                <span ng-if="pedido.estado == 'Recibido'" style="width: 40px; height: 40px; border-radius: 20px; line-height: 40px; font-size: 21px; margin-right: 15px;" class="mdl-chip__contact mdl-color--cyan mdl-color-text--white">R</span>                                
                                <div class="md-list-item-text" layout="column">
                                    <h3>Pedido n° {{pedido.idPedido}}</h3>
                                    <h4>Hora de retiro: {{pedido.horaRetiro}} hrs.</h4>                                                
                                    <p ng-if="pedido.pagada">Pedido pagado.</p>
                                    <p ng-if="!pedido.pagada">Debes pagar {{pedido.total|currency:$:0}} al retirar.</p>
                                </div>                                
                                <md-menu md-position-mode="target-right target" >
                                    <md-button aria-label="Abrir menu" class="md-icon-button" ng-click="$mdOpenMenu($event)">
                                        <md-icon md-menu-origin aria-label="Estado" md-svg-icon="desplegar"></md-icon>
                                    </md-button>
                                    <md-menu-content width="2">
                                        <md-menu-item>                                            
                                            <div layout="row" flex>
                                                <span ng-if="pedido.estado == 'Preparado'" class="mdl-chip mdl-chip--contact">
                                                    <span class="mdl-chip__contact mdl-color--pink-300 mdl-color-text--white">P</span>
                                                    <span class="mdl-chip__text">Preparado</span>
                                                </span>                                                 
                                                <span ng-if="pedido.estado == 'Recibido'" class="mdl-chip mdl-chip--contact">
                                                    <span class="mdl-chip__contact mdl-color--cyan mdl-color-text--white">R</span>
                                                    <span class="mdl-chip__text">Recibido</span>
                                                </span>                                                 
                                            </div>                                            
                                        </md-menu-item>
                                    </md-menu-content>
                                </md-menu>
                            </md-list-item>
                        </md-list>                                
                    </md-card-content>
                    <md-card-content class="alto-fijo" layout="column" layout-align="center center" ng-if="vm.cargandoPedidos">
                        <div flex>
                            <md-progress-circular style="margin-top: 20px;" md-diameter="30" md-mode="indeterminate"></md-progress-circular>
                        </div>
                    </md-card-content>                    
                    <md-card-actions>                        
                        <md-button class="md-primary" ng-click="vm.consultarPedidos()"> Consultar estado</md-button>                        
                    </md-card-actions>
                </md-card>                
                <md-card ng-show="vm.idCat == 0 && vm.compras.length > 0" flex class="md-whiteframe-3dp" ng-cloak="">                
                    <md-card-content style="padding: 0px !important;">                        
                        <md-toolbar class="md-medium-tall">
                            <div class="md-toolbar-tools">
                                <h3 class="titulo_compras-frecuentes">Compra en un click</h3>                                 
                                <span flex></span>
                                <md-button class="md-icon-button" style="margin-top: 30px; color: #FFF;">
                                    <md-icon md-svg-icon="mas" aria-label="Menú más"></md-icon>
                                </md-button>
                            </div>
                        </md-toolbar>
                        <md-tabs md-dynamic-height md-border-bottom class="md-primary">
                            <md-tab ng-repeat="compra in vm.compras" label="Pedido {{$index + 1}}"ng-click="vm.setCompra(compra)">
                                <md-content class="md-padding" style="padding-top: 5px !important;">                                    
                                    <md-subheader flex style="background-color: #ffffff !important;">
                                        Producto(s) del pedido                                                                                
                                    </md-subheader>
                                    <md-list>
                                        <md-list-item class="md-2-line" ng-repeat="d in compra.detalle" ng-click="null">
                                            <img ng-src="{{d.imagen}}" class="md-avatar" alt="{{d.nombre}}">
                                            <div class="md-list-item-text" layout="column">
                                                <h3>{{d.cantidad}} {{d.nombre}}</h3>
                                                <h4>Sub total {{ d.subTotal|currency:$:0}}</h4>                                                
                                            </div>
                                        </md-list-item>
                                    </md-list>
                                </md-content>
                            </md-tab>                            
                        </md-tabs>
                    </md-card-content>
                    <md-card-actions>
                        <md-button ng-click="vm.comprarEnUnClick()" class="md-primary">Comprar ahora</md-button>
                    </md-card-actions>
                </md-card>                
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


            <div id="div__titulo" class="contenedor" style="display: none;">
                <p class="__titulo">Productos</p>
            </div>

            <div ng-cloak class="mdl-grid contenedor" ng-if="vm.productos.length > 0">                        
                <div ng-repeat="producto in vm.productos"
                     class="mdl-card mdl-cell mdl-cell--3-col-desktop mdl-cell--4-col-tablet mdl-cell--2-col-phone mdl-shadow--2dp mdl-cell--middle">
                    <figure class="mdl-card__media" style="background-color: white;">
                        <img ng-src="{{producto.imagen}}" alt="{{producto.nombre}}"/>
                    </figure>
                    <div class="mdl-card__supporting-text get-off-margin">
                        <h5>{{producto.nombre}}</h5>{{producto.precio|currency:$:0}}                    
                    </div>
                    <div class="mdl-card__actions">                                              
                        <a ng-click="vm.verDialogo(producto)" 
                           class="mdl-button mdl-button--colored mdl-js-button">Agregar</a>
                        <div class="mdl-layout-spacer"></div> 
                        <!--<button class="mdl-button mdl-button--icon mdl-button--colored">
                            <i class="material-icons">favorite</i>
                        </button>-->
                    </div>
                </div>                                           
            </div>


            <div id="div__noResultado" style="display: none;">
                <center>
                    <p>No se han encontrado resultados para tu búsqueda.</p>
                </center>
            </div>

            <button ng-click="vm.verCarrito()" class="mdl-button mdl-js-button mdl-button--fab mdl-button--colored mdl-button--floating-action">
                <i class="material-icons">shopping_cart</i>
            </button>                 

            <div id="demo-snackbar-example" class="mdl-js-snackbar mdl-snackbar">
                <div class="mdl-snackbar__text"></div>
                <button class="mdl-snackbar__action" type="button"></button>
            </div>        

        </div>

        <script type="text/javascript">
                            if (window.screen.width > 800){
                    document.getElementById("nav").className += " side-nav--visible";
                    } else {
                    document.getElementsByTagName("md-tabs")[0].setAttribute("md-dynamic-height", "");
                    }
        </script>
        <script src="recursos-cliente/js/jquery.js"></script>
        <script src='recursos-cliente/js/scripts.js'></script>
        <script src='recursos-cliente/js/detabinator.js'></script>
        <script src='recursos-cliente/js/side-nav.js'></script>
        <script src='recursos-cliente/js/mdl.js'></script>        
        <script src="js/angular-animate.min.js"></script>
        <script src="js/angular-aria.min.js"></script>   
        <script src="js/angular-material.min.js"></script>        
        <script src="recursos-cliente/privado/js/AppCatalogo.js"></script>    
    </body>
</html>


