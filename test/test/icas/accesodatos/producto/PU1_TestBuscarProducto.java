/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.icas.accesodatos.producto;

import icas.accesodatos.Buscar;
import icas.accesodatos.Conexion;
import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.producto.BuscarProducto;
import icas.accesodatos.producto.CrearProducto;
import icas.accesodatos.producto.EliminarProducto;
import icas.dominio.Producto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author gabo
 */
public class PU1_TestBuscarProducto {

    @Before
    public void setUp() {
        Producto producto = new Producto();
        producto.setIdProducto("pruebaA");
        producto.setNombre("buscar_test");
        producto.setPrecioCompra(0);
        producto.setPrecioVenta(0);
        producto.setVigente(true);
        producto.setIdCategoria(1);
        producto.setUrlImagen(null);
        producto.setStock(100);
        Operacion crearProducto = new CrearProducto(new ConexionPostgreSql(), producto);
        crearProducto.ejecutar();
    }

    @After
    public void tearDown() {
        Producto producto = new Producto();
        producto.setIdProducto("pruebaA");
        Operacion eliminarProducto = new EliminarProducto(new ConexionPostgreSql(), producto);
        eliminarProducto.ejecutar();
    }

    @Test
    public void pruebaA() {
        Buscar buscarProducto = new BuscarProducto(new ConexionPostgreSql(), "pruebaA");
        JSONObject productoBuscado = buscarProducto.buscar();
        JSONObject productoEsperado = new JSONObject();
        productoEsperado.put("idProducto", "pruebaA");
        productoEsperado.put("nombre", "buscar_test");
        productoEsperado.put("precio", 0);
        productoEsperado.put("stock", 100);
        assertEquals(productoEsperado, productoBuscado);
    }
    
    @Test
    public void pruebaB(){
        Buscar buscarProducto = new BuscarProducto(new ConexionPostgreSql(), "no_registrado");
        JSONObject productoBuscado = buscarProducto.buscar();        
        assertTrue(productoBuscado == null);
    }
    
    @Test
    public void pruebaC(){
        Conexion falsasCredenciales = new Conexion() {
            private Properties properties;
            private String url;

            @Override
            public boolean abrirConexion() {
                boolean retorno = false;
                try {
                    properties = new Properties();
                    url = "jdbc:postgresql://localhost/icas";
                    properties.put("user", "error");
                    properties.put("password", "error");
                    Class.forName("org.postgresql.Driver");
                    if (this.connection == null) {
                        this.connection = DriverManager.getConnection(url, properties);
                        retorno = true;
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(PU3_TestCrearProducto.class.getName()).log(Level.SEVERE, null, ex);
                }
                return retorno;
            }

            @Override
            public boolean cerrarConexion() {
                boolean retorno = false;
                try {
                    this.connection.close();
                    retorno = true;
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionPostgreSql.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
                return retorno;
            }

            @Override
            public Connection getConnection() {
                return this.connection;
            }
        };
        Buscar buscarProducto = new BuscarProducto(falsasCredenciales, "no_registrado");
        JSONObject productoBuscado = buscarProducto.buscar();             
        assertTrue(productoBuscado == null);
    }
}
