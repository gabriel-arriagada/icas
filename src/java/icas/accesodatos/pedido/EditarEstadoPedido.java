/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.pedido;

import icas.dominio.Pedido;
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
public class EditarEstadoPedido extends Operacion{

    private PreparedStatement preparedStatement;    
    private final Pedido pedido;
    
    public EditarEstadoPedido(Conexion conexion, Pedido pedido) {
        this.conexion = conexion;
        this.pedido = pedido;
    }

    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        preparedStatement = null;
        
        if (this.conexion.abrirConexion()) {
            try {
                String consulta = "UPDATE pedido SET idEstado = ? WHERE idPedido = ?";
                this.conexion.getConnection().setAutoCommit(false);
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setInt(1, pedido.getIdEstado());
                preparedStatement.setInt(2, pedido.getIdPedido());
                int filasAfectadas = preparedStatement.executeUpdate();

                if (filasAfectadas > 0) {
                    retorno = true;
                    this.conexion.getConnection().commit();
                } else {
                    this.conexion.getConnection().rollback();
                }

            } catch (SQLException ex) {
                try {
                    this.conexion.getConnection().rollback();
                    Logger.getLogger(EditarEstadoPedido.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(EditarEstadoPedido.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new RuntimeException(ex.getMessage());
                }
            } finally {
                try {
                    this.preparedStatement.close();
                    this.conexion.getConnection().setAutoCommit(true);
                    this.conexion.cerrarConexion();
                } catch (SQLException ex) {
                    Logger.getLogger(EditarEstadoPedido.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        return retorno;
    }

}
