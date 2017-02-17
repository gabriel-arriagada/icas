/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.empleado;

import icas.accesodatos.Buscar;
import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.empleado.BuscarEmpleadoPorRut;
import icas.dominio.RolActual;
import icas.logicanegocio.sesion.VerificarSesion;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author Gabriel
 */
@WebServlet(name = "BuscarEmpleadoPorRutServlet", urlPatterns = {"/BuscarEmpleadoPorRutServlet"})
public class BuscarEmpleadoPorRutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerificarSesion verificar = new VerificarSesion();
        boolean esAdmin = verificar.verificarSesion(request, RolActual.ROL_ADMINISTRADOR);
        if (esAdmin) {
            if (request.getParameter("rut") != null) {
                String rut = request.getParameter("rut");
                Buscar buscarEmpleado = new BuscarEmpleadoPorRut(new ConexionPostgreSql(), rut);
                JSONObject existe = buscarEmpleado.buscar();
                if (existe != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

}
