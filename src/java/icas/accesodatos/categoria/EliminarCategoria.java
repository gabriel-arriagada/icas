/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.accesodatos.categoria;

import icas.dominio.Categoria;
import icas.accesodatos.Conexion;
import icas.accesodatos.Operacion;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gabriel
 */
public class EliminarCategoria extends Operacion{

    private PreparedStatement preparedStatement;    
    private final Categoria categoria;

    public EliminarCategoria(Conexion conexion, Categoria categoria) {
        this.conexion = conexion;
        this.categoria = categoria;
    }
    
    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        preparedStatement = null;
          
        String consulta = "DELETE FROM categoria WHERE idCategoria = ?";
        
        if(this.conexion.abrirConexion())
        {
            try 
            {             
                this.conexion.getConnection().setAutoCommit(false);
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setInt(1, categoria.getIdCategoria());
                int filasAfectadas = preparedStatement.executeUpdate();     
                
                if(filasAfectadas > 0){
                    this.conexion.getConnection().commit();
                    retorno = true;
                }else{
                    this.conexion.getConnection().rollback();                    
                }
                
            }
            catch (SQLException ex) 
            {
                try 
                {
                    this.conexion.getConnection().rollback();
                    Logger.getLogger(EliminarCategoria.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (SQLException ex1) 
                {
                    Logger.getLogger(EliminarCategoria.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new RuntimeException(ex.getMessage());
                }
            }
            finally
            {
                try 
                {
                    this.preparedStatement.close();
                    this.conexion.getConnection().setAutoCommit(true);
                    this.conexion.cerrarConexion();
                } 
                catch (SQLException ex) 
                {
                    Logger.getLogger(EliminarCategoria.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }                
            }
        }
        return retorno;
    }
    
}
