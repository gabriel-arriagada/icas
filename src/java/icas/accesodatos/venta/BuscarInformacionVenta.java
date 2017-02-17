/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.venta;

import icas.accesodatos.Buscar;
import icas.accesodatos.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Gabriel
 */
public class BuscarInformacionVenta extends Buscar {

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private final int idPedido;

    public BuscarInformacionVenta(Conexion conexion, int idPedido) {
        this.conexion = conexion;
        this.idPedido = idPedido;
    }

    @Override
    public JSONObject buscar() {
        JSONObject informacionVenta = new JSONObject();
        try {
            if (this.conexion.abrirConexion()) {
                String consulta = "SELECT p.idVenta, v.pagada, v.total, fp.nombre AS formaPago "
                        + "FROM pedido p, venta v, formaPago fp WHERE p.idPedido = ? "
                        + "AND p.idVenta = v.idVenta AND v.idFormaPago = fp.idFormaPago";
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setInt(1, idPedido);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    informacionVenta.put("idVenta", resultSet.getInt("idVenta"));
                    informacionVenta.put("total", resultSet.getInt("total"));
                    informacionVenta.put("pagada", resultSet.getBoolean("pagada"));
                    informacionVenta.put("formaPago", resultSet.getString("formaPago"));
                } else {
                    informacionVenta = null;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BuscarInformacionVenta.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if (preparedStatement != null && resultSet != null) {
                    preparedStatement.close();
                    resultSet.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BuscarInformacionVenta.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }
        return informacionVenta;
    }

}
