/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.producto;

import icas.accesodatos.Buscar;
import icas.accesodatos.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Gabriel
 */
public class BuscarProducto extends Buscar{

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;    
    private final String idProducto;

    public BuscarProducto(Conexion conexion, String idProducto) {
        this.conexion = conexion;
        this.idProducto = idProducto;
    }

    @Override
    public JSONObject buscar() {
        JSONObject producto = new JSONObject();
        try {            
            if (this.conexion.abrirConexion()) {                
                String consulta = "SELECT nombre, precioVenta, stock FROM producto WHERE IdProducto = ? AND vigente = true";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setString(1, idProducto);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    producto.put("idProducto", idProducto);
                    producto.put("nombre", resultSet.getString("nombre"));
                    producto.put("precio", resultSet.getInt("precioVenta"));
                    producto.put("stock", resultSet.getInt("stock"));
                } else {
                    producto = null;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BuscarProducto.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if (preparedStatement != null && resultSet != null) {
                    preparedStatement.close();
                    resultSet.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BuscarProducto.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }
        return producto;
    }

}
