/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.accesodatos.empleado;

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
public class LeerEmpleado extends Leer{
    
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    private final int idRol;
    
    public LeerEmpleado(Conexion conexion, int idRol){
        this.conexion = conexion;
        this.idRol = idRol;
    }
    
    @Override
    public JSONArray leer() {
         JSONArray jSONArray = new JSONArray();

        try {
            if (this.conexion.abrirConexion()) {
                String consulta = 
                        "SELECT e.rut, r.nombre AS rol, e.nombre, e.apellido, e.correo, "
                        + "(SELECT ve.vigente FROM vigenciaEmpleado ve WHERE ve.rutEmpleado = e.rut ORDER BY ve.fecha DESC LIMIT 1) AS vigente, "
                        + "(SELECT av.autoVenta FROM autoVenta av WHERE av.rutEmpleado = e.rut ORDER BY av.fecha DESC LIMIT 1) AS autoVenta "
                        + "FROM empleado e, rol r "
                        + "WHERE e.idRol = r.idRol AND e.idRol <> 1 AND e.rut <> '1-9' AND e.idRol = ?";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setInt(1, this.idRol);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    JSONObject usuario = new JSONObject();
                    usuario.put("rut", resultSet.getString("rut"));
                    usuario.put("rol", resultSet.getString("rol"));
                    usuario.put("nombre", resultSet.getString("nombre"));
                    usuario.put("apellido", resultSet.getString("apellido"));
                    usuario.put("correo", resultSet.getString("correo"));
                    usuario.put("vigente", resultSet.getBoolean("vigente"));                            
                    usuario.put("autoVenta", resultSet.getBoolean("autoVenta"));
                    jSONArray.add(usuario);                 
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if(resultSet != null){
                    resultSet.close();
                }                
                preparedStatement.close();
                this.conexion.cerrarConexion();
            } catch (SQLException ex) {
                Logger.getLogger(LeerEmpleado.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return jSONArray;
    }
    
}
