/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.sesion;

import icas.accesodatos.BuscarT;
import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.cliente.VerificarCliente;
import icas.dominio.Cliente;
import icas.dominio.RolActual;
import icas.dominio.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gabo
 */
@WebServlet(name = "IniciarSesionClienteServlet", urlPatterns = {"/IniciarSesionClienteServlet"})
public class IniciarSesionClienteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("rut") != null && request.getParameter("clave") != null) {
            boolean irAlCarrito = false;
            if (request.getParameter("irAlCarrito") != null) {
                irAlCarrito = Boolean.valueOf(request.getParameter("irAlCarrito"));
            }
            BuscarT<Cliente> verificar = new VerificarCliente(new ConexionPostgreSql(), new Cliente(request.getParameter("rut"), request.getParameter("clave")));
            Cliente cliente = verificar.buscar();

            if (cliente != null) {
                Usuario usuarioCliente = new Usuario();
                usuarioCliente.setRut(cliente.getRut());
                usuarioCliente.setNombre(cliente.getNombre());
                usuarioCliente.setApellido(cliente.getApellido());
                usuarioCliente.setRol(RolActual.ROL_CLIENTE);
                HttpSession session = request.getSession(false);
                session.setAttribute("usuario", usuarioCliente);
                if (irAlCarrito) {
                    response.sendRedirect("CarritoServlet");
                } else {
                    response.sendRedirect("IndexClienteServlet");
                }

            } else {
                response.sendRedirect("icas-cliente");
            }

        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
