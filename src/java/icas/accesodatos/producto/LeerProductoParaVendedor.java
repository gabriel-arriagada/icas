/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.producto;

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
public class LeerProductoParaVendedor extends Leer {

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public LeerProductoParaVendedor(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public JSONArray leer() {
        JSONArray jSONArray = new JSONArray();

        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT idProducto, nombre, stock "
                        + "FROM producto WHERE "
                        + "idProducto <> 'recarga' AND vigente = true";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    JSONObject producto = new JSONObject();
                    producto.put("idProducto", resultSet.getString("idProducto"));                   
                    producto.put("nombre", resultSet.getString("nombre"));
                    producto.put("stock", resultSet.getInt("stock"));                   
                    jSONArray.add(producto);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerProductoParaVendedor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (this.conexion.getConnection() != null) {
                    this.conexion.cerrarConexion();
                }
            } catch (SQLException ex) {
                Logger.getLogger(LeerProductoParaVendedor.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return jSONArray;
    }

}
