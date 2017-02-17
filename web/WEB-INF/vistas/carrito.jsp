<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>  
        <noscript>
        <meta http-equiv="refresh" content="0;url=error/no-script.html">
        </noscript>
        <meta name="viewport" content="width=device-width,minimum-scale=1">
        <meta name="theme-color" content="#00BCD4"/>
        <title>Carrito</title>       
        <link rel="shortcut icon" href="img/icas.ico" />
        <link rel='stylesheet' href='font/roboto.css'>
        <link href="css/angular-material.min.css" rel="stylesheet">  
        <link href="css/md-data-table.min.css" rel="stylesheet">  
        <link href="css/mdPickers.min.css" rel="stylesheet">  
        <style>
            html, body {                
                background-color: #EEEEEE;   
                font-family: 'Roboto', sans-serif;
            }

            body{
                overflow-x: hidden;
            }
            .header {
                width: 100%;
                height: 56px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.4);
                background: #00BCD4;
                color: #e0f7fa;          
                align-items: center;
                padding: 0 16px;
            }
            ._btn-atras{
                background: none;
                border: none;                                                
                outline: none;
            }
            .fila-forma-pago {
                border-bottom: 1px dashed #ddd; 
            }           

            .contenedor{
                margin-top: 3%;
            }

            @media (min-width: 800px) {
                .contenedor {
                    padding-left: 15%;
                    padding-right: 15%;                    
                }
            }           

            @media (max-width: 799px) {
                .contenedor {
                    padding-left: 1%;
                    padding-right: 1%;                    
                }
            }      
        </style>
        <script src="js/angular.min.js"></script>            
    </head>
    <% String nombre = "";
        String rut = "";
        if (request.getAttribute("nombre") != null) {
            nombre = request.getAttribute("nombre").toString();
            rut = request.getAttribute("rut").toString();
        }%>
    <body no-click-derecho ng-app="AppCarrito" ng-controller="CarritoController as vm" ng-init="vm.iniciar('<%out.print(nombre);%>', '<%out.print(rut);%>')">

        <div class="header md-toolbar-tools">
            <button class="_btn-atras" ng-click="vm.cancelar()">
                <md-icon style="color: #e0f7fa;" md-svg-icon="atras" aria-label="Cerrar dialogo"></md-icon>
            </button>
            <h2>Carrito</h2>
            <span flex></span>  
        </div>
        <!--<md-toolbar>
            <div class="md-toolbar-tools">            
                <md-button class="md-icon-button" ng-click="vm.cancelar()">
                    <md-icon md-svg-icon="atras" aria-label="Cerrar dialogo"></md-icon>
                </md-button>
                <h2>Carrito</h2>
                <span flex></span>            
            </div>
        </md-toolbar>-->
        <div class="contenedor">
            <md-card aria-label="Carrito" ng-cloak>
                <form>            
                    <md-card-content ng-cloak>

                        <div class="md-dialog-content" ng-if="vm.carro.length == 0">                
                            <center>
                                <md-icon md-svg-icon="carro" aria-label="Carro vacío" style="color: lightgrey;width: 150px; height: 150px;"></md-icon>
                                <p>¡No hay productos en el carrito!</p>
                            </center>
                        </div>

                        <!--<div class="md-dialog-content" ng-if="vm.carro.length > 0">   -->
                        <md-tabs md-dynamic-height md-border-bottom  md-selected="vm.tabItemSeleccionado"  ng-if="vm.carro.length > 0">

                            <md-tab label="Productos" ng-disabled="vm.respuestaDePago != null">
                                <div class="md-dialog-content">
                                    <md-toolbar class="md-table-toolbar md-default" ng-class="{esconder: vm.seleccionado.length > 0, mostrar: vm.seleccionado.length == 0}">
                                        <div class="md-toolbar-tools">
                                            <span style="font-size: 20px;">Mi compra</span>
                                        </div>
                                    </md-toolbar>

                                    <md-toolbar class="md-table-toolbar alternate" ng-class="{mostrar: vm.seleccionado.length > 0, esconder: vm.seleccionado.length == 0}">
                                        <div class="md-toolbar-tools">
                                            <span>{{vm.seleccionado.length}} {{vm.seleccionado.length > 1 ? 'items seleccionados' : 'item seleccionado'}}</span>
                                            <div flex></div>
                                            <md-button class="md-icon-button" ng-click="vm.eliminarItemDelCarro()">
                                                <md-icon md-svg-icon="eliminar" aria-label="Eliminar"></md-icon>
                                            </md-button>
                                        </div>
                                    </md-toolbar>   


                                    <md-table-container>
                                        <table md-table md-row-select multiple="false" ng-model="vm.seleccionado" md-progress="vm.promesa">
                                            <thead md-head md-order="vm.query.order">
                                                <tr md-row>                                
                                                    <th md-column md-order-by="nombre"><span>Producto</span></th>
                                                    <th md-column md-numeric md-order-by="precio" md-desc><span>Precio</span></th>
                                                    <th md-column md-numeric md-order-by="cantidad" md-desc><span>Cant.</span></th>
                                                    <th md-column md-numeric md-order-by="subTotal" md-desc><span>Sub total</span></th>
                                                </tr>
                                            </thead>
                                            <tbody md-body>
                                                <tr md-row md-select="item" 
                                                    md-on-select="vm.itemSeleccionado" 
                                                    md-auto-select 
                                                    ng-repeat="item in vm.carro| orderBy: vm.query.order | limitTo: vm.query.limit : (vm.query.page -1) * vm.query.limit"
                                                    ng-class="{'stock-excedido': item.stockExcedido == true}">
                                                    <td md-cell>{{item.nombre}}</td>
                                                    <td md-cell>{{item.precio| currency:$:0}}</td>
                                                    <td md-cell>{{item.cantidad}}</td>
                                                    <td md-cell>{{item.subTotal| currency:$:0}}</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </md-table-container>   
                                    <br>
                                    <md-input-container style="width: 100%; margin-bottom: 0px;">
                                        <label>Comentario</label>
                                        <input type="text" md-maxlength="50" ng-model="vm.comentario" maxlength="50" placeholder="Ingresar un comentario"/>
                                    </md-input-container>
                                </div>                                                                         
                            </md-tab>                


                            <!--INICIO FORMAS DE PAGO-->
                            <md-tab label="Forma de pago" ng-disabled="vm.respuestaDePago != null">
                                <md-content class="md-padding" style="background-color: white !important;">
                                    <h3 style="margin-left: 15px; font-weight: normal;">Selecciona una forma de pago</h3>
                                    <md-divider></md-divider>
                                    <md-radio-group                            
                                        ng-model="vm.idFormaDePagoSeleccionada" >
                                        <div ng-repeat='forma in vm.formasDePago' class="fila-forma-pago">
                                            <div flex layout='row' layout-padding layout-align="start center" >
                                                <div flex style="max-width:200px;">
                                                    {{forma.descripcion}}
                                                </div>
                                                <md-radio-button flex
                                                                 ng-disabled="forma.nombre == 'JUNAEB' && vm.total < forma.minimo"
                                                                 ng-value="forma.id"                                                     
                                                                 class="md-primary" >
                                                    {{forma.nombre}}
                                                </md-radio-button>
                                            </div>
                                        </div>
                                    </md-radio-group>                        
                                </md-content>
                            </md-tab>
                            <!--FIN FORMA DE PAGO-->

                            <!--Seleccion de hora de retiro-->
                            <md-tab label="Hora de retiro" ng-disabled="vm.respuestaDePago != null || vm.idFormaDePagoSeleccionada == 0">
                                <div class="md-dialog-content" ng-if="!vm.pagando">
                                    <md-toolbar class="md-table-toolbar md-default">
                                        <div class="md-toolbar-tools">
                                            <span style="font-size: 20px;">Mi horario</span>
                                        </div>
                                    </md-toolbar>
                                    <div layout="row" layout-align="start center">
                                        <div flex="33">
                                            <md-button ng-click="vm.mostrarTimePicker($event)" 
                                                       class="md-fab md-accent" 
                                                       aria-label="Hora" 
                                                       ng-if="vm.carro.length > 0">
                                                <md-icon md-svg-icon="reloj"></md-icon>
                                            </md-button>
                                        </div>
                                        <div flex="66">
                                            <p>Presiona el botón 
                                            <md-icon md-svg-icon="reloj" aria-label="Eliminar"></md-icon>
                                            para seleccionar la hora de retiro de tu encargo. O 
                                            <a style="cursor: pointer;" ng-click="vm.calcularHoraDeRetiro()">retira lo antes posible</a>.
                                            </p>                               
                                        </div>

                                    </div>
                                    <div ng-if="vm.buscandoHora" layout="column" layout-align="center center">
                                        <md-progress-circular flex md-diameter="20" md-mode="indeterminate"></md-progress-circular>
                                        <p flex style="margin-top: 20px; color: grey;">Obteniendo hora</p>
                                    </div>
                                    <div layout="row" layout-align="center center" ng-if="vm.horaDeRetiro != null && vm.permitidoPagar == true">
                                        <p>
                                            Iré a buscar mi pedido a las 
                                            <span style="font-weight: bold;">{{vm.horaDeRetiro}}</span> hrs.
                                        </p>
                                    </div>                                            
                                </div>    

                                <div ng-if="vm.pagando" class="md-dialog-content" style="min-height: 200px;" layout="column" layout-align="center center">
                                    <md-progress-circular flex style="margin-top: 20px;" md-diameter="40" md-mode="indeterminate"></md-progress-circular>
                                    <p flex style="margin-top: 20px; color: grey;">Procesando venta</p>
                                </div>

                            </md-tab>
                            <!--Fin selección hora de retiro-->               

                            <!--Sección de resultado al pagar-->
                            <md-tab label="{{vm.respuestaDePago.exito == false ? 'Error' : 'Éxito'}}" 
                                    ng-disabled="vm.respuestaDePago == null">

                                <div class="md-dialog-content" ng-if="vm.respuestaDePago.razon == 'saldoInsuficiente'">
                                    <center>
                                        <md-icon md-svg-icon="cara_triste" aria-label="Error de saldo" style="color: lightgrey;width: 150px; height: 150px;"></md-icon>
                                        <p>¡No tienes saldo suficiente para realizar la compra!</p>
                                    </center>
                                </div> 

                                <div class="md-dialog-content" ng-if="vm.respuestaDePago.razon == 'stockInsuficiente'">
                                    <center>
                                        <md-icon md-svg-icon="cara_triste" aria-label="Error de stock" style="color: lightgrey;width: 150px; height: 150px;"></md-icon>
                                        <p>¡Algunos productos han cambiado su stock por que otros alumnos los han comprado antes que tu!</p>
                                    </center>
                                </div> 

                                <div class="md-dialog-content" ng-if="vm.respuestaDePago.exito == true">
                                    <center>
                                        <md-icon md-svg-icon="cara_feliz" aria-label="Venta realizada" style="color: lightgrey;width: 150px; height: 150px;"></md-icon>
                                        <p>¡Tu compra se ha realizado con éxito!</p>
                                        <p>{{vm.textoExitoTrue}}</p>
                                        <p ng-if="vm.ventaPagada == true" style="font-weight: bold;">Nuevo saldo: {{vm.nuevoSaldo| currency:$:0}}</p>
                                        <p ng-if="vm.ventaPagada == false" style="font-weight: bold;">Recuerda que debes pagar {{vm.total| currency:$:0}} al retirar.</p>
                                    </center>
                                </div> 

                                <div class="md-dialog-content" ng-if="vm.otroError == true">
                                    <center>
                                        <md-icon md-svg-icon="cara_triste" aria-label="Otro error" style="color: lightgrey;width: 150px; height: 150px;"></md-icon>
                                        <p>¡Ups! Ha ocurrido un error al procesar la compra. Inténtalo más tarde.</p>
                                    </center>
                                </div> 

                            </md-tab>
                            <!--Fin sección de resultado-->


                        </md-tabs>                             
                        <!-- </div>-->

                    </md-card-content>
                    <md-card-actions layout="row" ng-cloak>

                        <md-chips ng-if="vm.respuestaDePago == null">                                                  
                            <md-chip>Total: {{vm.total| currency:$:0}}</md-chip>
                        </md-chips>                       

                        <span flex></span> 

                        <md-button class="md-primary md-raised md-hue-1" ng-click="vm.avanzarDeTabItem(1)"  
                                   ng-if="vm.carro.length > 0 && vm.horaDeRetiro == null && vm.tabItemSeleccionado == 0">
                            Siguiente
                        </md-button>         

                        <md-button class="md-primary md-raised md-hue-1" ng-click="vm.avanzarDeTabItem(2)"  
                                   ng-if="vm.carro.length > 0 && vm.horaDeRetiro == null && vm.tabItemSeleccionado == 1 && vm.idFormaDePagoSeleccionada > 0">
                            Siguiente
                        </md-button>

                        <md-button  ng-click="vm.pagar()" class="md-primary md-raised md-hue-1" type="submit" 
                                    ng-if="vm.carro.length > 0 && vm.permitidoPagar == true && vm.respuestaDePago.exito != true">
                            Pagar
                        </md-button>

                        <md-button class="md-primary md-raised md-hue-1" ng-click="vm.cancelar()"  ng-if="vm.carro.length == 0">
                            Volver
                        </md-button>

                        <md-button class="md-primary md-raised md-hue-1" ng-click="vm.reiniciarError()"  
                                   ng-if="vm.carro.length > 0 && vm.respuestaDePago.exito == false && vm.otroError == false">
                            Ok
                        </md-button>

                        <md-button class="md-primary md-raised md-hue-1" ng-click="vm.cancelar()"  
                                   ng-if="vm.otroError == true">
                            Ok
                        </md-button>

                        <md-button class="md-primary md-raised md-hue-1" ng-click="vm.cancelar()"  
                                   ng-if="vm.carro.length > 0 && vm.respuestaDePago.exito == true">
                            Salir
                        </md-button>

                    </md-card-actions>
                </form>

            </md-card>
        </div>    

        <script src="js/angular-animate.min.js"></script>
        <script src="js/angular-aria.min.js"></script>
        <script src="js/angular-messages.min.js"></script>    
        <script src="js/angular-material.min.js"></script>    
        <script src="js/angular-websocket.min.js"></script>    
        <script src="js/md-data-table.min.js"></script>   
        <script src="js/moment.min.js"></script>    
        <script src="js/mdPickers.min.js"></script>    
        <script src="recursos-cliente/privado/js/AppCarrito.js"></script>

    </body>
</html>



