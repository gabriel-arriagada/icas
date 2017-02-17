/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.venta;

import icas.accesodatos.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author gabo
 */
public class ConsultaSaldoCliente implements IOperacionInt{

    private final String rutCliente;
    
    public ConsultaSaldoCliente(String rutCliente){
        this.rutCliente = rutCliente;
    }
    
    @Override
    public int ejecutar(Conexion conexion) throws SQLException {                
        int saldoCliente = 0;
        String consulta = "SELECT saldo FROM cuenta WHERE rutCliente = ?";
        PreparedStatement preparedStatement = conexion.getConnection().prepareStatement(consulta);
        preparedStatement.setString(1, rutCliente);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            saldoCliente = resultSet.getInt("saldo");            
        }
        preparedStatement.clearParameters();       
        return saldoCliente;
    }

}
