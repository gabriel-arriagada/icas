/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.cliente;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.cliente.EliminarCliente;
import icas.dominio.Cliente;
import icas.dominio.RolActual;
import icas.logicanegocio.sesion.VerificarSesion;
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
@WebServlet(name = "EliminarClienteServlet", urlPatterns = {"/EliminarClienteServlet"})
public class EliminarClienteServlet extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status;
        VerificarSesion verificarSesion = new VerificarSesion();
        if (verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR) == true) {
            if (request.getParameter("rut") != null) {
                String rut = request.getParameter("rut");
                
                Cliente cliente = new Cliente();
                cliente.setRut(rut);
                Operacion eliminar = new EliminarCliente(new ConexionPostgreSql(), cliente);
                if (eliminar.ejecutar()== true) {
                    status = HttpServletResponse.SC_OK;
                } else {
                    status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                }
            } else {
                status = HttpServletResponse.SC_BAD_REQUEST;
            }

        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED;
        }
        response.setStatus(status);
    }
}
