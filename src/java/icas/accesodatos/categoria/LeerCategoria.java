/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.categoria;

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
public class LeerCategoria extends Leer {

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;    

    public LeerCategoria(Conexion conexion){        
        this.conexion = conexion;
    }

    @Override
    public JSONArray leer() {
        JSONArray jSONArray = new JSONArray();

        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT idCategoria, categoria FROM categoria";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    JSONObject categoria = new JSONObject();
                    categoria.put("idCategoria", resultSet.getInt("idCategoria"));
                    categoria.put("categoria", resultSet.getString("categoria"));
                    jSONArray.add(categoria);
                    categoria = null;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerCategoria.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                resultSet.close();
                preparedStatement.close();
                this.conexion.cerrarConexion();
            } catch (SQLException ex) {
                Logger.getLogger(LeerCategoria.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

        return jSONArray;
    }

}
