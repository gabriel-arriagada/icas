/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.accesodatos.producto;

import icas.dominio.Producto;
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
public class EditarProducto extends Operacion{

    private PreparedStatement preparedStatement;    
    private final Producto producto;
    
    public EditarProducto(Conexion conexion, Producto producto){
        this.conexion = conexion;
        this.producto = producto;
    }
    
    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        preparedStatement = null;
          
        String consulta = "UPDATE producto SET idCategoria = ?, nombre = ?, stock = ?, "
                + "precioCompra = ?, precioVenta = ? WHERE idProducto = ?";
        
        if(this.conexion.abrirConexion())
        {
            try 
            {             
                this.conexion.getConnection().setAutoCommit(false);
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setInt(1, producto.getIdCategoria());
                preparedStatement.setString(2, producto.getNombre());
                preparedStatement.setInt(3, producto.getStock());
                preparedStatement.setInt(4, producto.getPrecioCompra());
                preparedStatement.setInt(5, producto.getPrecioVenta());
                preparedStatement.setString(6, producto.getIdProducto());
                int filasAfectadas = preparedStatement.executeUpdate();   
                
                if(filasAfectadas > 0){
                    retorno = true;
                    this.conexion.getConnection().commit();                
                }else{
                    this.conexion.getConnection().rollback();
                }
                
            }
            catch (SQLException ex) 
            {
                try 
                {
                    this.conexion.getConnection().rollback();
                    Logger.getLogger(EditarProducto.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (SQLException ex1) 
                {
                    Logger.getLogger(EditarProducto.class.getName()).log(Level.SEVERE, null, ex1);
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
                    Logger.getLogger(EditarProducto.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }                
            }
        }
        return retorno;
    }
    
}
