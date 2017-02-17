package icas.accesodatos.cuenta;

import icas.dominio.Cuenta;
import icas.accesodatos.Conexion;
import icas.accesodatos.Operacion;
import icas.accesodatos.venta.IOperacionInt;
import icas.accesodatos.venta.InsertarVentaGenerarId;
import icas.dominio.Venta;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CargarCuenta extends Operacion {

    private PreparedStatement preparedStatement;       
    private final String rutVendedor;
    private final Cuenta cuenta;

    public CargarCuenta(Conexion conexion, String rutVendedor, Cuenta cuenta) {
        this.conexion = conexion;        
        this.rutVendedor = rutVendedor;
        this.cuenta = cuenta;
    }

    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        preparedStatement = null;

        String updateSaldo = "UPDATE cuenta SET saldo = (saldo + ?)  WHERE rutCliente = ?";
        String insertDetalle = "INSERT INTO detalle(idVenta, idProducto, cantidad, precio, subtotal) VALUES(?,?,?,?,?)";

        if (this.conexion.abrirConexion()) {
            try {
                this.conexion.getConnection().setAutoCommit(false);
                Venta venta = new Venta();
                venta.setRutVendedor(rutVendedor);
                venta.setRutCliente(cuenta.getRut());
                venta.setTotal(cuenta.getSaldo());
                venta.setIdFormaPago(3);/*3: forma de pago efectivo, por ahora.*/
                venta.setPagada(true);
                IOperacionInt insertarVentaGenerarId = new InsertarVentaGenerarId(venta);
                int idVenta = insertarVentaGenerarId.ejecutar(conexion);
                if (idVenta > 0) {
                    preparedStatement = this.conexion.getConnection().prepareStatement(insertDetalle);
                    preparedStatement.setInt(1, idVenta);
                    preparedStatement.setString(2, "recarga");//idProducto
                    preparedStatement.setInt(3, 1);//cantidad
                    preparedStatement.setInt(4, cuenta.getSaldo());//precio
                    preparedStatement.setInt(5, cuenta.getSaldo());//subTotal                                                            
                    if (preparedStatement.executeUpdate() > 0) {
                        preparedStatement.clearParameters();
                        preparedStatement = this.conexion.getConnection().prepareStatement(updateSaldo);
                        preparedStatement.setInt(1, cuenta.getSaldo());
                        preparedStatement.setString(2, cuenta.getRut());                       
                        if (preparedStatement.executeUpdate() > 0) {
                            this.conexion.getConnection().commit();
                            retorno = true;
                        } else {
                            this.conexion.getConnection().rollback();
                        }
                    }else{
                        this.conexion.getConnection().rollback();
                    }
                }
            } catch (SQLException ex) {
                try {
                    this.conexion.getConnection().rollback();
                    Logger.getLogger(CargarCuenta.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(CargarCuenta.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new RuntimeException(ex.getMessage());
                }
            } finally {
                try {
                    this.preparedStatement.close();
                    this.conexion.getConnection().setAutoCommit(true);
                    this.conexion.cerrarConexion();
                } catch (SQLException ex) {
                    Logger.getLogger(CargarCuenta.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        return retorno;
    }
}
