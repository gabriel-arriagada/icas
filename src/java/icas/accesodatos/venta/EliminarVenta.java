/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.venta;

import icas.dominio.Venta;
import icas.accesodatos.Conexion;
import icas.accesodatos.Operacion;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gabriel
 */
public class EliminarVenta extends Operacion{

    private CallableStatement callableStatement;
    private final Venta venta;

    public EliminarVenta(Conexion conexion, Venta venta) {
        this.conexion = conexion;
        this.venta = venta;
    }

    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        callableStatement = null;
       
        if (this.conexion.abrirConexion()) 
        {
            try 
            {
                String procedure = "{call anularVenta(?)}";
                this.conexion.getConnection().setAutoCommit(false);
                callableStatement = this.conexion.getConnection().prepareCall(procedure);
                callableStatement.setInt(1, venta.getIdVenta());
                callableStatement.execute();
                this.conexion.getConnection().commit();
                retorno = true;

            } 
            catch (SQLException ex) 
            {
                retorno = false;
                try
                {
                    this.conexion.getConnection().rollback();
                    Logger.getLogger(EliminarVenta.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (SQLException ex1) 
                {
                    Logger.getLogger(EliminarVenta.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new RuntimeException(ex.getMessage());
                }
            }
            finally
            {
                try
                {
                    if(this.callableStatement != null){
                        this.callableStatement.close();
                    }                    
                    this.conexion.getConnection().setAutoCommit(true);
                    this.conexion.cerrarConexion();
                }
                catch (SQLException ex) 
                {
                    Logger.getLogger(EliminarVenta.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        return retorno;
    }
}
