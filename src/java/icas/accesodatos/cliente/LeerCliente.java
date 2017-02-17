/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.accesodatos.cliente;

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
public class LeerCliente extends Leer{
    
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;    
    
    public LeerCliente(Conexion conexion){
        this.conexion = conexion;        
    }
    
    @Override
    public JSONArray leer() {
         JSONArray jSONArray = new JSONArray();

        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT c.rut, c.nombre, c.apellido, c.correo, "
                        + "(SELECT vc.vigente FROM vigenciaCliente vc WHERE vc.rutCliente = c.rut ORDER BY vc.fecha DESC LIMIT 1) AS vigente "
                        + "FROM cliente c";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);                
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    JSONObject usuario = new JSONObject();
                    usuario.put("rut", resultSet.getString("rut"));                    
                    usuario.put("nombre", resultSet.getString("nombre"));
                    usuario.put("apellido", resultSet.getString("apellido"));
                    usuario.put("correo", resultSet.getString("correo"));
                    usuario.put("vigente", resultSet.getBoolean("vigente"));                            
                    jSONArray.add(usuario);                 
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerCliente.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if(resultSet != null){
                    resultSet.close();
                }                
                preparedStatement.close();
                this.conexion.cerrarConexion();
            } catch (SQLException ex) {
                Logger.getLogger(LeerCliente.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return jSONArray;
    }
    
}
