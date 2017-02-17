/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.imagen;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.producto.EditarUrlImagen;
import icas.dominio.Producto;
import icas.dominio.RolActual;
import icas.dominio.UrlImagen;
import icas.logicanegocio.sesion.VerificarSesion;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author gabo
 */
@WebServlet(name = "GuardarImagenServlet", urlPatterns = {"/GuardarImagenServlet"})
public class GuardarImagenServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status = 0;
        VerificarSesion verificarSesion = new VerificarSesion();
        if (verificarSesion.verificarSesion(request, RolActual.ROL_ADMINISTRADOR) == true) {
            if (request.getParts().isEmpty() == false) {
                ProcesarMultipart procesarMultipart = new ProcesarMultipart();
                Part imagenPart = request.getPart("imagen");
                Part idProductoPart = request.getPart("idProducto");
                String formato = procesarMultipart.getFormato(imagenPart);
                //log(imagenPart.getHeader("content-type"));
                String idProducto = procesarMultipart.getTexto(idProductoPart);

                if (formato != null) {                    
                    Producto producto = new Producto();
                    producto.setIdProducto(idProducto);
                    producto.setUrlImagen(UrlImagen.URL_CARPETA + idProducto + formato);
                    
                    Operacion editar = new EditarUrlImagen(new ConexionPostgreSql(), producto);
                    
                    if (procesarMultipart.guardarImagen(imagenPart, idProducto, formato) == true
                            && editar.ejecutar() == true) {
                        status = HttpServletResponse.SC_OK;
                    } else {
                        status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                    }
                } else {
                    status = HttpServletResponse.SC_BAD_REQUEST;
                }
            } else {
                status = HttpServletResponse.SC_BAD_REQUEST;
            }
        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED;
        }
        response.setStatus(status);
    }

}
