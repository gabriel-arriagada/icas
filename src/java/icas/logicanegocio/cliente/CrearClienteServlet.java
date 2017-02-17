/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.cliente;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.cliente.CrearCliente;
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
@WebServlet(name = "CrearClienteServlet", urlPatterns = {"/CrearClienteServlet"})
public class CrearClienteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status;
        VerificarSesion verificarSesion = new VerificarSesion();
        if (verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR) == true) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject objetoJson = (JSONObject) parser.parse(request.getReader());
                String rut = objetoJson.get("rut").toString();                
                String nombre = objetoJson.get("nombre").toString();
                String apellido = objetoJson.get("apellido").toString();
                String correo = objetoJson.get("correo").toString();
                String clave = objetoJson.get("clave").toString();                
                Cliente cliente = new Cliente();
                cliente.setRut(rut);                
                cliente.setClave(clave);
                cliente.setNombre(nombre);
                cliente.setApellido(apellido);
                cliente.setCorreo(correo);                
                Operacion crear = new CrearCliente(new ConexionPostgreSql(), cliente);
                
                if (crear.ejecutar()== true) {
                    status = HttpServletResponse.SC_OK;//200
                } else {
                    status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;//500
                }
            } catch (ParseException ex) {
                Logger.getLogger(CrearClienteServlet.class.getName()).log(Level.SEVERE, null, ex);
                status = HttpServletResponse.SC_BAD_REQUEST;
            }
        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED;//401
        }
        response.setStatus(status);

    }

}
