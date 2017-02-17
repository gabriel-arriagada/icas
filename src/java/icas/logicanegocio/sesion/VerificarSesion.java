/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.sesion;

import icas.dominio.RolActual;
import icas.dominio.Usuario;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Gabriel
 */
public class VerificarSesion {

    private Usuario usuario;
    private boolean retorno;

    public VerificarSesion() {
        retorno = false;
        usuario = null;
    }

    public boolean verificarSesion(HttpServletRequest request, String rol) {

        if (request.getSession().getAttribute("usuario") != null) {
            usuario = (Usuario) request.getSession().getAttribute("usuario");
            if (usuario.getRol().equals(rol)) {
                retorno = true;
            }
        }
        return retorno;
    }

    public boolean esReceptorDeComandas(HttpSession httpSession) {        
        this.usuario = (Usuario) httpSession.getAttribute("usuario");
        boolean esAdmin = this.usuario.getRol().equals(RolActual.ROL_ADMINISTRADOR);
        boolean esVendedor = this.usuario.getRol().equals(RolActual.ROL_VENDEDOR);
        if (esAdmin || esVendedor) {
            this.retorno = true;
        }
        return this.retorno;
    }

    public Usuario getUsuario() {
        return usuario;
    }

}
