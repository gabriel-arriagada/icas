/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.venta;

import icas.accesodatos.Conexion;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author gabo
 */
public class ReducirSaldoCliente implements IOperacionInt{

    private final int totalVenta;
    private final String rutCliente;
    
    public ReducirSaldoCliente(int totalVenta, String rutCliente){
        this.rutCliente = rutCliente;
        this.totalVenta = totalVenta;
    }                    

    @Override
    public int ejecutar(Conexion conexion) throws SQLException {
        int filasAfectadas;        
        String consulta = "UPDATE cuenta SET saldo = (saldo - ?) WHERE rutCliente = ?";
        PreparedStatement preparedStatement = conexion.getConnection().prepareStatement(consulta);
        preparedStatement.setInt(1, totalVenta);
        preparedStatement.setString(2, rutCliente);
        filasAfectadas = preparedStatement.executeUpdate();
        preparedStatement.clearParameters();
        return filasAfectadas;
    }

    
}
