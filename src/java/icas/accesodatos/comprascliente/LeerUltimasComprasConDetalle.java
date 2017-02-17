package icas.accesodatos.comprascliente;

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

public class LeerUltimasComprasConDetalle extends Leer {

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private final String rutCliente;

    public LeerUltimasComprasConDetalle(Conexion conexion, String rutCliente) {
        this.conexion = conexion;
        this.rutCliente = rutCliente;
    }

    @Override
    public JSONArray leer() {
        JSONArray ultimasCompras = new JSONArray();
        try {
            if (this.conexion.abrirConexion()) {

                String consultaCompras = "SELECT venta.idVenta, venta.idFormaPago, formaPago.nombre as formaPago, "
                        + "(SELECT SUM(d.subTotal) FROM detalle d WHERE d.idVenta = venta.idVenta GROUP BY d.idVenta) AS total "
                        + "FROM venta, formaPago "
                        + "WHERE venta.rutCliente = ? "
                        + "AND venta.idFormaPago = formaPago.idFormaPago "
                        + "AND (SELECT idProducto FROM detalle d WHERE d.idVenta = venta.idVenta LIMIT 1) <> 'recarga'"
                        + "ORDER BY venta.fecha DESC LIMIT 3";

                String consultaDetalleCompra = "SELECT detalle.idProducto, "
                        + "producto.nombre AS producto, "
                        + "producto.urlImagen AS imagen, "
                        + "detalle.cantidad, detalle.subTotal "
                        + "FROM producto, detalle WHERE detalle.idVenta = ? "
                        + "AND detalle.idProducto = producto.idProducto";

                preparedStatement = this.conexion.getConnection().prepareStatement(consultaCompras);
                preparedStatement.setString(1, this.rutCliente);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    preparedStatement.clearParameters();
                    JSONObject infoCompra = new JSONObject();
                    int idVenta = resultSet.getInt("idVenta");
                    infoCompra.put("idFormaPago", resultSet.getInt("idFormaPago"));
                    infoCompra.put("formaPago", resultSet.getString("formaPago"));
                    infoCompra.put("total", resultSet.getInt("total"));

                    preparedStatement = this.conexion.getConnection().prepareStatement(consultaDetalleCompra);
                    preparedStatement.setInt(1, idVenta);
                    JSONArray detalle;
                    try (ResultSet resultSet_ = preparedStatement.executeQuery()) {
                        detalle = new JSONArray();
                        while (resultSet_.next()) {
                            JSONObject infoDetalle = new JSONObject();                           
                                infoDetalle.put("idProducto", resultSet_.getString("idProducto"));
                                infoDetalle.put("nombre", resultSet_.getString("producto"));
                                infoDetalle.put("imagen", UrlImagen.URL_VER_IMAGENES + resultSet_.getString("imagen"));
                                infoDetalle.put("cantidad", resultSet_.getInt("cantidad"));
                                infoDetalle.put("subTotal", resultSet_.getInt("subTotal"));
                                detalle.add(infoDetalle);                           
                        }
                    }                                        
                    JSONObject compraMasDetalle = new JSONObject();
                    compraMasDetalle.put("infoCompra", infoCompra);
                    compraMasDetalle.put("detalle", detalle);
                    ultimasCompras.add(compraMasDetalle);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeerComprasParaCliente.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                preparedStatement.close();
                this.conexion.cerrarConexion();
            } catch (SQLException ex) {
                Logger.getLogger(LeerComprasParaCliente.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }
        return ultimasCompras;
    }

}
