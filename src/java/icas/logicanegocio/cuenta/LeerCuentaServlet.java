/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.cuenta;

import icas.accesodatos.Buscar;
import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.cuenta.BuscarCuenta;
import icas.dominio.RolActual;
import icas.logicanegocio.sesion.VerificarSesion;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author gabo
 */
@WebServlet(name = "LeerCuentaServlet", urlPatterns = {"/LeerCuentaServlet"})
public class LeerCuentaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerificarSesion verificarSesion = new VerificarSesion();
        if (verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR) == true
                || verificarSesion.verificarSesion(request, RolActual.ROL_VENDEDOR) == true) {
            if (request.getParameter("rut") != null) {
                String rut = request.getParameter("rut");
                Buscar buscar = new BuscarCuenta(new ConexionPostgreSql(), rut);
                JSONObject cuenta = buscar.buscar();
                if (cuenta != null) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    PrintWriter out = response.getWriter();
                    out.print(cuenta);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
