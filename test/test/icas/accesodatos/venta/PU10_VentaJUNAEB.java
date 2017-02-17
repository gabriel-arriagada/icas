/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.icas.accesodatos.venta;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.producto.EliminarProducto;
import icas.accesodatos.venta.Contexto;
import icas.accesodatos.venta.VentaJUNAEB;
import icas.dominio.Producto;
import icas.dominio.Venta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author gabo
 */
public class PU10_VentaJUNAEB {

    private JSONArray resultadoVenta;
    
    public PU10_VentaJUNAEB(){
        resultadoVenta = new JSONArray();
    }
    
    @Before
    public void setUp(){
        PU10_CrearProductosDePrueba crear = new PU10_CrearProductosDePrueba();
        crear.crearProducto("productoA", "Nombre" , 1, 100, 200, 10, true);
        crear.crearProducto("productoB", "Nombre" , 1, 100, 200, 20, true);
    }
    
    @After
    public void tearDown(){
        Producto productoA = new Producto();
        productoA.setIdProducto("productoA");
        Producto productoB = new Producto();
        productoA.setIdProducto("productoB");
        Operacion eliminarA = new EliminarProducto(new ConexionPostgreSql(), productoA);
        Operacion eliminarB = new EliminarProducto(new ConexionPostgreSql(), productoB);
    }
    
    @Test
    public void pruebaA() {
        JSONArray detalleVenta = new JSONArray();
        JSONObject productoA = new JSONObject();
        productoA.put("idProducto", "p");
        Venta venta = new Venta();
        venta.setRutVendedor("1-9");
        venta.setRutCliente("17334973-4");
        venta.setIdFormaPago(2);
        venta.setHoraDeRetiro("10:00");
        venta.setTienePedido(true);
        venta.setPagada(true);
        Contexto ventaJUNAEB = new Contexto(new VentaJUNAEB(new ConexionPostgreSql(), detalleVenta, venta));
        resultadoVenta = ventaJUNAEB.ejecutarAlgoritmo();
    }

    @Test
    public void pruebaB() {
    }

    @Test
    public void pruebaC() {
    }
    
}
