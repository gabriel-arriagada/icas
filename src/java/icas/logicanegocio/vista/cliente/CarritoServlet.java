/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.vista.cliente;

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
 * @author gabo
 */
@WebServlet(name = "CarritoServlet", urlPatterns = {"/CarritoServlet"})
public class CarritoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetBrowser getBrowser = new GetBrowser();
        boolean esChrome = getBrowser.esChrome(request);
        if (esChrome) {
            RequestDispatcher dispatcher;
            VerificarSesion verificarSesion = new VerificarSesion();
            boolean esCliente = verificarSesion.verificarSesion(request, RolActual.ROL_CLIENTE);
            if (esCliente) {
                String nombre = verificarSesion.getUsuario().getNombre() + " " + verificarSesion.getUsuario().getApellido();
                String rut = verificarSesion.getUsuario().getRut();
                request.setAttribute("nombre", nombre);
                request.setAttribute("rut", rut);
                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/vistas/carrito.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("icas-cliente");
            }
        } else {
            response.sendRedirect("error/navegador.html");
        }

    }

}
