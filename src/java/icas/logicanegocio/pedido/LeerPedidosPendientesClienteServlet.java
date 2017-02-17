/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.pedido;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Leer;
import icas.accesodatos.pedido.LeerPedidosPendientesCliente;
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
@WebServlet(name = "LeerPedidosPendientesClienteServlet", urlPatterns = {"/LeerPedidosPendientesClienteServlet"})
public class LeerPedidosPendientesClienteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerificarSesion vs = new VerificarSesion();
        if (vs.verificarSesion(request, RolActual.ROL_CLIENTE)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            Leer getPedidos = new LeerPedidosPendientesCliente(new ConexionPostgreSql(), vs.getUsuario().getRut());
            out.print(getPedidos.leer());
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
