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
public class CrearProducto extends Operacion {

    private PreparedStatement preparedStatement;
    private final Producto producto;

    public CrearProducto(Conexion conexion, Producto producto) {
        this.producto = producto;
        this.conexion = conexion;
    }

    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        preparedStatement = null;

        if (this.conexion.abrirConexion()) {
            try {

                String consulta = "INSERT INTO producto"
                        + " (idProducto, idCategoria, nombre, stock, precioCompra, precioVenta, vigente) "
                        + " VALUES(?, ?, ?, ?, ?, ?, ?)";

                this.conexion.getConnection().setAutoCommit(false);
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setString(1, producto.getIdProducto());
                preparedStatement.setInt(2, producto.getIdCategoria());
                preparedStatement.setString(3, producto.getNombre());
                preparedStatement.setInt(4, producto.getStock());
                preparedStatement.setInt(5, producto.getPrecioCompra());
                preparedStatement.setInt(6, producto.getPrecioVenta());
                preparedStatement.setBoolean(7, producto.isVigente());
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
                    Logger.getLogger(CrearProducto.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(CrearProducto.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new RuntimeException(ex.getMessage());
                }
            } finally {
                try {
                    if(this.preparedStatement != null){
                        this.preparedStatement.close();
                    }                    
                    this.conexion.getConnection().setAutoCommit(true);
                    this.conexion.cerrarConexion();
                } catch (SQLException ex) {
                    Logger.getLogger(CrearProducto.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        return retorno;
    }

}
