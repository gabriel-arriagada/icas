/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.icas.accesodatos.producto;

import icas.accesodatos.Conexion;
import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Operacion;
import icas.accesodatos.producto.CrearProducto;
import icas.accesodatos.producto.EditarProducto;
import icas.accesodatos.producto.EliminarProducto;
import icas.dominio.Producto;
import java.sql.Connection;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author gabo
 */
public class PU5_TestEditarProducto {

    @Before
    public void setUp() {
        Producto producto = new Producto();
        producto.setIdProducto("test_editar");
        producto.setNombre("test_editar");
        producto.setPrecioCompra(0);
        producto.setPrecioVenta(0);
        producto.setVigente(false);
        producto.setIdCategoria(1);
        producto.setUrlImagen(null);
        producto.setStock(100);
        Operacion crearProducto = new CrearProducto(new ConexionPostgreSql(), producto);
        crearProducto.ejecutar();
    }

    @After
    public void tearDown() {
        Producto producto = new Producto();
        producto.setIdProducto("test_editar");
        Operacion eliminarProducto = new EliminarProducto(new ConexionPostgreSql(), producto);
        eliminarProducto.ejecutar();
    }

    @Test
    public void pruebaA() {
        Producto producto = new Producto();
        producto.setIdProducto("test_editar");
        producto.setNombre("test_editado");
        producto.setPrecioCompra(0);
        producto.setPrecioVenta(0);
        producto.setVigente(true);
        producto.setIdCategoria(2);
        producto.setUrlImagen("url_imagen");
        producto.setStock(500);
        Operacion editarProducto = new EditarProducto(new ConexionPostgreSql(), producto);
        assertTrue(editarProducto.ejecutar());
    }

    @Test
    public void pruebaB() {
        Producto producto = new Producto();
        producto.setIdProducto("test_editar");
        producto.setNombre("Nombre de producto que excede el largo máximo permitido");
        producto.setPrecioCompra(0);
        producto.setPrecioVenta(0);
        producto.setVigente(true);
        producto.setIdCategoria(2);
        producto.setUrlImagen("url_imagen");
        producto.setStock(500);
        Operacion editarProducto = new EditarProducto(new ConexionPostgreSql(), producto);
        assertEquals(false, editarProducto.ejecutar());
    }

    @Test
    public void pruebaC() {
        Conexion invalida = new Conexion() {
            @Override
            public boolean abrirConexion() {
                return false;
            }

            @Override
            public boolean cerrarConexion() {
                return false;
            }

            @Override
            public Connection getConnection() {
                return this.connection;
            }
        };

        Operacion editarProducto = new EditarProducto(invalida, null);
        assertFalse(editarProducto.ejecutar());
    }
}
