/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.accesodatos.comprascliente;

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
public class LeerComprasParaCliente extends Leer{

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;  
    private final String rutCliente;
    
    public LeerComprasParaCliente(Conexion conexion, String rutCliente)
    {
        this.conexion = conexion;      
        this.rutCliente = rutCliente;
    }

    @Override
    public JSONArray leer() {
        JSONArray compras = new JSONArray();
      
        try {
            if (this.conexion.abrirConexion()) {                
                String consulta = "SELECT fp.nombre AS formaPago, to_char(v.fecha, 'DD-MM-YYYY') AS fecha, "
                        + "(SELECT SUM(d.subTotal) FROM detalle d WHERE d.idVenta = v.idVenta GROUP BY d.idVenta) AS total "                        
                        + "FROM venta v, formaPago fp "
                        + "WHERE v.rutCliente = ? AND v.idFormaPago = fp.idFormaPago ORDER BY v.fecha DESC";                        
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);                
                preparedStatement.setString(1, this.rutCliente);
                
                resultSet = preparedStatement.executeQuery();
                                                               
                
                while (resultSet.next()) {
                    JSONObject compra = new JSONObject();                                       
                    compra.put("formaPago", resultSet.getString("formaPago"));                    
                    compra.put("fecha", resultSet.getString("fecha"));                                                          
                    compra.put("total", resultSet.getInt("total"));
                    compras.add(compra);                                           
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerComprasParaCliente.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if(resultSet != null){
                    resultSet.close();
                }                
                preparedStatement.close();
                this.conexion.cerrarConexion();
            } catch (SQLException ex) {
                Logger.getLogger(LeerComprasParaCliente.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return compras;
    }
    
}
