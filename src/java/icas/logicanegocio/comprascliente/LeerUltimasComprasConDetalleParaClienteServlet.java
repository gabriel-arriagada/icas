/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.comprascliente;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Leer;
import icas.accesodatos.comprascliente.LeerUltimasComprasConDetalle;
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

/**
 *
 * @author Gabriel
 */
@WebServlet(name = "LeerUltimasComprasConDetalleParaClienteServlet", urlPatterns = {"/LeerUltimasComprasConDetalleParaClienteServlet"})
public class LeerUltimasComprasConDetalleParaClienteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VerificarSesion vf = new VerificarSesion();
        if (vf.verificarSesion(request, RolActual.ROL_CLIENTE) == true) {
            Usuario usuario = vf.getUsuario();
            Leer leer = new LeerUltimasComprasConDetalle(new ConexionPostgreSql(), usuario.getRut());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.print(leer.leer());
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
