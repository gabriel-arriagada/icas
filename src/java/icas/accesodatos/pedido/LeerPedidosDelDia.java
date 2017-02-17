/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.pedido;

import icas.accesodatos.Conexion;
import icas.accesodatos.Leer;
import icas.dominio.EstadoPedido;
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
public class LeerPedidosDelDia extends Leer {
        
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;    
    
    public LeerPedidosDelDia(Conexion conexion){
        this.conexion = conexion;
    }

    @Override    
    public JSONArray leer() {
        JSONArray pedidos = new JSONArray();
          try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT idPedido, horaRetiro, nombre, rut, estado FROM pedidoView" 
                        + " WHERE idEstado <> ?";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setInt(1, EstadoPedido.ENTREGADO.getIdEstado());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    JSONObject pedido = new JSONObject();
                    pedido.put("idPedido", resultSet.getString("idPedido"));
                    pedido.put("hora", resultSet.getTime("horaRetiro").toString());
                    pedido.put("nombre", resultSet.getString("nombre"));
                    pedido.put("rut", resultSet.getString("rut"));
                    pedido.put("estado", resultSet.getString("estado"));
                    pedidos.add(pedido);                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerPedidosDelDia.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if(resultSet != null){
                    resultSet.close();
                }
                if(preparedStatement !=  null){
                    preparedStatement.close();
                }                
                if(this.conexion.getConnection() != null){
                    this.conexion.cerrarConexion();
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(LeerPedidosDelDia.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return pedidos;
    }

}
