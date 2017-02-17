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
        <!--<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">-->
        <link href="font/material-icons.css" rel="stylesheet">
        <link href="css/angular-material.min.css" rel="stylesheet">  
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

            .div__monito{
                background-color: #B2EBF2;
                min-height: 200px;
            }

            .ocultar{
                display: none;
            }

            .mostrar{
                display: inline;
            }
        </style>
        <title>Mis datos</title>
    </head>
    <body no-click-derecho ng-app="AppMisDatos" ng-controller="MisDatosController as vm">
        <div class="header md-toolbar-tools">
            <button class="_btn-atras" ng-click="vm.cancelar()">               
                <md-icon style="color: #e0f7fa;" md-svg-icon="atras" aria-label="Cerrar dialogo"></md-icon>
            </button>
            <h3>Mis datos</h3>             
        </div>

        <div class="contenedor" ng-cloak="">
            <form name="datos" ng-submit="vm.editarCliente(datos)">
                <md-card layout="row" layout-xs="column">                
                    <div flex-sm="33" flex-gt-sm="33" layout-align="center center" layout="column" class="div__monito">
                        <i class="material-icons" style="color: #e0f7fa; font-size: 120px;">&#xE853;</i>        
                    </div>
                    <div layout="column" flex>
                        <md-card-content flex layout="column">
                            <h2 class="md-title">Datos personales</h2>

                            <md-input-container>
                                <label>Nombre</label>
                                <input name='nombre' ng-model="vm.personales.nombre" required ng-disabled="!vm.editar" maxlength="50">
                                <div ng-messages="datos.nombre.$error">
                                    <div ng-message="required">Completa este campo.</div>
                                </div>
                            </md-input-container>
                            <md-input-container>
                                <label>Apellido</label>
                                <input name="apellido" ng-model="vm.personales.apellido" required ng-disabled="!vm.editar" maxlength="50">
                                <div ng-messages="datos.apellido.$error">
                                    <div ng-message="required">Completa este campo.</div>
                                </div>
                            </md-input-container>
                            <md-input-container>
                                <label>Correo</label>
                                <input name='correo' type="email" ng-pattern="/^.+@.+\..+$/" maxlength="100" ng-model="vm.personales.correo" required ng-disabled="!vm.editar" maxlength="50">
                                <div ng-messages="datos.correo.$error" md-auto-hide="false" ng-show="datos.correo.$touched" ng-messages-multiple>
                                    <div ng-message="required">Completa este campo.</div>
                                    <div ng-message="pattern">Escribe un correo real.</div>
                                </div>
                            </md-input-container>                                                
                        </md-card-content>
                        <div class="md-actions" layout="row" layout-align="end center">

                            <md-button 
                                class="md-primary" 
                                ng-click="vm.editar = true"
                                ng-if="!vm.editar && !vm.datosEditados">Editar datos</md-button>

                            <md-button 
                                class="md-primary"                             
                                ng-if="vm.editar && !vm.editandoDatos"
                                ng-disabled="datos.$invalid" 
                                type="submit">Guardar</md-button>

                            <md-progress-circular 
                                md-mode="indeterminate" 
                                md-diameter="20" 
                                ng-if="vm.editandoDatos">                    
                            </md-progress-circular>

                            <p ng-if="vm.datosEditados">{{vm.textoDatos}}</p>

                        </div>
                    </div>               
                </md-card>    
            </form>
        </div>

        <div class="contenedor" ng-cloak="" style="margin-bottom: 3%;">
            <form name="formClave" ng-submit="vm.cambiarClave(formClave)">
                <md-card layout="row" layout-xs="column">
                    <div flex-sm="33" flex-gt-sm="33" layout-align="center center" layout="column" class="div__monito">
                        <i class="material-icons" style="color: #e0f7fa; font-size: 120px;">&#xE88D;</i>                       
                    </div>
                    <div layout="column" flex>
                        <md-card-content flex layout="column">
                            <h2 class="md-title">Cambiar clave</h2>
                            <md-input-container>
                                <label>Clave actual</label>
                                <input id="claveActual" name="claveActual" ng-model="vm.claves.claveActual" required ng-minlength="4" maxlength="10" type="password">
                                <div ng-messages="formClave.claveActual.$error" md-auto-hide="false" ng-show="formClave.claveActual.$touched" ng-messages-multiple>
                                    <div ng-message="required">Ingresa tu actual clave</div>
                                    <div ng-message="minlength">La clave debe contener como mínimo 4 dígitos</div>
                                </div>
                            </md-input-container>
                            <md-input-container>
                                <label>Nueva clave</label>
                                <input name="nuevaClaveA" ng-model="vm.claves.nuevaClaveA" required ng-minlength="4" maxlength="10" type="password">
                                <div ng-messages="formClave.nuevaClaveA.$error" md-auto-hide="false" ng-show="formClave.nuevaClaveA.$touched" ng-messages-multiple>
                                    <div ng-message="required">Ingresa una nueva clave</div>                                    
                                    <div ng-message="minlength">La clave debe contener como mínimo 4 dígitos</div>
                                </div>
                            </md-input-container>
                            <md-input-container>
                                <label>Confirma la nueva clave</label>
                                <input id="nuevaClaveB" name="nuevaClaveB" ng-disabled="vm.claves.nuevaClaveA == null" ng-pattern="{{vm.claves.nuevaClaveA}}" ng-model="vm.claves.nuevaClaveB" required min="4" maxlength="10" type="password">
                                <div ng-messages="formClave.nuevaClaveB.$error" ng-show="formClave.nuevaClaveB.$touched">
                                    <div ng-message="required">Confirma la nueva clave</div> 
                                    <div ng-message="pattern">Las claves no coinciden</div>                                     
                                </div>
                            </md-input-container>
                        </md-card-content>
                        <div class="md-actions" layout="row" layout-align="end center">
                            <md-progress-circular 
                                md-mode="indeterminate" 
                                md-diameter="20" 
                                ng-if="vm.cambiandoClave">                    
                            </md-progress-circular>
                            <md-button ng-if="!vm.claveFin" class="md-primary" ng-disabled="vm.cambiandoClave" type="submit">Guardar cambios</md-button>
                            <h5 ng-if="vm.claveFin">{{vm.textoClave}}</h5>
                        </div>
                    </div>
                </md-card>
            </form>
        </div>

        <script src="js/angular-animate.min.js"></script>
        <script src="js/angular-aria.min.js"></script>   
        <script src="js/angular-messages.min.js"></script>    
        <script src="js/angular-material.min.js"></script>                
        <script src="recursos-cliente/privado/js/AppMisDatos.js"></script>

    </body>
</html>
