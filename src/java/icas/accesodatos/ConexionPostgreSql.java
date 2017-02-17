/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.accesodatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gabriel
 */
public class ConexionPostgreSql extends Conexion{

    private Properties properties;
    private String url;
    
    public ConexionPostgreSql()
    {
        try {
            properties = new Properties();
            url = "jdbc:postgresql://localhost/icas_db";
            properties.put("user", "icas");
            properties.put("password", "jazzbass");                        
            Class.forName("org.postgresql.Driver");            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionPostgreSql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean abrirConexion() {
        boolean retorno = false;
        try {
            if(this.connection == null)
            {
                this.connection = DriverManager.getConnection(url, properties);
                retorno = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConexionPostgreSql.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
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
    
}
