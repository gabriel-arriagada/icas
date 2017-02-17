/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.vigenciaempleado;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Leer;
import icas.accesodatos.autoventavendedor.LeerRazonesAutoVenta;
import icas.accesodatos.vigenciaempleado.LeerRazonesVigenciaEmpleado;
import icas.dominio.RolActual;
import icas.logicanegocio.sesion.VerificarSesion;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gabo
 */
@WebServlet(name = "LeerRazonesVigenciaEmpleadoServlet", urlPatterns = {"/LeerRazonesVigenciaEmpleadoServlet"})
public class LeerRazonesVigenciaEmpleadoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VerificarSesion verificarSesion = new VerificarSesion();

        if (verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR) == true) {

            if (request.getParameter("razonesPositivas") != null) {
                boolean razonesPositivas =  Boolean.valueOf(request.getParameter("razonesPositivas"));
                Leer leerRazonesVigenciaEmpleado = new LeerRazonesVigenciaEmpleado(new ConexionPostgreSql(), razonesPositivas);
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();
                out.print(leerRazonesVigenciaEmpleado.leer());
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log("Intento de acceso a servlet Categorias método GET");
        }
    }

}
