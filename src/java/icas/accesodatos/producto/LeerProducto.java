/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.producto;

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
public class LeerProducto extends Leer {

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public LeerProducto(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public JSONArray leer() {
        JSONArray jSONArray = new JSONArray();

        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT idProducto, categoria, nombre, stock,"
                        + " precioCompra, precioVenta, vigente, urlImagen FROM productoView WHERE "
                        + " idProducto <> 'recarga'";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    JSONObject producto = new JSONObject();
                    producto.put("idProducto", resultSet.getString("idProducto"));
                    producto.put("categoria", resultSet.getString("categoria"));
                    producto.put("nombre", resultSet.getString("nombre"));
                    producto.put("stock", resultSet.getInt("stock"));
                    producto.put("precioCompra", resultSet.getInt("precioCompra"));
                    producto.put("precioVenta", resultSet.getInt("precioVenta"));
                    producto.put("vigente", resultSet.getBoolean("vigente"));
                    producto.put("urlImagen", UrlImagen.URL_VER_IMAGENES + resultSet.getString("urlImagen"));
                    jSONArray.add(producto);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerProducto.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(LeerProducto.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return jSONArray;
    }

}
