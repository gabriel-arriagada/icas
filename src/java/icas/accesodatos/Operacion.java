/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos;

/**
 *
 * @author gabo
 */
public abstract class Operacion {
    protected Conexion conexion;
    public abstract boolean ejecutar();
}
