/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.pedido;

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
public class LeerPedidosPendientesCliente extends Leer {

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;    
    private final String rut;
    
    public LeerPedidosPendientesCliente(Conexion conexion, String rut){
        this.conexion = conexion;
        this.rut = rut;
    }

    @Override    
    public JSONArray leer() {
        JSONArray pedidos = new JSONArray();
          try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT p.idPedido, p.horaRetiro, e.estado, v.pagada, v.total "
                        + "FROM pedido p, estado e, venta v WHERE v.rutCliente = ? "
                        + "AND to_char(v.fecha, 'YYYY-MM-DD') = to_char(now(), 'YYYY-MM-DD') "
                        + "AND p.idEstado <> 4 "
                        + "AND p.idVenta = v.idVenta "
                        + "AND p.idEstado = e.idEstado "
                        + "ORDER BY p.horaRetiro";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setString(1, rut);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    JSONObject pedido = new JSONObject();
                    pedido.put("idPedido", resultSet.getInt("idPedido"));
                    pedido.put("horaRetiro", resultSet.getTime("horaRetiro").toString());
                    pedido.put("estado", resultSet.getString("estado"));
                    pedido.put("pagada", resultSet.getBoolean("pagada"));                    
                    pedido.put("total", resultSet.getInt("total"));
                    pedidos.add(pedido);                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerPedidosPendientesCliente.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(LeerPedidosPendientesCliente.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return pedidos;
    }

}
