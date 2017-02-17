/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.dominio;

/**
 *
 * @author gabo
 */
public enum EstadoPedido {
    
    RECIBIDO(1), 
    EN_PREPARACION(2),
    PREPARADO(3),
    ENTREGADO(4);
    
    private final int idEstado;

    private EstadoPedido(int idEstado) {
        this.idEstado = idEstado;
    }

    public int getIdEstado() {
        return idEstado;
    }       
    
    
}
