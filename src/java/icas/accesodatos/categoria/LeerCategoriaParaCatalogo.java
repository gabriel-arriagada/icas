/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.categoria;

import icas.accesodatos.Conexion;
import icas.accesodatos.LeerArray;
import icas.dominio.Categoria;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabo
 */
public class LeerCategoriaParaCatalogo extends LeerArray {

    private final ArrayList<Categoria> categorias;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Categoria categoria;

    public LeerCategoriaParaCatalogo(Conexion generica) {
        this.conexion = generica;
        categorias = new ArrayList<>();
    }

    @Override
    public ArrayList leer() {
        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT idCategoria, categoria FROM categoria WHERE idCategoria > 1";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {                    
                    categoria = new Categoria();
                    categoria.setIdCategoria(resultSet.getInt("idCategoria"));
                    categoria.setCategoria(resultSet.getString("categoria"));
                    categorias.add(categoria);                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerCategoriaParaCatalogo.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if(resultSet != null){
                    resultSet.close();
                }                
                preparedStatement.close();
                this.conexion.cerrarConexion();
            } catch (SQLException ex) {
                Logger.getLogger(LeerCategoriaParaCatalogo.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }
        return categorias;
    }

}
