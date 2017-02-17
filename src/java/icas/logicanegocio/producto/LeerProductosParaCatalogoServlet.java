/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.logicanegocio.producto;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Leer;
import icas.accesodatos.producto.LeerProductoParaCatalogo;
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
@WebServlet(name = "LeerProductosParaCatalogoServlet", urlPatterns = {"/LeerProductosParaCatalogoServlet"})
public class LeerProductosParaCatalogoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        if (request.getParameter("idCategoria") != null) {
            int idCategoria  = Integer.parseInt(request.getParameter("idCategoria"));
            Leer leer = new LeerProductoParaCatalogo(new ConexionPostgreSql(), idCategoria);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.print(leer.leer());
        }
    }
}
