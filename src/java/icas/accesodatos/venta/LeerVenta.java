/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.accesodatos.venta;

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
public class LeerVenta extends Leer{

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;    

    public LeerVenta(Conexion conexion) {
        this.conexion = conexion;
    }
    
    @Override
    public JSONArray leer() {
        JSONArray jSONArray = new JSONArray();

        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT idVenta, vendedor, cliente, total, fecha, formaPago, pagada FROM ventaView";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    JSONObject venta = new JSONObject();
                    venta.put("idVenta", resultSet.getInt("idVenta"));
                    venta.put("vendedor", resultSet.getString("vendedor"));
                    venta.put("cliente", resultSet.getString("cliente"));
                    venta.put("total", resultSet.getInt("total"));
                    venta.put("fecha", resultSet.getString("fecha"));
                    venta.put("formaPago", resultSet.getString("formaPago"));
                    venta.put("pagada", resultSet.getBoolean("pagada"));
                    jSONArray.add(venta);                 
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerVenta.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                resultSet.close();
                preparedStatement.close();
                this.conexion.cerrarConexion();
            } catch (SQLException ex) {
                Logger.getLogger(LeerVenta.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return jSONArray;
    }
    
}
