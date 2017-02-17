/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.producto;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Leer;
import icas.accesodatos.producto.LeerProductoParaVendedor;
import icas.dominio.RolActual;
import icas.logicanegocio.sesion.VerificarSesion;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;

/**
 *
 * @author gabo
 */
@WebServlet(name = "LeerProductoVendedorServlet", urlPatterns = {"/LeerProductoVendedorServlet"})
public class LeerProductoVendedorServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {        
        VerificarSesion verificarSesion = new VerificarSesion();
        boolean esVendedor = verificarSesion.verificarSesion(request, RolActual.ROL_VENDEDOR);
        if (esVendedor) {              
            Leer leer = new LeerProductoParaVendedor(new ConexionPostgreSql());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            JSONArray resultado = leer.leer();
            if (resultado != null) {
                out.print(resultado);                
            } else {
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
