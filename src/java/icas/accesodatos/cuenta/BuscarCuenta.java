/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.cuenta;

import icas.accesodatos.Buscar;
import icas.accesodatos.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Gabriel
 */
public class BuscarCuenta extends Buscar{
    
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private final String rut;

    public BuscarCuenta(Conexion conexion, String rut) {
        this.conexion = conexion;
        this.rut = rut;
    }

    @Override
    public JSONObject buscar() {
        JSONObject cuenta = new JSONObject();
        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT c.saldo, cli.nombre, cli.apellido FROM cuenta c INNER JOIN "
                        +"cliente cli ON c.rutCliente = cli.rut WHERE rutCliente = ?";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setString(1, rut);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    cuenta.put("saldo", resultSet.getInt("saldo"));
                    cuenta.put("nombre", resultSet.getString("nombre"));
                    cuenta.put("apellido", resultSet.getString("apellido"));
                    cuenta.put("recargaMaxima", (10000 - resultSet.getInt("saldo")));
                } else {
                    cuenta = null;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BuscarCuenta.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if (preparedStatement != null && resultSet != null) {
                    preparedStatement.close();
                    resultSet.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BuscarCuenta.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }
        return cuenta;
    }

}
