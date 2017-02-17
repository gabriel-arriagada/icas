/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.venta;

import org.json.simple.JSONArray;

/**
 *
 * @author gabo
 */
public class Contexto {
    
    private final AbstractVenta strategy;
    
    public Contexto(AbstractVenta venta){
        this.strategy = venta;
    }
    
    public JSONArray ejecutarAlgoritmo(){
        return strategy.generarVenta();
    }
}
