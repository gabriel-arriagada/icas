/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.autoregistrocliente;

import icas.accesodatos.Buscar;
import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.cliente.BuscarClientePorRut;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author Gabriel
 */
@WebServlet(name = "BuscarClientePorRutServlet", urlPatterns = {"/BuscarClientePorRutServlet"})
public class BuscarClientePorRutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("rut") != null) {
            String rut = request.getParameter("rut");            
            Buscar buscarCliente = new BuscarClientePorRut(new ConexionPostgreSql(), rut);
            JSONObject existe = buscarCliente.buscar();
            if (existe != null) {
                response.setStatus(HttpServletResponse.SC_OK);
            }else{
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
