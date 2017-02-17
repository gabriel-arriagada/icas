/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos;

import org.json.simple.JSONObject;

/**
 *
 * @author gabo
 */
public abstract class Buscar {
    protected Conexion conexion;
    public abstract JSONObject buscar();
}
