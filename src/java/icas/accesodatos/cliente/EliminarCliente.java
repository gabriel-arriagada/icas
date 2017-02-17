
package icas.accesodatos.cliente;

import icas.accesodatos.Conexion;
import icas.accesodatos.Operacion;
import icas.dominio.Cliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EliminarCliente extends Operacion {

    private PreparedStatement statementSelectVentas;
    private PreparedStatement statementEliminarVigencia;
    private PreparedStatement statementEliminarCuenta;
    private PreparedStatement statementEliminarCliente;
    private final Cliente cliente;

    public EliminarCliente(Conexion conexion, Cliente cliente) {
        this.conexion = conexion;
        this.cliente = cliente;
    }

    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        statementSelectVentas = null;
        statementEliminarVigencia = null;
        statementEliminarCuenta = null;
        statementEliminarCliente = null;

        if (this.conexion.abrirConexion()) {
            try {
                this.conexion.getConnection().setAutoCommit(false);

                String selectVentas = "SELECT idVenta FROM venta WHERE rutCliente = ?";                
                statementSelectVentas = this.conexion.getConnection().prepareStatement(selectVentas);
                statementSelectVentas.setString(1, cliente.getRut());
                ResultSet resultSet = statementSelectVentas.executeQuery();
                if (!resultSet.next()) {
                    String eliminiarVigencia = "DELETE FROM vigenciaCliente WHERE rutCliente = ?";
                    statementEliminarVigencia = this.conexion.getConnection().prepareStatement(eliminiarVigencia);
                    statementEliminarVigencia.setString(1, cliente.getRut());
                    statementEliminarVigencia.executeUpdate();
                    
                    String eliminarCuenta = "DELETE FROM cuenta WHERE rutCliente = ?";
                    statementEliminarCuenta = this.conexion.getConnection().prepareStatement(eliminarCuenta);
                    statementEliminarCuenta.setString(1, cliente.getRut());
                    statementEliminarCuenta.executeUpdate();
                    
                    String eliminarCliente = "DELETE FROM cliente WHERE rut = ?";
                    statementEliminarCliente = this.conexion.getConnection().prepareStatement(eliminarCliente);
                    statementEliminarCliente.setString(1, cliente.getRut());
                    statementEliminarCliente.executeUpdate();
                    
                    this.conexion.getConnection().commit();
                    retorno = true;
                }
 
            } catch (SQLException ex) {
                try {
                    this.conexion.getConnection().rollback();
                    Logger.getLogger(EliminarCliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(EliminarCliente.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new RuntimeException(ex.getMessage());
                }
            } finally {
                try {
                    if(this.statementSelectVentas != null){
                        this.statementSelectVentas.close();
                    }
                    
                    if(this.statementEliminarVigencia != null){
                        this.statementEliminarVigencia.close();
                    }
                    if(this.statementEliminarCuenta != null){
                        this.statementEliminarCuenta.close();
                    }
                    if(this.statementEliminarCliente != null){
                        this.statementEliminarCliente.close();
                    }
                    this.conexion.getConnection().setAutoCommit(true);
                    this.conexion.cerrarConexion();
                } catch (SQLException ex) {
                    Logger.getLogger(EliminarCliente.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        return retorno;
    }
}
