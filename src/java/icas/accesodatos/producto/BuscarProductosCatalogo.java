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
 * @author gabo
 */
public class BuscarProductosCatalogo extends Leer {

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private final String busqueda;

    public BuscarProductosCatalogo(Conexion conexion, String busqueda) {
        this.conexion = conexion;
        this.busqueda = busqueda;
    }

    @Override
    public JSONArray leer() {
        JSONArray jSONArray = new JSONArray();

        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT p.idProducto, p.nombre, p.stock, "
                        + "p.precioVenta, p.urlImagen FROM producto p, categoria c "
                        + "WHERE p.vigente = true AND p.stock > 0 "
                        + "AND p.idCategoria = c.idCategoria "
                        + "AND lower(p.nombre) LIKE lower(?)";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setString(1, "%" + busqueda + "%");                
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    JSONObject producto = new JSONObject();
                    producto.put("idProducto", resultSet.getString("idProducto"));
                    producto.put("nombre", resultSet.getString("nombre"));                    
                    producto.put("stock", resultSet.getInt("stock"));
                    producto.put("precio", resultSet.getInt("precioVenta"));
                    producto.put("imagen", UrlImagen.URL_VER_IMAGENES + resultSet.getString("urlImagen"));
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
