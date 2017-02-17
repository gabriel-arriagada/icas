/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.producto;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.producto.CambiarEstadoProducto;
import icas.accesodatos.producto.EditarProducto;
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
@WebServlet(name = "EditarProductoServlet", urlPatterns = {"/EditarProductoServlet"})
public class EditarProductoServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status = 0;
        VerificarSesion verificarSesion = new VerificarSesion();
        if (verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR)) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject objetoJson = (JSONObject) parser.parse(request.getReader());
                int accion = Integer.parseInt(objetoJson.get("accion").toString());
                String idProducto = objetoJson.get("idProducto").toString();
                Producto producto = new Producto();
                switch(accion)
                {
                    case 200:                        
                        int idCategoria = Integer.parseInt(objetoJson.get("idCategoria").toString());
                        String nombre = objetoJson.get("nombre").toString();
                        int stock = Integer.parseInt(objetoJson.get("stock").toString());
                        int precioCompra = Integer.parseInt(objetoJson.get("precioCompra").toString());
                        int precioVenta = Integer.parseInt(objetoJson.get("precioVenta").toString());                        
                        producto.setIdProducto(idProducto);
                        producto.setIdCategoria(idCategoria);
                        producto.setNombre(nombre);
                        producto.setStock(stock);
                        producto.setPrecioCompra(precioCompra);
                        producto.setPrecioVenta(precioVenta);
                        
                        Operacion editar = new EditarProducto(new ConexionPostgreSql(), producto);
                        
                        if (editar.ejecutar()== true) {
                            status = HttpServletResponse.SC_OK;
                        } else {
                            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                        }      
                        break;
                    case 100:
                        boolean vigente = (boolean)objetoJson.get("vigente");                        
                        producto.setIdProducto(idProducto);
                        producto.setVigente(vigente);
                        Operacion cambiarEstado = new CambiarEstadoProducto(new ConexionPostgreSql(), producto);
                        if(cambiarEstado.ejecutar()== true){
                            status = HttpServletResponse.SC_OK;
                        } else {
                            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                        }
                        break;
                }                        
            } catch (ParseException ex) {
                Logger.getLogger(EditarProductoServlet.class.getName()).log(Level.SEVERE, null, ex);
                status = HttpServletResponse.SC_BAD_REQUEST;
                throw new RuntimeException(ex.getMessage());
            }
        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED;
        }
        response.setStatus(status);
    }

   
   

}
