/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.producto;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.producto.CrearProducto;
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
@WebServlet(name = "CrearProductoServlet", urlPatterns = {"/CrearProductoServlet"})
public class CrearProductoServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int status;
        VerificarSesion verificarSesion = new VerificarSesion();
        if (verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR) == true) {
            try {
                /*Recepción*/
                JSONParser parser = new JSONParser();
                JSONObject objetoJson = (JSONObject) parser.parse(request.getReader());
                String idProducto = objetoJson.get("idProducto").toString();
                int idCategoria = Integer.parseInt(objetoJson.get("idCategoria").toString());
                String nombre = objetoJson.get("nombre").toString();
                int stock = Integer.parseInt(objetoJson.get("stock").toString());
                int precioCompra = Integer.parseInt(objetoJson.get("precioCompra").toString());
                int precioVenta = Integer.parseInt(objetoJson.get("precioVenta").toString());
                
                
                /*Acceso a datos*/                                                
                Producto producto = new Producto();
                producto.setIdProducto(idProducto);
                producto.setIdCategoria(idCategoria);
                producto.setNombre(nombre);
                producto.setStock(stock);
                producto.setPrecioCompra(precioCompra);
                producto.setPrecioVenta(precioVenta);
                producto.setVigente(true);                
                Operacion crear = new CrearProducto(new ConexionPostgreSql(), producto);
                
                if (crear.ejecutar()== true) {
                    status = HttpServletResponse.SC_OK;//200
                } else {
                    status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;//500
                }
            } catch (ParseException ex) {
                Logger.getLogger(CrearProductoServlet.class.getName()).log(Level.SEVERE, null, ex);
                status = HttpServletResponse.SC_BAD_REQUEST;
            }
        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED;//401
        }
        response.setStatus(status);
    }

}
