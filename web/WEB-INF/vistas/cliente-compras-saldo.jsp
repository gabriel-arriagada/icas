<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>        
        <noscript>
        <meta http-equiv="refresh" content="0;url=error/no-script.html">
        </noscript>
        <link rel="shortcut icon" href="img/icas.ico" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge">   
        <meta name="theme-color" content="#00BCD4"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
        <link rel='stylesheet' href='font/roboto.css'>
        <link href="font/material-icons.css" rel="stylesheet">
        <link href="css/angular-material.min.css" rel="stylesheet">  
        <link href="css/md-data-table.min.css" rel="stylesheet">  
        <script src="js/angular.min.js"></script>
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

            .contenedor{
                margin-top: 3%;
            }

            @media (min-width: 800px) {
                .contenedor {
                    padding-left: 20%;
                    padding-right: 20%;                    
                }
            }           

            @media (max-width: 799px) {
                .contenedor {
                    padding-left: 1%;
                    padding-right: 1%;                    
                }
            }                      

            .div__monito{
                background-color: #B2EBF2;
                min-height: 150px;
            }           

            .monito{
                color: #e0f7fa; 
                font-size: 120px;
            }

            @media (max-width: 384px) {                
                .monito{
                    font-size: 100px;
                }
                .div__monito{
                    padding-right: 5px;
                    padding-left: 5px;
                }
            }
        </style>
        <title>Mi saldo y compras</title>
    </head>    
    <body no-click-derecho ng-app="AppSaldoCompras" ng-controller="SaldoComprasController as vm">

        <div class="header md-toolbar-tools">
            <button class="_btn-atras" ng-click="vm.cancelar()">               
                <md-icon style="color: #e0f7fa;" md-svg-icon="atras" aria-label="Cerrar dialogo"></md-icon>
            </button>
            <h3>Mi saldo y compras</h3>             
        </div>

        <div class="contenedor" ng-cloak="">
            <md-card layout="row">                
                <div flex-sm="33" flex-gt-sm="33" layout-align="center center" layout="column" class="div__monito">
                    <i class="material-icons monito">&#xE263;</i>                                
                </div>
                <div layout="column" flex>
                    <md-card-content flex>
                        <h2 class="md-title">Saldo</h2>
                        <p style="color: grey;">Tu saldo actual es {{vm.saldo|currency:$:0}}.</p>                        
                    </md-card-content>
                    <div class="md-actions" layout="row" layout-align="end center">
                        <md-button class="md-primary">ok</md-button>
                    </div>
                </div>               
            </md-card>            
        </div>

        <div class="contenedor" ng-cloak="">
            <md-card layout="row">                
                <div flex-sm="33" flex-gt-sm="33" layout-align="center center" layout="column" class="div__monito">
                    <i class="material-icons monito">&#xE616;</i>        
                </div>
                <div layout="column" flex>
                    <md-card-content flex>
                        <h2 class="md-title">Última recarga</h2>
                        <p ng-if="vm.fechaRecarga != null" style="color: grey;">Última recarga de {{vm.montoRecarga|currency:$:0}} el {{vm.fechaRecarga}} a las 
                            {{vm.horaRecarga}} hrs.</p>  
                        <p ng-if="vm.fechaRecarga == null" style="color: grey;">Aún no has recargado tu cuenta. 
                            Para recargarla, debes ir al mesón de la cafetería.</p>
                    </md-card-content>
                    <div class="md-actions" layout="row" layout-align="end center">
                        <md-button class="md-primary">ok</md-button>
                    </div>
                </div>               
            </md-card>            
        </div>

        <div ng-if="vm.compras.length > 0" class="contenedor" style="margin-bottom: 50px;" ng-cloak="">
            <md-card>
                <md-card-content>
                    <md-toolbar class="md-table-toolbar md-default">
                        <div class="md-toolbar-tools">
                            <span>Mis compras</span>
                        </div>
                    </md-toolbar>

                    <md-table-container>
                        <table md-table md-row-select multiple="false" ng-model="vm.seleccionado" md-progress="vm.promise">
                            <thead md-head md-order="vm.query.order"  md-on-reorder="vm.ordenSeleccionado">
                                <tr md-row>                                    
                                    <th md-column md-order-by="formaPago" md-desc><span>Forma de pago</span></th>
                                    <th md-column md-order-by="fecha" md-desc><span>Fecha</span></th>
                                    <th md-column md-order-by="total" md-desc><span>Total</span></th>
                                </tr>
                            </thead>
                            <tbody md-body>
                                <tr md-row md-select="compra"            
                                    md-on-select="vm.itemSeleccionado"
                                    md-auto-select 
                                    ng-repeat="compra in vm.compras| orderBy: vm.query.order | limitTo: vm.query.limit : (vm.query.page -1) * vm.query.limit">                                    
                                    <td md-cell>{{compra.formaPago}}</td>
                                    <td md-cell>{{compra.fecha}}</td>
                                    <td md-cell>{{compra.total|currency:$:0}}</td>
                                </tr>
                            </tbody>
                        </table>
                    </md-table-container>

                    <md-table-pagination md-limit="vm.query.limit"                                          
                                         md-page-select="false" 
                                         md-page="vm.query.page" 
                                         md-total="{{vm.compras.length}}" 
                                         md-on-paginate="vm.compras" md-page-select>                                         
                    </md-table-pagination>                
                </md-card-content>                
            </md-card>                
        </div>

        <script src="js/angular-animate.min.js"></script>
        <script src="js/angular-aria.min.js"></script>           
        <script src="js/angular-material.min.js"></script>   
        <script src="js/md-data-table.min.js"></script>   
        <script src="recursos-cliente/privado/js/AppSaldoCompras.js"></script>
    </body>
</html>