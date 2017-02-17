/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.cliente;

import icas.accesodatos.Conexion;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author gabo
 */
public class CrearCuenta {

    public boolean crearCuenta(Conexion conexion, PreparedStatement preparedStatement, String rut) throws SQLException {
        boolean retorno = false;
        String consultaCuenta = "INSERT INTO cuenta(rutCliente, saldo) VALUES(?,?)";
        preparedStatement = conexion.getConnection().prepareStatement(consultaCuenta);
        preparedStatement.setString(1, rut);
        preparedStatement.setInt(2, 0);
        if (preparedStatement.executeUpdate() > 0) {
            retorno = true;
        }
        return retorno;
    }
}
