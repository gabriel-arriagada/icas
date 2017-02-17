/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.icas.accesodatos.venta;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.producto.CrearProducto;
import icas.dominio.Producto;

/**
 *
 * @author gabo
 */
public class PU10_CrearProductosDePrueba {
    public void crearProducto(String id, String nombre, int idCategoria, int precioCompra, int precioVenta, int stock, boolean vigente){
        Producto producto = new Producto();
        producto.setIdProducto(id);
        producto.setNombre(nombre);
        producto.setPrecioCompra(precioCompra);
        producto.setPrecioVenta(precioVenta);
        producto.setVigente(vigente);
        producto.setIdCategoria(idCategoria);        
        producto.setStock(stock);
        Operacion crearProducto = new CrearProducto(new ConexionPostgreSql(), producto);
        crearProducto.ejecutar();
    }
}
