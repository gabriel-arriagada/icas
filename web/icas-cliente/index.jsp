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
        boolean esCliente = verificarSesion.verificarSesion(request, RolActual.ROL_CLIENTE);

        if (esCliente) {
            response.sendRedirect("../IndexClienteServlet");
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
        <meta name="theme-color" content="#00BCD4"/>
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
          ng-controller="PantallaIncialController as vm"
          <% if (request.getParameter("irAlCarrito") != null) {
                  boolean irAlCarrito = Boolean.valueOf(request.getParameter("irAlCarrito"));
                  out.print("ng-init='vm.inicio(" + irAlCarrito + ")'");
              } else {
                  out.print("ng-init='vm.inicio(false)'");
              }
          %>
          ng-cloak
          no-click-derecho
          class="colorDeFondo">
        <div flex class="relative" layout-fill>            
            <md-toolbar class="animate-show md-whiteframe-z1 md-medium-tall centrado fuenteToolBar">                
                Cuenta Cafetería               
            </md-toolbar>
            <md-content flex layout="column" id="content" class="colorDeFondo">
                <center>
                    <ng-view></ng-view>
                </center>
            </md-content>
        </div>                      

        <script src="../js/angular.min.js"></script>
        <script src="../js/angular-animate.min.js"></script>
        <script src="../js/angular-aria.min.js"></script>
        <script src="../js/angular-messages.min.js"></script>
        <script src="../js/angular-material.min.js"></script>          
        <script src="../js/angular-route.min.js"></script>   

        <script src="js/LogInApp.js"></script>
        <script src="js/LogInController.js"></script>
        <script src="js/RegistrarController.js"></script>
        <script src="js/RegistrarFactory.js"></script>
        <script src="js/VerificarRutService.js"></script>
        <script src="js/DirectivasDeValidacion.js"></script>

    </body>
</html>
