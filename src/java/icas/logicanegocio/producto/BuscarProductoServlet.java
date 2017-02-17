/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.producto;

import icas.logicanegocio.sesion.VerificarSesion;
import icas.dominio.RolActual;
import icas.accesodatos.Buscar;
import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.producto.BuscarProducto;
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
@WebServlet(name = "BuscarProductoServlet", urlPatterns = {"/BuscarProductoServlet"})
public class BuscarProductoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerificarSesion verificarSesion = new VerificarSesion();
        if (verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR) == true ||
                verificarSesion.verificarSesion(request, RolActual.ROL_VENDEDOR) == true) {
            if (request.getParameter("idProducto") != null && request.getParameter("cantidad") != null) {
                String idProducto = request.getParameter("idProducto");
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                Buscar buscar = new BuscarProducto(new ConexionPostgreSql(), idProducto);
                JSONObject producto = buscar.buscar();
                if (producto != null) {
                    if (Integer.parseInt(producto.get("stock").toString()) >= cantidad) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("utf-8");
                        PrintWriter out = response.getWriter();
                        out.print(producto);
                    } else {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                    }
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
