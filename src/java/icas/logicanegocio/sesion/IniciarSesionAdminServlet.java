/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.sesion;

import icas.accesodatos.BuscarT;
import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.empleado.VerificarEmpleado;
import icas.dominio.Empleado;
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
@WebServlet(name = "IniciarSesionAdminServlet", urlPatterns = {"/IniciarSesionAdminServlet"})
public class IniciarSesionAdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("rut") != null
                && request.getParameter("clave") != null) {

            BuscarT<Empleado> verificar = new VerificarEmpleado(
                    new ConexionPostgreSql(),
                    new Empleado(
                            request.getParameter("rut"),
                            request.getParameter("clave")));

            Empleado empleado = verificar.buscar();
            
            if (empleado != null) {
                String rol = empleado.getRol();
                switch (rol) {
                    case RolActual.ROL_ADMINISTRADOR: {
                        Usuario administrador = new Usuario();
                        administrador.setRut(empleado.getRut());
                        administrador.setNombre(empleado.getNombre());
                        administrador.setApellido(empleado.getApellido());
                        administrador.setRol(empleado.getRol());
                        HttpSession session = request.getSession(true);
                        session.setAttribute("usuario", administrador);
                        response.sendRedirect("IndexAdministradorServlet");
                        break;
                    }
                    case RolActual.ROL_VENDEDOR: {
                        Usuario vendedor = new Usuario();
                        vendedor.setRut(empleado.getRut());
                        vendedor.setNombre(empleado.getNombre());
                        vendedor.setApellido(empleado.getApellido());
                        vendedor.setRol(empleado.getRol());
                        HttpSession session = request.getSession(true);
                        session.setAttribute("usuario", vendedor);
                        response.sendRedirect("IndexVendedorServlet");
                        break;
                    }
                }
            } else {
                response.sendRedirect("icas-admin");
            }
        }
    }
}
