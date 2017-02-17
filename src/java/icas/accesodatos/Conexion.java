/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.accesodatos;

import java.sql.Connection;

/**
 *
 * @author Gabriel
 */
public abstract class Conexion {    
   protected Connection connection;
   public abstract boolean abrirConexion();
   public abstract boolean cerrarConexion();
   public abstract Connection getConnection();
}
