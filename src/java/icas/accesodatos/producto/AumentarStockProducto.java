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
public class AumentarStockProducto extends Operacion{

    private PreparedStatement preparedStatement;
    private final Producto producto;

    public AumentarStockProducto(Conexion conexion, Producto producto) {
        this.conexion = conexion;
        this.producto = producto;
    }

    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        preparedStatement = null;

        String consulta = "UPDATE producto SET stock = ? WHERE idProducto = ?";

        if (this.conexion.abrirConexion()) {
            try {
                this.conexion.getConnection().setAutoCommit(false);
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setInt(1, producto.getStock());
                preparedStatement.setString(2, producto.getIdProducto());
                preparedStatement.executeUpdate();
                this.conexion.getConnection().commit();
                retorno = true;
            } catch (SQLException ex) {
                try {
                    this.conexion.getConnection().rollback();
                    Logger.getLogger(AumentarStockProducto.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(AumentarStockProducto.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new RuntimeException(ex.getMessage());
                }
            } finally {
                try {
                    this.preparedStatement.close();
                    this.conexion.getConnection().setAutoCommit(true);
                    this.conexion.cerrarConexion();
                } catch (SQLException ex) {
                    Logger.getLogger(AumentarStockProducto.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        return retorno;
    }

}
