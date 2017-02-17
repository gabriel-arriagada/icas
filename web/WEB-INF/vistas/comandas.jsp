<!DOCTYPE html>
<html lang="en" >
    <head>
        <noscript>
        <meta http-equiv="refresh" content="0;url=error/no-script.html">
        </noscript>
        <title>Panel de comandas</title>
        <link rel="shortcut icon" href="img/icas.ico" />
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel='stylesheet' href='font/roboto.css'>
        <link rel="stylesheet" href="css/angular-material.min.css">                        
        <link rel="stylesheet" href="recursos-admin/assets/css/comanda-style.css">                        
        <link rel="stylesheet" href="recursos-admin/assets/css/material-design-lite.css">                        
    </head>

    <body no-click-derecho style="background-color: #D7DBD8;"
          ng-app="ComandasApp" 
          layout="column" 
          ng-controller="ComandasController as vm"            
          ng-cloak>

    <md-card flex layout="row" style="margin: 1.5% 3% 1.5% 3%;">      

        <div flex="30" layout="column">

            <md-toolbar flex="7" class="fondoGris">
                <div class="md-toolbar-tools">

                    <md-button ng-click="vm.volver()" aria-label="Volver" class="md-icon-button">
                        <md-icon md-svg-icon="atras"></md-icon>                                                       
                    </md-button>

                    <span flex></span>

                    <md-button ng-disabled="true" aria-label="Notificaciones">
                        <md-icon md-menu-align-target md-svg-icon="notifi"></md-icon>                                                       
                    </md-button>
                    <span class="notificacion">{{vm.comandas.length}}</span> 

                    <!--<md-button aria-label="Opción" class="md-icon-button">
                        <md-icon 
                            md-menu-origin
                            md-menu-align-target
                            md-svg-icon="mas" aria-label="Más"
                            style="color: gray !important;">                                        
                        </md-icon>
                    </md-button>-->
                </div>
            </md-toolbar>                

            <md-toolbar flex="7" class="animate-show md-hue-1" style="background-color: white;">
                <div class="md-toolbar-tools">
                    <md-button class="md-icon-button" ng-click="null" aria-label="Menu">
                        <md-icon md-svg-icon="buscar" aria-label="Buscar"></md-icon>
                    </md-button>                            
                    <span flex>
                        <form name="form">
                            <md-autocomplete md-input-name="rutCliente"    
                                             md-no-cache="true"                                                                                                                                                   
                                             md-search-text="vm.textoDeBusqueda"
                                             md-items="item in vm.buscarPorRut(vm.textoDeBusqueda)"
                                             md-selected-item-change="vm.cargarPedido(item)"
                                             md-item-text="item.nombre"
                                             placeholder="Buscar pedido por rut">
                                <md-item-template>                                        
                                    <span md-highlight-text="vm.textoDeBusqueda">
                                        {{item.nombre}} - {{item.rut}} - idPedido: {{item.idPedido}}
                                    </span>
                                </md-item-template>
                                <div ng-messages="form.rutCliente.$error" ng-if="form.rutCliente.$touched">

                                </div>
                            </md-autocomplete>
                        </form>                            
                    </span>
                </div>
            </md-toolbar>

            <md-divider></md-divider>

            <md-content flex class="fondoComandas">                                                           
                <md-list><!--class="md-dense"-->
                    <md-list-item 
                        class="md-2-line" 
                        ng-repeat="item in vm.comandas| orderBy:'hora'" 
                        ng-click="vm.cargarPedido(item)">                                                    

                        <md-icon md-svg-icon="comanda" class="md-avatar-icon"></md-icon>  <!--get-color-->                           

                        <div class="md-list-item-text" layout="column">
                            <h3 style="font-weight: bold;">{{ item.hora}} hrs.</h3>
                            <h4>{{ item.nombre}}</h4>
                            <p>Pedido n° {{item.idPedido}}</p>
                            <md-divider></md-divider>
                        </div>

                        <span ng-if="item.estado == 'Recibido'" class="mdl-chip mdl-chip--contact" style="width: 125px;">
                            <span class="mdl-chip__contact mdl-color--pink-300 mdl-color-text--white" style="font-size: 15px;">R</span>
                            <span class="mdl-chip__text">Recibido</span>
                        </span>                                                 

                        <span ng-if="item.estado == 'Preparado'" class="mdl-chip mdl-chip--contact" style="width: 125px;">
                            <span class="mdl-chip__contact mdl-color--cyan mdl-color-text--white" style="font-size: 15px;">P</span>
                            <span class="mdl-chip__text">Preparado</span>
                        </span>     

                        <span ng-if="item.estado == 'En preparación'" class="mdl-chip mdl-chip--contact" style="width: 125px;">
                            <span class="mdl-chip__contact mdl-color--yellow-300 mdl-color-text--grey-900" style="font-size: 15px;">EP</span>
                            <span class="mdl-chip__text">En preparación</span>
                        </span>     

                    </md-list-item>                       
                </md-list>
            </md-content>

        </div>

        <md-divider></md-divider>

        <!--imagen de mano con celular-->
        <div flex="70" class="padre barraInferior" 
             style="background-color: #fff;" 
             ng-cloak 
             ng-if="vm.verPedido == false">
            <div class="hijo">                 
                <div class="circular"></div>
                <br><br>
                <center>
                    <p class="textoRecepcion">Recepción de pedidos de clientes.</p>
                </center>                    
            </div>
        </div>
        <!--Fin imagen-->

        <!--Detalle del pedido-->
        <div flex="70" layout="column" layout-align="start stretch" class="padre imagen" style="background-color: #fff;" ng-cloak ng-if="vm.verPedido == true && vm.comandas.length > 0">  
            <div flex="7">
                <md-toolbar class="fondoGris">
                    <div class="md-toolbar-tools" style="color: #000000;">
                        <h3>Pedido n° {{vm.numeroPedido}}</h3>
                        <span flex></span>
                        <h3 ng-if="!vm.informacionVenta.pagada">
                            NO pagado
                        </h3>                            
                        <h3 ng-if="vm.informacionVenta.pagada">
                            Pagado
                        </h3>
                    </div>
                </md-toolbar>               
            </div>                
            <md-content flex class="fondoTransparente"> 
                <div style="margin-left: 13%; margin-right: 13%;">
                    <comentario-pedido
                        ng-if="vm.comentario != null"
                        comentario="{{vm.comentario}}"
                        md-theme="default">
                    </comentario-pedido>
                    <detalle-pedido 
                        ng-repeat="detalle in vm.detallePedido"
                        cantidad="{{detalle.cantidad}}" 
                        producto="{{detalle.producto}}" 
                        subTotal="{{detalle.subTotal}}"
                        imagen="{{detalle.imagen}}"
                        md-theme="default">
                    </detalle-pedido>                            
                </div>                
            </md-content>

            <!--Barra inferior de acciones para finalizar-->
            <div layout="row" layout-align="start center" 
                 style="height: 55px; width: 100%; background-color: #F5F1EE; padding-left: 20px;">

                <span flex="25" ng-if="vm.informacionVenta.total > 0" class="mdl-chip mdl-chip--contact" style="margin-right: 20px;">
                    <span class="mdl-chip__contact mdl-color--grey-400 mdl-color-text--grey-900" style="font-size: 15px;">
                        <md-icon md-svg-icon="debe"></md-icon>
                    </span>
                    <span class="mdl-chip__text" style="font-size: 18px;">Debe {{vm.informacionVenta.total| currency:$:0}}</span>
                </span>                

                <span flex="25" ng-if="vm.informacionVenta.total > 0" class="mdl-chip mdl-chip--contact">
                    <span class="mdl-chip__contact mdl-color--grey-400 mdl-color-text--grey-900" style="font-size: 15px;">
                        <md-icon md-svg-icon="formaPago"></md-icon>
                    </span>
                    <span class="mdl-chip__text" style="font-size: 18px;">Paga con {{vm.informacionVenta.formaPago}}</span>
                </span>

            </div>            

            <div class="hijo" ng-if="vm.buscandoPedido == true" >
                <md-progress-circular md-mode='indeterminate' class="md-primary" md-diameter="60">                    
                </md-progress-circular>
            </div>

            <md-button ng-if="vm.currentItem.estado == 'En preparación'"                    
                       ng-click="vm.cambiarEstadoPedidoToPreparado()" 
                       class="md-fab md-primary md-fab-bottom-right" 
                       aria-label="Cambiar a preparado">
                <md-tooltip md-direction="top">
                    Cambiar el estado del pedido a preparado
                </md-tooltip>
                <md-icon md-svg-icon="preparado" style="width: 30px; height: 30px;"></md-icon>
            </md-button>                

            <md-button ng-if="vm.currentItem.estado == 'Preparado'"                    
                       ng-click="vm.cambiarEstadoPedidoToEntregado()" 
                       class="md-fab md-primary md-fab-bottom-right" 
                       aria-label="Entregar el pedido">
                <md-tooltip md-direction="top">
                    Entregar pedido
                </md-tooltip>
                <md-icon md-svg-icon="entregado"></md-icon>
            </md-button>                
        </div>   

    </md-card> 


    <script src="js/angular.min.js"></script>
    <script src="js/angular-animate.min.js"></script>
    <script src="js/angular-aria.min.js"></script>
    <script src="js/angular-material.min.js"></script>
    <script src="js/angular-messages.min.js"></script>
    <script src="js/angular-websocket.min.js"></script>

    <script src="recursos-admin/js/comandas/ComandasApp.js"></script>        
    <script src="recursos-admin/js/comandas/PedidosFactory.js"></script> 
    <script src="recursos-admin/js/comandas/GetColor.js"></script> 
    <script src="recursos-admin/js/comandas/ComandasController.js" charset="utf-8"></script>     

</body>
</html>
