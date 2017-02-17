/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.autoventavendedor;

import icas.accesodatos.Conexion;
import icas.accesodatos.Operacion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Gabriel
 */
public class VerificarAutoVenta extends Operacion {

    private PreparedStatement preparedStatement;    
    private final String rutVendedor;

    public VerificarAutoVenta(Conexion conexion, String rutVendedor) {
        this.conexion = conexion;    
        this.rutVendedor = rutVendedor;
    }

    @Override
    public boolean ejecutar() {
        boolean puedeAutoVenderse = false;
        if (this.conexion.abrirConexion()) {
            try {
                String consulta = "SELECT av.autoVenta FROM autoVenta av WHERE av.rutEmpleado = ? ORDER BY av.fecha DESC LIMIT 1";

                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setString(1, rutVendedor);                
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        puedeAutoVenderse = resultSet.getBoolean("autoVenta");
                    }
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage());
            } finally {
                this.conexion.cerrarConexion();
            }
        }
        return puedeAutoVenderse;
    }

}
