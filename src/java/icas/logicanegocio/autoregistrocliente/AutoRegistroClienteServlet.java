/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.autoregistrocliente;


import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.cliente.CrearCliente;
import icas.dominio.Cliente;
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
 * @author Gabriel
 */
@WebServlet(name = "AutoRegistroClienteServlet", urlPatterns = {"/AutoRegistroClienteServlet"})
public class AutoRegistroClienteServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status = 0;
        try {
            JSONParser parser = new JSONParser();
            JSONObject objetoJson = (JSONObject) parser.parse(request.getReader());
            if (objetoJson != null) {
                String rut = objetoJson.get("rut").toString();                
                String nombre = objetoJson.get("nombre").toString();
                String apellido = objetoJson.get("apellido").toString();
                String correo = objetoJson.get("correo").toString();
                String clave = objetoJson.get("claveA").toString();                
                Cliente cliente = new Cliente();
                cliente.setRut(rut);                
                cliente.setClave(clave);
                cliente.setNombre(nombre);
                cliente.setApellido(apellido);
                cliente.setCorreo(correo);                
                
                Operacion crear = new CrearCliente(new ConexionPostgreSql(), cliente);
                
                if (crear.ejecutar() == true) {
                    status = HttpServletResponse.SC_OK;//200
                } else {
                    status = HttpServletResponse.SC_CONFLICT;//409
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(AutoRegistroClienteServlet.class.getName()).log(Level.SEVERE, null, ex);
            status = HttpServletResponse.SC_BAD_REQUEST;
        }

        response.setStatus(status);

    }
}
