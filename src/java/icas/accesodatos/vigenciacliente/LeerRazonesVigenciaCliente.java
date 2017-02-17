/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.vigenciacliente;

import icas.accesodatos.Conexion;
import icas.accesodatos.Leer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Gabriel
 */
public class LeerRazonesVigenciaCliente extends Leer {

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;    
    private final boolean razonesPositivas;
    
    
    public LeerRazonesVigenciaCliente(Conexion conexion, boolean razonesPositivas){        
        this.conexion = conexion;
        this.razonesPositivas = razonesPositivas;
    }

    @Override
    public JSONArray leer() {
        JSONArray jSONArray = new JSONArray();

        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT idRazon, razon FROM razonVigenciaCliente "
                        + "WHERE positiva = ? OR positiva IS NULL ORDER BY idRazon";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setBoolean(1, razonesPositivas);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    JSONObject razon = new JSONObject();
                    razon.put("idRazon", resultSet.getInt("idRazon"));
                    razon.put("razon", resultSet.getString("razon"));
                    jSONArray.add(razon);                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerRazonesVigenciaCliente.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                resultSet.close();
                preparedStatement.close();
                this.conexion.cerrarConexion();
            } catch (SQLException ex) {
                Logger.getLogger(LeerRazonesVigenciaCliente.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return jSONArray;
    }

}
