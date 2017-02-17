<%@page import="icas.dominio.RolActual"%>
<%@page import="icas.logicanegocio.sesion.VerificarSesion"%>
<%@page import="icas.dominio.GetBrowser"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% GetBrowser browser = new GetBrowser();
    if (!browser.esChrome(request)) {
        response.sendRedirect("../error/navegador.html");
    } else {
        VerificarSesion verificarSesion = new VerificarSesion();
        boolean esAdmin = verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR);
        boolean esVendedor = verificarSesion.verificarSesion(request, RolActual.ROL_VENDEDOR);

        if (esAdmin) {
            response.sendRedirect("../IndexAdministradorServlet");
        } else if (esVendedor) {
            response.sendRedirect("../IndexVendedorServlet");
        }
    }
%>
<html>
    <head>
        <noscript>
        <meta http-equiv="refresh" content="0;url=../error/no-script.html">
        </noscript>
        <title>Iniciar sesión</title>
        <link rel="shortcut icon" href="../img/icas.ico" />
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">        
        <link rel='stylesheet' href='../font/roboto.css'>
        <link rel="stylesheet" href="../css/angular-material.min.css">        
        <style>                        
            .centrado {                
                display: -webkit-box;
                display: -moz-box;
                display: -ms-flexbox;
                display: -webkit-flex;
                display: flex;

                -webkit-box-align: center;
                -moz-box-align: center;
                -ms-flex-align: center;
                -webkit-align-items: center;
                align-items: center;

                -webkit-box-pack: center;
                -moz-box-pack: center;
                -ms-flex-pack: center;
                -webkit-justify-content: center;
                justify-content: center;
            }

            .colorDeFondo{
                background-color: #EEEEEE;
            }

            .arreglosCard{
                margin-top: 40px;
                margin-bottom: 40px;
                padding-top: 20px;   
                max-width: 450px;                  
            }

            .fuenteToolBar{
                font-size: 25px;
            }

            .izquierda{
                text-align: left;
            }
        </style>
    </head>
    <body ng-app="LogInApp"           
          ng-controller="LogInController"
          ng-cloak
          no-click-derecho
          class="colorDeFondo">

        <div flex class="relative" layout-fill>            
            <md-toolbar class="animate-show md-whiteframe-z1 md-medium-tall centrado fuenteToolBar">                
                Acceso a panel de control               
            </md-toolbar>
            <md-content flex layout="column" id="content" class="colorDeFondo">
                <center>
                    <md-card class="arreglosCard md-padding">
                        <md-icon md-svg-icon="usuario" style="color: lightgray;width: 150px;height: 150px;"></md-icon>
                        <center><p style="font-weight: bold; color: #cccccc;">Ingresa con tu Cuenta</p></center>
                        <form name="logIn" action="../IniciarSesionAdminServlet" method="post">
                            <md-card-content>  
                                <md-input-container class="md-block izquierda">
                                    <label>Rut</label>                            
                                    <input name="rut"                        
                                           style="font-size: 20px;"
                                           ng-pattern="/^[0-9]+-[0-9kK]{1}$/"
                                           maxlength="10" ng-model="vm.logIn.rut" 
                                           valida-rut                       
                                           required>
                                    <div ng-messages="logIn.rut.$error" md-auto-hide="false" ng-show="logIn.rut.$touched" ng-messages-multiple>
                                        <div ng-message="required">Debes ingresar un rut.</div>
                                        <div ng-message="rutInvalido">El rut no es válido.</div>                                      
                                        <div ng-message="pattern">Debes ingresar un rut con el formato correcto.</div>
                                    </div>
                                </md-input-container>
                                <md-input-container class="md-block izquierda">
                                    <label>Clave</label>                            
                                    <input style="font-size: 20px;" 
                                           ng-model="vm.logIn.clave"                                                                            
                                           name="clave" type="password" 
                                           required>
                                    <div ng-messages="logIn.clave.$error" md-auto-hide="false" ng-show="logIn.clave.$touched">
                                        <div ng-message="required">Ingresa tu contrseña</div>                                                                          
                                    </div>
                                </md-input-container>                                                    
                            </md-card-content>     
                            <md-card-actions layout="row" layout-align="start center" style="margin-bottom: 30px;">
                                <md-card-icon-actions style="padding-top: 10px;"></md-card-icon-actions>                                   </md-card-icon-actions>                        
                                <md-button class="md-raised md-primary" type="submit" ng-disabled="logIn.$invalid">Entrar</md-button>                        
                            </md-card-actions> 
                        </form>                                                
                    </md-card>
                </center>
            </md-content>
        </div>                



        <!-- Angular Material requires Angular.js Libraries -->
        <script src="../js/angular.min.js"></script>
        <script src="../js/angular-animate.min.js"></script>
        <script src="../js/angular-aria.min.js"></script>
        <script src="../js/angular-messages.min.js"></script>
        <script src="../js/verificar-rut.js"></script>

        <!-- Angular Material Library -->
        <script src="../js/angular-material.min.js"></script>

        <script src="js/LogInApp.js"></script>
        <script src="js/LogInController.js"></script>        
        <script src="js/VerificarRutService.js"></script>
        <script src="js/DirectivasDeValidacion.js"></script>

    </body>
</html>
