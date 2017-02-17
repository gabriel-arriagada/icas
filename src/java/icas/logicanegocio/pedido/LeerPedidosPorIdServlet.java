/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.pedido;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Leer;
import icas.accesodatos.pedido.LeerPedidoPorId;
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
@WebServlet(name = "LeerPedidosPorIdServlet", urlPatterns = {"/LeerPedidosPorIdServlet"})
public class LeerPedidosPorIdServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerificarSesion vs = new VerificarSesion();
        if (vs.verificarSesion(request, RolActual.ROL_ADMINISTRADOR)
                || vs.verificarSesion(request, RolActual.ROL_VENDEDOR)) {
            if (request.getParameter("idPedido") != null) {
                String idPedido = request.getParameter("idPedido");
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();
                Leer buscarPedidoPorId = new LeerPedidoPorId(new ConexionPostgreSql(), idPedido);
                out.print(buscarPedidoPorId.leer());
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
