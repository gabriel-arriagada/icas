/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.venta;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.venta.Contexto;
import icas.accesodatos.venta.VentaEfectivo;
import icas.accesodatos.venta.VentaJUNAEB;
import icas.accesodatos.venta.VentaPrepago;
import icas.dominio.RolActual;
import icas.dominio.Venta;
import icas.logicanegocio.sesion.VerificarSesion;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author gabo
 */
@WebServlet(name = "FinalizarVentaAppServlet", urlPatterns = {"/FinalizarVentaAppClienteServlet"})
public class FinalizarVentaAppClienteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status = 0;
        VerificarSesion vs = new VerificarSesion();
        if (vs.verificarSesion(request, RolActual.ROL_CLIENTE) == true) {
            try {
                JSONParser parser = new JSONParser();
                JSONArray ventaDesdeCliente = (JSONArray) parser.parse(request.getReader());

                JSONObject datosVenta = (JSONObject) ventaDesdeCliente.get(0);
                JSONArray detalleVenta = (JSONArray) ventaDesdeCliente.get(1);

                int idFormaPago = Integer.parseInt(datosVenta.get("idFormaPago").toString());
                String horaDeRetiro = datosVenta.get("horaDeRetiro").toString();
                String comentario = null;
                if (datosVenta.get("comentario") != null) {
                    comentario = datosVenta.get("comentario").toString();
                }

                JSONArray resultadoVenta = null;
                Venta venta = new Venta();
                venta.setRutVendedor("1-9");
                venta.setRutCliente(vs.getUsuario().getRut());
                venta.setIdFormaPago(idFormaPago);
                venta.setHoraDeRetiro(horaDeRetiro);
                venta.setTienePedido(true);
                venta.setComentario(comentario);

                switch (idFormaPago) {
                    case 1:/*Pre pago*/

                        venta.setPagada(true);
                        Contexto ventaPrepago = new Contexto(new VentaPrepago(new ConexionPostgreSql(), detalleVenta, venta));
                        resultadoVenta = ventaPrepago.ejecutarAlgoritmo();
                        break;
                    case 2:/*JUNAEB*/

                        venta.setPagada(false);
                        Contexto ventaJUNAEB = new Contexto(new VentaJUNAEB(new ConexionPostgreSql(), detalleVenta, venta));
                        resultadoVenta = ventaJUNAEB.ejecutarAlgoritmo();
                        break;
                    case 3:/*Efectivo*/

                        venta.setPagada(false);
                        Contexto ventaEfectivo = new Contexto(new VentaEfectivo(new ConexionPostgreSql(), detalleVenta, venta));
                        resultadoVenta = ventaEfectivo.ejecutarAlgoritmo();
                        break;
                }

                JSONObject exito = (JSONObject) resultadoVenta.get(0);
                if ((boolean) exito.get("exito") == true) {
                    status = HttpServletResponse.SC_OK;
                } else {
                    status = HttpServletResponse.SC_BAD_REQUEST;
                }
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();
                out.print(resultadoVenta);
            } catch (ParseException ex) {
                Logger.getLogger(FinalizarVentaAppClienteServlet.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED;//401
        }
        response.setStatus(status);
    }
}
