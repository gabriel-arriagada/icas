package icas.accesodatos.empleado;

import icas.accesodatos.Conexion;
import icas.accesodatos.Operacion;
import icas.dominio.Empleado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EliminarEmpleado extends Operacion {

    private PreparedStatement statementSelectRolEmpleado;
    private PreparedStatement statementSelectVentas;
    private PreparedStatement statementEliminarVigencia;
    private PreparedStatement statementEliminarAutoVenta;
    private PreparedStatement statementEliminarEmpleado;
    private final Empleado empleado;

    public EliminarEmpleado(Conexion conexion, Empleado empleado) {
        this.conexion = conexion;
        this.empleado = empleado;
    }

    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        statementSelectRolEmpleado = null;
        statementSelectVentas = null;
        statementEliminarVigencia = null;
        statementEliminarAutoVenta = null;
        statementEliminarEmpleado = null;

        if (this.conexion.abrirConexion()) {
            try {
                this.conexion.getConnection().setAutoCommit(false);

                String selectRol = "SELECT idRol FROM empleado WHERE rut = ?";
                statementSelectRolEmpleado = this.conexion.getConnection().prepareStatement(selectRol);
                statementSelectRolEmpleado.setString(1, empleado.getRut());
                ResultSet __resultSet = statementSelectRolEmpleado.executeQuery();
                int idRol = 0;
                if (__resultSet.next()) {
                    idRol = __resultSet.getInt("idRol");
                }

                if (idRol != 1) {
                    String selectVentas = "SELECT idVenta FROM venta WHERE rutVendedor = ?";
                    statementSelectVentas = this.conexion.getConnection().prepareStatement(selectVentas);
                    statementSelectVentas.setString(1, empleado.getRut());
                    ResultSet resultSet = statementSelectVentas.executeQuery();
                    if (!resultSet.next()) {
                        String eliminiarVigencia = "DELETE FROM vigenciaEmpleado WHERE rutEmpleado = ?";
                        statementEliminarVigencia = this.conexion.getConnection().prepareStatement(eliminiarVigencia);
                        statementEliminarVigencia.setString(1, empleado.getRut());
                        statementEliminarVigencia.executeUpdate();

                        String eliminarCuenta = "DELETE FROM autoVenta WHERE rutEmpleado = ?";
                        statementEliminarAutoVenta = this.conexion.getConnection().prepareStatement(eliminarCuenta);
                        statementEliminarAutoVenta.setString(1, empleado.getRut());
                        statementEliminarAutoVenta.executeUpdate();

                        String eliminarEmpleado = "DELETE FROM empleado WHERE rut = ?";
                        statementEliminarEmpleado = this.conexion.getConnection().prepareStatement(eliminarEmpleado);
                        statementEliminarEmpleado.setString(1, empleado.getRut());
                        statementEliminarEmpleado.executeUpdate();

                        this.conexion.getConnection().commit();
                        retorno = true;
                    }
                }

            } catch (SQLException ex) {
                try {
                    this.conexion.getConnection().rollback();
                    Logger.getLogger(EliminarEmpleado.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(EliminarEmpleado.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new RuntimeException(ex.getMessage());
                }
            } finally {
                try {
                    if (this.statementSelectRolEmpleado != null) {
                        this.statementSelectRolEmpleado.close();
                    }
                    
                    if (this.statementSelectVentas != null) {
                        this.statementSelectVentas.close();
                    }

                    if (this.statementEliminarVigencia != null) {
                        this.statementEliminarVigencia.close();
                    }
                    if (this.statementEliminarAutoVenta != null) {
                        this.statementEliminarAutoVenta.close();
                    }
                    if (this.statementEliminarEmpleado != null) {
                        this.statementEliminarEmpleado.close();
                    }
                    this.conexion.getConnection().setAutoCommit(true);
                    this.conexion.cerrarConexion();
                } catch (SQLException ex) {
                    Logger.getLogger(EliminarEmpleado.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        return retorno;
    }

}
