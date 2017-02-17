/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.pedido;

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
public class EntregarPedidoVenta extends Operacion{

    private PreparedStatement pedidoStatement;
    private PreparedStatement ventaStatement;
    private final int idPedido;
    private final int idEstado;
    private final int idVenta;
    
    public EntregarPedidoVenta(Conexion conexion, int idPedido, int idEstado, int idVenta) {
        this.conexion = conexion;        
        this.idPedido = idPedido;
        this.idEstado = idEstado;
        this.idVenta = idVenta;
    }

    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        pedidoStatement = null;
        
        if (this.conexion.abrirConexion()) {
            try {
                String consultaPedido = "UPDATE pedido SET idEstado = ? WHERE idPedido = ?";
                String consulaVenta = "UPDATE venta SET pagada = true WHERE idVenta = ?";
                
                this.conexion.getConnection().setAutoCommit(false);
                
                pedidoStatement = this.conexion.getConnection().prepareStatement(consultaPedido);
                pedidoStatement.setInt(1, idEstado);
                pedidoStatement.setInt(2, idPedido);
                pedidoStatement.executeUpdate();

                ventaStatement = this.conexion.getConnection().prepareStatement(consulaVenta);
                ventaStatement.setInt(1, idVenta);
                ventaStatement.executeUpdate();
                                
                this.conexion.getConnection().commit();
                retorno = true;

            } catch (SQLException ex) {
                try {
                    this.conexion.getConnection().rollback();
                    Logger.getLogger(EntregarPedidoVenta.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(EntregarPedidoVenta.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new RuntimeException(ex.getMessage());
                }
            } finally {
                try {
                    
                    this.conexion.getConnection().setAutoCommit(true);
                    if(pedidoStatement != null){
                        this.pedidoStatement.close();
                    }
                    if(ventaStatement != null){
                        this.ventaStatement.close();
                    }                                                            
                    if(this.conexion.getConnection() != null){
                        this.conexion.cerrarConexion();
                    }
                                        
                } catch (SQLException ex) {
                    Logger.getLogger(EntregarPedidoVenta.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        return retorno;
    }

}
