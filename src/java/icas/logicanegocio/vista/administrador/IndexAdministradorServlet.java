/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.vista.administrador;

import icas.dominio.GetBrowser;
import icas.dominio.RolActual;
import icas.logicanegocio.sesion.VerificarSesion;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gabriel
 */
@WebServlet(name = "IndexAdministradorServlet", urlPatterns = {"/IndexAdministradorServlet"})
public class IndexAdministradorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetBrowser getBrowser = new GetBrowser();
        boolean esChrome = getBrowser.esChrome(request);
        if (esChrome) {
            VerificarSesion verificarSesion = new VerificarSesion();
            boolean esAdmin = verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR);
            if (esAdmin) {
                RequestDispatcher dispatcher = getServletContext()
                        .getRequestDispatcher("/WEB-INF/vistas/administrador.jsp");
                request.setAttribute("usuario", verificarSesion.getUsuario());
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("icas-admin");
            }
        } else {
            response.sendRedirect("error/navegador.html");
        }

    }
}
