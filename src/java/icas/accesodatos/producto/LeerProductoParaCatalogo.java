package icas.accesodatos.producto;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import icas.accesodatos.Conexion;
import icas.accesodatos.ConexionPostgreSql;
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
public class LeerProductoParaCatalogo extends Leer{

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;      
    private final int idCategoria;
    
    public LeerProductoParaCatalogo(Conexion conexion, int idCategoria)
    {
        this.conexion = new ConexionPostgreSql();        
        this.idCategoria = idCategoria;
    }

    @Override
    public JSONArray leer() {
        JSONArray productos = new JSONArray();        
        String parametro = "";        
        if(idCategoria > 0){            
            parametro = "AND idCategoria = ?";
        }
        
        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT idProducto, nombre, stock,"+
                        " precioVenta, urlImagen FROM producto WHERE vigente = true AND stock > 0 "
                        + parametro + " ORDER BY idCategoria";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                if(idCategoria > 0){
                    preparedStatement.setInt(1, idCategoria);
                }
                resultSet = preparedStatement.executeQuery();
                                                                               
                while (resultSet.next()) {
                    JSONObject producto = new JSONObject();
                    producto.put("idProducto", resultSet.getString("idProducto"));                    
                    producto.put("nombre", resultSet.getString("nombre"));
                    producto.put("stock", resultSet.getInt("stock"));                    
                    producto.put("precio", resultSet.getInt("precioVenta"));                    
                    producto.put("imagen", UrlImagen.URL_VER_IMAGENES + resultSet.getString("urlImagen"));                     
                    productos.add(producto);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerProducto.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if(resultSet != null){
                    resultSet.close();
                }                
                preparedStatement.close();
                this.conexion.cerrarConexion();
            } catch (SQLException ex) {
                Logger.getLogger(LeerProducto.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return productos;
    }
    
}
