/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.icas.accesodatos.producto;

import icas.accesodatos.Conexion;
import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Leer;
import icas.accesodatos.producto.LeerProducto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author gabo
 */
public class PU8_TestLeerProductos {

    @Test
    public void pruebaA() {
        Leer leerProductos = new LeerProducto(new ConexionPostgreSql());
        assertTrue(leerProductos.leer() != null);
    }

    @Test
    public void pruebaB() {
        Conexion falsasCredenciales;
        falsasCredenciales = new Conexion() {
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
                    retorno = false;                    
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
                    Logger.getLogger(PU8_TestLeerProductos.class.getName()).log(Level.SEVERE, null, ex);
                }                
                
                return retorno;
            }

            @Override
            public Connection getConnection() {
                return this.connection;
            }
        };

        Leer leerProductos = new LeerProducto(falsasCredenciales);        
        assertTrue(leerProductos.leer().size() == 0);
    }

}
