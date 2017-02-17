/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.venta;

import icas.accesodatos.Conexion;
import org.json.simple.JSONArray;

/**
 *
 * @author gabo
 */
public abstract class AbstractVenta {                 
    protected JSONArray detalleVenta;
    protected Conexion conexion;          
    public abstract JSONArray generarVenta();
}
