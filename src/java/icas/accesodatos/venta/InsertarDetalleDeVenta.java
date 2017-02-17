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
public class InsertarDetalleDeVenta implements IOperacionInt{

    private final JSONArray detalleVenta;
    private final int idVenta;
    
    public InsertarDetalleDeVenta(JSONArray detalleVenta, int idVenta){
        this.detalleVenta = detalleVenta;
        this.idVenta = idVenta;
    }
    
    @Override
    public int ejecutar(Conexion conexion) throws SQLException {
        int filasAfectadas = 0;
        String consulta = "INSERT INTO detalle(idVenta, idProducto, cantidad, precio, subTotal) "
                + "VALUES(?,?,?,?,?)";
        PreparedStatement preparedStatement;
        for (Object object : detalleVenta) {
            JSONObject detalle = (JSONObject) object;
            String idProducto = detalle.get("idProducto").toString();
            int cantidad = Integer.parseInt(detalle.get("cantidad").toString());
            int precio = Integer.parseInt(detalle.get("precio").toString());

            preparedStatement = conexion.getConnection().prepareStatement(consulta);
            preparedStatement.setInt(1, idVenta);
            preparedStatement.setString(2, idProducto);
            preparedStatement.setInt(3, cantidad);
            preparedStatement.setInt(4, precio);
            preparedStatement.setInt(5, precio * cantidad);
            int fa = preparedStatement.executeUpdate();
            filasAfectadas = filasAfectadas + fa; 
            preparedStatement.clearParameters();
        }
        return filasAfectadas;
    }
}
