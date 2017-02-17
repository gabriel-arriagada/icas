package icas.accesodatos.venta;

import icas.accesodatos.Conexion;
import icas.dominio.Venta;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class VentaPrepago extends AbstractVenta {

    private final JSONArray resultadoVenta;
    private final Venta venta;

    public VentaPrepago(Conexion conexion, JSONArray detalleVenta, Venta venta) {
        this.detalleVenta = detalleVenta;
        this.resultadoVenta = new JSONArray();
        this.conexion = conexion;
        this.venta = venta;
    }

    @Override
    public JSONArray generarVenta() {
        JSONObject exito = new JSONObject();
        exito.put("exito", false);
        if (conexion.abrirConexion()) {

            try {
                conexion.getConnection().setAutoCommit(false);
                IOperacionJSON consultaStockPrecio = new ConsultaStockPrecio(detalleVenta);
                JSONArray resultadoVerificarStock = consultaStockPrecio.ejecutar(conexion);
                boolean stockSuficiente = (boolean) ((JSONObject) resultadoVerificarStock.get(0)).get("stockSuficiente");

                if (stockSuficiente == true) {

                    int totalVenta = (int) ((JSONObject) resultadoVerificarStock.get(0)).get("totalVenta");
                    IOperacionInt consultarSaldoCliente = new ConsultaSaldoCliente(venta.getRutCliente());
                    int saldoCliente = consultarSaldoCliente.ejecutar(conexion);

                    if (saldoCliente >= totalVenta) {

                        this.venta.setTotal(0);//Cero por que ya está pagado!
                        IOperacionInt insertarVentaGenerarId = new InsertarVentaGenerarId(venta);
                        int idVenta = insertarVentaGenerarId.ejecutar(conexion);
                        IOperacionInt insertarDetalleVenta = new InsertarDetalleDeVenta(detalleVenta, idVenta);
                        insertarDetalleVenta.ejecutar(conexion);
                        IOperacionInt reducirSaldoCliente = new ReducirSaldoCliente(totalVenta, venta.getRutCliente());
                        reducirSaldoCliente.ejecutar(conexion);
                        IOperacionInt reducirStockProductos = new ReducirStockProductos(detalleVenta);
                        reducirStockProductos.ejecutar(conexion);

                        if (this.venta.tienePedido() == true) {
                            IOperacionInt insertarPedido = new InsertarPedido(idVenta, this.venta.getHoraDeRetiro(), this.venta.getComentario());
                            int idPedido = insertarPedido.ejecutar(conexion);
                            int nuevoSaldoCliente = saldoCliente - totalVenta;
                            exito.put("idPedido", idPedido);
                            exito.put("horaRetiroPedido", this.venta.getHoraDeRetiro());
                            exito.put("ventaPagada", this.venta.isPagada());
                            exito.put("nuevoSaldoCliente", nuevoSaldoCliente);
                            conexion.getConnection().commit();
                            exito.replace("exito", false, true);                           

                        } else {
                            conexion.getConnection().commit();
                            exito.replace("exito", false, true);
                        }

                    } else {
                        exito.put("razon", "saldoInsuficiente");
                    }

                } else {
                    exito.put("razon", "stockInsuficiente");
                    resultadoVenta.add(resultadoVerificarStock.get(1));
                }

            } catch (SQLException ex) {
                Logger.getLogger(VentaPrepago.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    this.conexion.getConnection().rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(VentaPrepago.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    this.conexion.getConnection().setAutoCommit(true);
                    this.conexion.cerrarConexion();
                } catch (SQLException ex) {
                    Logger.getLogger(VentaPrepago.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        resultadoVenta.add(0, exito);
        return resultadoVenta;
    }
}
