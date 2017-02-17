/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.pedido;


import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.pedido.EditarEstadoPedido;
import icas.dominio.Pedido;
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
@WebServlet(name = "EditarEstadoPedidoServlet", urlPatterns = {"/EditarEstadoPedidoServlet"})
public class EditarEstadoPedidoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VerificarSesion vs = new VerificarSesion();
        if (vs.verificarSesion(request, RolActual.ROL_ADMINISTRADOR)
                || vs.verificarSesion(request, RolActual.ROL_VENDEDOR)) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject objetoJson = (JSONObject) parser.parse(request.getReader());
                int idPedido = Integer.parseInt(objetoJson.get("idPedido").toString());
                int idEstado = Integer.parseInt(objetoJson.get("idEstado").toString());
                Pedido pedido = new Pedido();
                pedido.setIdPedido(idPedido);
                pedido.setIdEstado(idEstado);
                Operacion cambiarEstadoPedido = new EditarEstadoPedido(new ConexionPostgreSql(), pedido);
                if (cambiarEstadoPedido.ejecutar() == true) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
            } catch (ParseException ex) {
                Logger.getLogger(EditarEstadoPedidoServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
