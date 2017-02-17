/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.producto;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.producto.AumentarStockProducto;
import icas.dominio.Producto;
import icas.dominio.RolActual;
import icas.logicanegocio.sesion.VerificarSesion;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author gabo
 */
@WebServlet(name = "AumentarStockProductoServlet", urlPatterns = {"/AumentarStockProductoServlet"})
public class AumentarStockProductoServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status;
        VerificarSesion verificarSesion = new VerificarSesion();
        boolean esVendedor = verificarSesion.verificarSesion(request, RolActual.ROL_VENDEDOR);
        if (esVendedor) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject objetoJson = (JSONObject) parser.parse(request.getReader());
                int nuevoStock = Integer.parseInt(objetoJson.get("stock").toString());
                String idProducto = objetoJson.get("idProducto").toString();
                Producto producto = new Producto();
                producto.setIdProducto(idProducto);
                producto.setStock(nuevoStock);
                Operacion aumentarStock = new AumentarStockProducto(new ConexionPostgreSql(), producto);
                if (aumentarStock.ejecutar()) {
                    status = HttpServletResponse.SC_OK;
                } else {
                    status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                }
            } catch (ParseException ex) {
                Logger.getLogger(AumentarStockProductoServlet.class.getName()).log(Level.SEVERE, null, ex);
                status = HttpServletResponse.SC_BAD_REQUEST;
            }
        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED;
        }
        response.setStatus(status);
    }

}
