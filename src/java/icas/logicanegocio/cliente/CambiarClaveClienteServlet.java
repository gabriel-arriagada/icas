/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.cliente;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.cliente.CambiarClaveCliente;
import icas.accesodatos.cliente.VerificarClaveCliente;
import icas.dominio.Cliente;
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
@WebServlet(name = "CambiarClaveClienteServlet", urlPatterns = {"/CambiarClaveClienteServlet"})
public class CambiarClaveClienteServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status = 0;
        VerificarSesion verificarSesion = new VerificarSesion();
        boolean esCliente = verificarSesion.verificarSesion(request, RolActual.ROL_CLIENTE);
        if (esCliente) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject objetoJson = (JSONObject) parser.parse(request.getReader());
                String rut = verificarSesion.getUsuario().getRut();
                String clave = objetoJson.get("claveActual").toString();
                String nuevaClave = objetoJson.get("nuevaClaveA").toString();
                Cliente cliente = new Cliente();
                cliente.setRut(rut);
                cliente.setClave(clave);
                Operacion verificarClave = new VerificarClaveCliente(new ConexionPostgreSql(), cliente);
                if (verificarClave.ejecutar() == true) {
                    Operacion cambiarClave = new CambiarClaveCliente(new ConexionPostgreSql(), rut, nuevaClave);
                    if (cambiarClave.ejecutar()) {
                        status = HttpServletResponse.SC_OK;//200
                    } else {
                        status = HttpServletResponse.SC_CONFLICT;//409
                    }
                } else {
                    //Bad request para cuando la contraseña actual es incorrecta.
                    status = HttpServletResponse.SC_BAD_REQUEST;//400
                }
            } catch (ParseException ex) {
                Logger.getLogger(CambiarClaveClienteServlet.class.getName()).log(Level.SEVERE, null, ex);
                status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            }
        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED;
        }
        response.setStatus(status);
    }

}
