/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.autoventavendedor;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.autoventavendedor.CrearAutoVenta;
import icas.dominio.AutoVenta;
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
@WebServlet(name = "CrearAutoVentaServlet", urlPatterns = {"/CrearAutoVentaServlet"})
public class CrearAutoVentaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerificarSesion verificarSesion = new VerificarSesion();
        if (verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR) == true) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject objetoJson = (JSONObject) parser.parse(request.getReader());
                log(objetoJson.toJSONString());
                int idRazon = Integer.parseInt(objetoJson.get("idRazon").toString());
                boolean autoVenta = Boolean.valueOf(objetoJson.get("autoVenta").toString());
                log(String.valueOf(autoVenta));
                String rutVendedor = objetoJson.get("rutVendedor").toString();
                AutoVenta av = new AutoVenta();
                av.setAutoVenta(autoVenta);
                av.setIdRazon(idRazon);
                av.setRutVendedor(rutVendedor);                
                Operacion crear = new CrearAutoVenta(new ConexionPostgreSql(), av);
                if (crear.ejecutar() == true) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } catch (ParseException ex) {
                Logger.getLogger(CrearAutoVentaServlet.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
