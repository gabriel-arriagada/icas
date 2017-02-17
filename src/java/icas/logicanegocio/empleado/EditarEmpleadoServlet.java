/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.empleado;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.empleado.EditarEmpleado;
import icas.dominio.Empleado;
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
@WebServlet(name = "EditarEmpleadoServlet", urlPatterns = {"/EditarEmpleadoServlet"})
public class EditarEmpleadoServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status;
        VerificarSesion verificarSesion = new VerificarSesion();
        if (verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR)) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject objetoJson = (JSONObject) parser.parse(request.getReader());                
                String rut = objetoJson.get("rut").toString();
                Empleado empleado = new Empleado();
                String nombre = objetoJson.get("nombre").toString();
                String apellido = objetoJson.get("apellido").toString();
                String correo = objetoJson.get("correo").toString();
                empleado.setNombre(nombre);
                empleado.setApellido(apellido);
                empleado.setCorreo(correo);
                empleado.setRut(rut);
                Operacion editar = new EditarEmpleado(new ConexionPostgreSql(), empleado);
                if (editar.ejecutar() == true) {
                    status = HttpServletResponse.SC_OK;
                } else {
                    status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                }
        }catch (ParseException ex) {
                Logger.getLogger(EditarEmpleadoServlet.class.getName()).log(Level.SEVERE, null, ex);
                status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            }
    }

    
        else {
            status = HttpServletResponse.SC_UNAUTHORIZED;
    }

    response.setStatus (status);
}

}
