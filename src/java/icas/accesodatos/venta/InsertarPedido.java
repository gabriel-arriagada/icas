/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.venta;

import icas.accesodatos.Conexion;
import icas.dominio.EstadoPedido;
import icas.dominio.Hora;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabo
 */
public class InsertarPedido implements IOperacionInt{

    private final Hora hora;
    private final int idVenta;
    private final String horaString;
    private final String comentario;
    
    public InsertarPedido(int idVenta, String hora, String comentario) {
        this.hora = new Hora();
        this.idVenta = idVenta;
        this.horaString = hora;
        this.comentario = comentario;
    }
    
    @Override
    public int ejecutar(Conexion conexion) throws SQLException {
        int idPedido = 0;
        try {            
            String consulta = "INSERT INTO pedido (idVenta, horaRetiro, idEstado, comentario) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conexion.getConnection().prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, idVenta);
            preparedStatement.setTime(2, this.hora.getHoraSql(horaString));
            preparedStatement.setInt(3, EstadoPedido.RECIBIDO.getIdEstado());
            preparedStatement.setString(4, comentario);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                idPedido = resultSet.getInt("idPedido");
            }            
        } catch (ParseException ex) {
            Logger.getLogger(InsertarPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idPedido;
    }
}
