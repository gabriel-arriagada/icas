/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.accesodatos.detalle;

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
public class BuscarDetalle extends Leer{

    private PreparedStatement preparedStatement;
    private ResultSet resultSet; 
    private final String id;

    public BuscarDetalle(Conexion conexion, String id) {
        this.conexion = conexion;
        this.id = id;
    }
    
    @Override
    public JSONArray leer() {
        JSONArray jSONArray = new JSONArray();

        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT idVenta, producto, cantidad, precio, subTotal, imagen FROM"
                        +" detalleView WHERE idVenta = ?";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setInt(1, Integer.parseInt(id));
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    JSONObject detalle = new JSONObject();
                    detalle.put("producto", resultSet.getString("producto"));
                    detalle.put("cantidad", resultSet.getInt("cantidad"));
                    detalle.put("precio", resultSet.getInt("precio"));
                    detalle.put("subTotal", resultSet.getInt("subTotal"));
                    detalle.put("imagen", UrlImagen.URL_VER_IMAGENES + resultSet.getString("imagen"));
                    jSONArray.add(detalle);                 
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BuscarDetalle.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if(resultSet != null){
                    resultSet.close();
                }
                
                if(preparedStatement != null){
                    preparedStatement.close();
                }
                this.conexion.cerrarConexion();
            } catch (SQLException ex) {
                Logger.getLogger(BuscarDetalle.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return jSONArray;
    }  
}
