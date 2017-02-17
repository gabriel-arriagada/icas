/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.horaservidor;


import icas.dominio.Hora;
import icas.dominio.TiempoDePedido;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "GetHoraServlet", urlPatterns = {"/GetHoraServlet"})
public class GetHoraServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        JSONObject hora = new JSONObject();
        Hora miHora = new Hora();

        if (request.getParameter("masX") != null) {
                        
            hora.put("hora", miHora.getHoraMasX(TiempoDePedido.TREINTA_MINUTOS));
            
        } else {
            
            hora.put("hora", miHora.getHora());
            hora.put("minimo", "00:" + TiempoDePedido.TREINTA_MINUTOS);

        }        
        out.print(hora);
        
    }

}
