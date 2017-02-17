package icas.accesodatos.venta;

import icas.accesodatos.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConsultaStockPrecio implements IOperacionJSON{

    private final JSONArray retorno;
    private final JSONArray detalleVenta;
    private final JSONArray productosConStockInsuficiente;

    public ConsultaStockPrecio(JSONArray detalleVenta) {
        this.detalleVenta = detalleVenta;
        this.retorno = new JSONArray();
        this.productosConStockInsuficiente = new JSONArray();
    }
    
    @Override
    public JSONArray ejecutar(Conexion conexion) throws SQLException {
        boolean stockSuficiente = true;
        int totalVenta = 0;
        String consulta = "SELECT stock, precioVenta FROM producto WHERE idProducto = ?";
        PreparedStatement preparedStatement = conexion.getConnection().prepareStatement(consulta);

        for (Object object : this.detalleVenta) {

            JSONObject detalle = (JSONObject) object;
            String idProducto = detalle.get("idProducto").toString();
            preparedStatement.setString(1, idProducto);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int stockActualProducto = resultSet.getInt("stock");
                int cantidadRequerida = Integer.parseInt(detalle.get("cantidad").toString());
                if (cantidadRequerida <= stockActualProducto) {
                    int precioDeVenta = resultSet.getInt("precioVenta");
                    totalVenta = totalVenta + (precioDeVenta * cantidadRequerida);
                    detalle.put("precio", precioDeVenta);
                } else {
                    stockSuficiente = false;
                    String nombreProducto = detalle.get("nombre").toString();
                    JSONObject productoConStockInsuficiente = new JSONObject();
                    productoConStockInsuficiente.put("idProducto", idProducto);                    
                    productoConStockInsuficiente.put("stockMaximo", stockActualProducto);
                    productoConStockInsuficiente.put("nombre", nombreProducto);
                    this.productosConStockInsuficiente.add(productoConStockInsuficiente);
                }
            }

        }

        preparedStatement.clearParameters();
        
        if(stockSuficiente == false){
            this.retorno.add(productosConStockInsuficiente);
        }
        
        JSONObject resultado = new JSONObject();
        resultado.put("stockSuficiente", stockSuficiente);
        resultado.put("totalVenta", totalVenta);
        this.retorno.add(0, resultado);

        return this.retorno;
    }
    
}
