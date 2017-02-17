/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.accesodatos;

import org.json.simple.JSONArray;

/**
 *
 * @author Gabriel
 */
public abstract class Leer {
    protected Conexion conexion;
    public abstract JSONArray leer();
}
