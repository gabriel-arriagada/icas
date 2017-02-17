/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.cliente;

import icas.accesodatos.Conexion;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author gabo
 */
public class AgregarVigenciaCliente {
    public boolean agregarVigencia(Conexion conexion, PreparedStatement preparedStatement, String rut) throws SQLException{
        boolean retorno = false;        
        String consulta = "INSERT INTO vigenciaCliente(rutCliente, fecha, idRazon, vigente)"
                + " VALUES(?,?,?,?)";
        preparedStatement = conexion.getConnection().prepareStatement(consulta);
        preparedStatement.setString(1, rut);
        preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        preparedStatement.setInt(3, 5);/*Razón 5 de ingreso al sistema*/
        preparedStatement.setBoolean(4, true);
        if(preparedStatement.executeUpdate() > 0){
            retorno = true;
        }
        return retorno;
    }
}
