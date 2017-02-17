/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.cuenta;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.cuenta.CargarCuenta;
import icas.accesodatos.empleado.VerificarClaveEmpleado;
import icas.dominio.Cuenta;
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
@WebServlet(name = "CargarCuentaServlet", urlPatterns = {"/CargarCuentaServlet"})
public class CargarCuentaServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerificarSesion verificarSesion = new VerificarSesion();
        boolean esAdmin = verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR);
        boolean esVendedor = verificarSesion.verificarSesion(request, RolActual.ROL_VENDEDOR);

        if (esAdmin || esVendedor) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject objetoJson = (JSONObject) parser.parse(request.getReader());

                String claveEmpleado = objetoJson.get("clave").toString();
                Operacion verificarClave = new VerificarClaveEmpleado(new ConexionPostgreSql(), new Empleado(verificarSesion.getUsuario().getRut(), claveEmpleado));

                if (verificarClave.ejecutar()) {
                    String rut = objetoJson.get("rut").toString();
                    int montoCarga = Integer.parseInt(objetoJson.get("montoCarga").toString());

                    if (!rut.equals("") && montoCarga > 0) {
                        Cuenta cuenta = new Cuenta();
                        cuenta.setRut(rut);
                        cuenta.setSaldo(montoCarga);

                        Operacion editar = new CargarCuenta(new ConexionPostgreSql(), verificarSesion.getUsuario().getRut(), cuenta);

                        if (editar.ejecutar() == true) {
                            response.setStatus(HttpServletResponse.SC_OK);
                        } else {
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }

            } catch (ParseException ex) {
                Logger.getLogger(CargarCuentaServlet.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
