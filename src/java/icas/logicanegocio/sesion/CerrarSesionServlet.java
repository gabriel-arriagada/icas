/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.sesion;

import icas.dominio.RolActual;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gabo
 */
@WebServlet(name = "CerrarSesionServlet", urlPatterns = {"/CerrarSesionServlet"})
public class CerrarSesionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerificarSesion verificarSesion = new VerificarSesion();
        
        boolean esAdmin = verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR);
        boolean esVendedor = verificarSesion.verificarSesion(request, RolActual.ROL_VENDEDOR);
        boolean esCliente = verificarSesion.verificarSesion(request, RolActual.ROL_CLIENTE);
        
        if (esAdmin || esVendedor){
            request.getSession().invalidate();
            response.sendRedirect("icas-admin");
        } else if (esCliente) {
            request.getSession().invalidate();
            response.sendRedirect("icas-cliente");
        } else {
            response.sendRedirect("IndexClienteServlet");
        }
    }
}
