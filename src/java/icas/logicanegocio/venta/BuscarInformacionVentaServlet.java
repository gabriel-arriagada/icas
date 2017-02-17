/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.venta;

import icas.accesodatos.Buscar;
import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.venta.BuscarInformacionVenta;
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
@WebServlet(name = "BuscarInformacionVentaServlet", urlPatterns = {"/BuscarInformacionVentaServlet"})
public class BuscarInformacionVentaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VerificarSesion verificarSesion = new VerificarSesion();
        boolean esAdmin = verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR);
        boolean esVendedor = verificarSesion.verificarSesion(request, RolActual.ROL_VENDEDOR);
        if (esAdmin || esVendedor) {
            if (request.getParameter("idPedido") != null) {
                int idPedido = Integer.parseInt(request.getParameter("idPedido"));
                Buscar buscar = new BuscarInformacionVenta(new ConexionPostgreSql(), idPedido);
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();
                out.print(buscar.buscar());
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
