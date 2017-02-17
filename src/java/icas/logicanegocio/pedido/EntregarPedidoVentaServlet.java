/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.pedido;


import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.pedido.EntregarPedidoVenta;
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
@WebServlet(name = "EntregarPedidoVentaServlet", urlPatterns = {"/EntregarPedidoVentaServlet"})
public class EntregarPedidoVentaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        VerificarSesion vs = new VerificarSesion();
        boolean isAdmin = vs.verificarSesion(request, RolActual.ROL_ADMINISTRADOR);
        boolean isVendedor = vs.verificarSesion(request, RolActual.ROL_VENDEDOR);
        
        if (isAdmin || isVendedor) {
            try {
                JSONParser parser = new JSONParser();                
                JSONObject objetoJson = (JSONObject) parser.parse(request.getReader());
                int idPedido = Integer.parseInt(objetoJson.get("idPedido").toString());
                int idEstado = Integer.parseInt(objetoJson.get("idEstado").toString());
                int idVenta = Integer.parseInt(objetoJson.get("idVenta").toString());                                
                
                Operacion entregarPedido = new EntregarPedidoVenta(new ConexionPostgreSql(), idPedido, idEstado, idVenta);
                
                if (entregarPedido.ejecutar() == true) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
            } catch (ParseException ex) {
                Logger.getLogger(EntregarPedidoVentaServlet.class.getName()).log(Level.SEVERE, null, ex);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
