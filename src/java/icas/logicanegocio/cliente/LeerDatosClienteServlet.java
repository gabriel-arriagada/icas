/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.cliente;

import icas.accesodatos.Buscar;
import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.cliente.BuscarClientePorRut;
import icas.dominio.RolActual;
import icas.dominio.Usuario;
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
 * @author Gabriel
 */
@WebServlet(name = "LeerDatosClienteServlet", urlPatterns = {"/LeerDatosClienteServlet"})
public class LeerDatosClienteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerificarSesion verificarSesion = new VerificarSesion();
        if (verificarSesion.verificarSesion(request, RolActual.ROL_CLIENTE) == true) {
            Usuario usuario = verificarSesion.getUsuario();
            Buscar getDatos = new BuscarClientePorRut(new ConexionPostgreSql(), usuario.getRut());
            JSONObject datos = getDatos.buscar();
            if (datos != null) {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();
                out.print(datos);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
