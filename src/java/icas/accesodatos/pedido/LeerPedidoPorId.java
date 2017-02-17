/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.pedido;

import icas.accesodatos.Conexion;
import icas.accesodatos.Leer;
import icas.dominio.UrlImagen;
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
public class LeerPedidoPorId extends Leer{

    private PreparedStatement preparedStatement;
    private PreparedStatement statementComentario;
    private ResultSet resultSet;    
    private final String id;

    public LeerPedidoPorId(Conexion conexion, String id) {
        this.conexion = conexion;
        this.id = id;
    }

    @Override
    public JSONArray leer() {       
        JSONArray pedido = new JSONArray();
        JSONObject comentario = new JSONObject();
        JSONArray detalleDelPedido = new JSONArray();
        try {
            if (this.conexion.abrirConexion()) {
                
                String consultaComentario  = "SELECT comentario FROM pedido WHERE idPedido = ?";                
                statementComentario = this.conexion.getConnection().prepareStatement(consultaComentario);
                statementComentario.setInt(1, Integer.parseInt(id));
                try (ResultSet _resultSet = statementComentario.executeQuery()) {
                    if(_resultSet.next()){
                        comentario.put("comentario", _resultSet.getString("comentario"));
                    }
                }
                
                String consulta = "SELECT producto, cantidad, precio, subTotal, imagen FROM detalleView"
                        + " WHERE idVenta = (SELECT idVenta FROM pedido WHERE idPedido = ?)";
                
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setInt(1, Integer.parseInt(id));
                resultSet = preparedStatement.executeQuery();
                
                while (resultSet.next()) {
                    JSONObject item = new JSONObject();
                    item.put("producto", resultSet.getString("producto"));
                    item.put("cantidad", resultSet.getInt("cantidad"));
                    item.put("precio", resultSet.getInt("precio"));
                    item.put("subTotal", resultSet.getInt("subTotal"));
                    item.put("imagen", UrlImagen.URL_VER_IMAGENES + resultSet.getString("imagen"));
                    detalleDelPedido.add(item);
                }
                
                pedido.add(comentario);
                pedido.add(detalleDelPedido);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerPedidoPorId.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if(statementComentario != null){
                    statementComentario.close();
                }
                
                if (this.conexion.getConnection() != null) {
                    this.conexion.cerrarConexion();
                }

            } catch (SQLException ex) {
                Logger.getLogger(LeerPedidoPorId.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return pedido;
    }

}
