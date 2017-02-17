/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.accesodatos.cliente;

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
public class BuscarClientePorRut extends Buscar{

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;    
    private final String rut;

    public BuscarClientePorRut(Conexion conexion, String rut) {
        this.conexion = conexion;
        this.rut = rut;
    }

    @Override
    public JSONObject buscar() {
        JSONObject cliente = new JSONObject();
        try {            
            if (this.conexion.abrirConexion()) {                
                String consulta = "SELECT nombre, apellido, correo FROM cliente WHERE rut = ? ";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setString(1, rut);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    cliente.put("rut", rut);
                    cliente.put("nombre", resultSet.getString("nombre"));
                    cliente.put("apellido", resultSet.getString("apellido"));
                    cliente.put("correo", resultSet.getString("correo"));
                } else {
                    cliente = null;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BuscarClientePorRut.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if (preparedStatement != null && resultSet != null) {
                    preparedStatement.close();
                    resultSet.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BuscarClientePorRut.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }
        return cliente;
    }

    
}
