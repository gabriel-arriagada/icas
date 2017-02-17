/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.vigenciaempleado;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.vigenciaempleado.CrearVigenciaEmpleado;
import icas.dominio.RolActual;
import icas.dominio.VigenciaEmpleado;
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
@WebServlet(name = "CrearVigenciaEmpleadoServlet", urlPatterns = {"/CrearVigenciaEmpleadoServlet"})
public class CrearVigenciaEmpleadoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerificarSesion verificarSesion = new VerificarSesion();
        if (verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR) == true) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject objetoJson = (JSONObject) parser.parse(request.getReader());
                log(objetoJson.toJSONString());
                int idRazon = Integer.parseInt(objetoJson.get("idRazon").toString());
                boolean vigente = Boolean.valueOf(objetoJson.get("vigente").toString());
                log(String.valueOf(vigente));
                String rutVendedor = objetoJson.get("rutVendedor").toString();
                VigenciaEmpleado vigenciaEmpleado = new VigenciaEmpleado();
                vigenciaEmpleado.setVigente(vigente);
                vigenciaEmpleado.setIdRazon(idRazon);
                vigenciaEmpleado.setRutVendedor(rutVendedor);                
                Operacion crear = new CrearVigenciaEmpleado(new ConexionPostgreSql(), vigenciaEmpleado);
                if (crear.ejecutar() == true) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } catch (ParseException ex) {
                Logger.getLogger(CrearVigenciaEmpleadoServlet.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
