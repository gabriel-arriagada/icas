/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.vista.cliente;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.LeerArray;
import icas.accesodatos.categoria.LeerCategoriaParaCatalogo;
import icas.dominio.Categoria;
import icas.dominio.GetBrowser;
import icas.dominio.RolActual;
import icas.logicanegocio.sesion.VerificarSesion;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gabo
 */
@WebServlet(name = "IndexClienteServlet", urlPatterns = {"/IndexClienteServlet"})
public class IndexClienteServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetBrowser getBrowser = new GetBrowser();
        boolean esChrome = getBrowser.esChrome(request);
        if (esChrome) {
            RequestDispatcher dispatcher;
            LeerArray leerCategorias = new LeerCategoriaParaCatalogo(new ConexionPostgreSql());
            ArrayList<Categoria> categorias = leerCategorias.leer();
            if (!categorias.isEmpty()) {
                request.setAttribute("categorias", categorias);
            }
            VerificarSesion verificarSesion = new VerificarSesion();
            boolean esCliente = verificarSesion.verificarSesion(request, RolActual.ROL_CLIENTE);
            if (esCliente) {
                String usuario = verificarSesion.getUsuario().getNombre() + " " + verificarSesion.getUsuario().getApellido();
                String rut = verificarSesion.getUsuario().getRut();
                request.setAttribute("usuario", usuario);
                request.setAttribute("rut", rut);
                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/vistas/catalogo.jsp");
            } else {
                dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/vistas/index.jsp");
            }
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
        } else {
            response.sendRedirect("error/navegador.html");
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
