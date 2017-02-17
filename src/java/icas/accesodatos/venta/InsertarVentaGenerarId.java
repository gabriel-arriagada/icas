/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.venta;

import icas.accesodatos.Conexion;
import icas.dominio.Venta;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author gabo
 */
public class InsertarVentaGenerarId implements IOperacionInt{

    private final Venta venta;
    
    public InsertarVentaGenerarId(Venta venta){
        this.venta = venta;
    }
    
    @Override
    public int ejecutar(Conexion conexion) throws SQLException {
        int idVenta = 0;
        String consultaGuardarVenta = "INSERT INTO "
                + "venta(rutVendedor, rutCliente, total, fecha, idFormaPago, pagada) "
                + "VALUES(?,?,?,?,?,?)";

        PreparedStatement preparedStatement = conexion.getConnection().prepareStatement(consultaGuardarVenta, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, venta.getRutVendedor());
        preparedStatement.setString(2, venta.getRutCliente());
        preparedStatement.setInt(3, venta.getTotal());
        preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        preparedStatement.setInt(5, venta.getIdFormaPago());
        preparedStatement.setBoolean(6, venta.isPagada());
        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            idVenta = resultSet.getInt("idVenta");
        }
        preparedStatement.clearParameters();
        return idVenta;        
    }
}
