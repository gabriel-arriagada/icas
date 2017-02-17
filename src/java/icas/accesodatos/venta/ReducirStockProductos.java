/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.venta;

import icas.accesodatos.Conexion;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author gabo
 */
public class ReducirStockProductos implements IOperacionInt{

    private final JSONArray detalleVenta;
    
    public ReducirStockProductos(JSONArray detalleVenta){
        this.detalleVenta = detalleVenta;
    }
    
    @Override
    public int ejecutar(Conexion conexion) throws SQLException {
        int filasAfectadas = 0;
        String consulta = "UPDATE producto SET stock = (stock - ?) WHERE idProducto = ?";
        PreparedStatement preparedStatement;
        for (Object object : detalleVenta) {
            
            JSONObject detalle = (JSONObject) object;
            String idProducto = detalle.get("idProducto").toString();
            int cantidad = Integer.parseInt(detalle.get("cantidad").toString());         
            
            preparedStatement = conexion.getConnection().prepareStatement(consulta);
            preparedStatement.setInt(1, cantidad);
            preparedStatement.setString(2, idProducto);
            int fa = preparedStatement.executeUpdate();
            preparedStatement.clearParameters();
            filasAfectadas = filasAfectadas + fa;
        }
        return filasAfectadas;
    }
}
