/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.venta;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.venta.EliminarVenta;
import icas.dominio.RolActual;
import icas.dominio.Venta;
import icas.logicanegocio.sesion.VerificarSesion;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gabo
 */
@WebServlet(name = "EliminarVentaServlet", urlPatterns = {"/EliminarVentaServlet"})
public class EliminarVentaServlet extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerificarSesion verificarSesion = new VerificarSesion();
        if (verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR) == true) {
            if (request.getParameter("idVenta") != null) {
                int idVenta = Integer.parseInt(request.getParameter("idVenta"));                
                Venta venta = new Venta();
                venta.setIdVenta(idVenta);
                Operacion eliminar = new EliminarVenta(new ConexionPostgreSql(), venta);                
                if (eliminar.ejecutar()== true) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


}
