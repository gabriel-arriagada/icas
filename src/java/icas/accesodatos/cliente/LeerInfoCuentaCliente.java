/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.cliente;

import icas.accesodatos.Conexion;
import icas.accesodatos.ConexionPostgreSql;
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
public class LeerInfoCuentaCliente extends Leer{

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;    
    private final String rut;
    
    public LeerInfoCuentaCliente(Conexion conexion, String rut) {
        this.conexion = new ConexionPostgreSql();
        this.rut = rut;
    }

    @Override
    public JSONArray leer() {
        JSONArray informacion = new JSONArray();
        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT saldo FROM cuenta WHERE rutCliente = ?";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setString(1, rut);
                resultSet = preparedStatement.executeQuery();
                
                if (resultSet.next()) {
                    JSONObject saldo = new JSONObject();
                    saldo.put("saldo", resultSet.getString("saldo"));
                    informacion.add(saldo);
                }
                
                String consultaRecarga = "SELECT to_char(v.fecha, 'DD-MM-YYYY'::text) AS fecha, "
                        + "to_char(v.fecha, 'hh24:MI'::text) AS hora, v.total as monto "
                        + "FROM venta v, detalle d "
                        + "WHERE v.idVenta = d.idVenta AND d.idProducto = 'recarga' "
                        + "AND v.rutCliente = ? ORDER BY v.fecha DESC LIMIT 1;";
                preparedStatement.clearParameters();
                resultSet = null;
                preparedStatement = this.conexion.getConnection().prepareStatement(consultaRecarga);
                preparedStatement.setString(1, rut);
                resultSet = preparedStatement.executeQuery();                
                if (resultSet.next()) {
                    JSONObject recarga = new JSONObject();
                    recarga.put("fecha", resultSet.getString("fecha"));                    
                    recarga.put("hora", resultSet.getString("hora"));
                    recarga.put("monto", resultSet.getString("monto"));
                    informacion.add(recarga);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerInfoCuentaCliente.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {                
            try {
                this.conexion.cerrarConexion();
                if (preparedStatement != null && resultSet != null) {
                    preparedStatement.close();
                    resultSet.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(LeerInfoCuentaCliente.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }
        return informacion;
    }

}
