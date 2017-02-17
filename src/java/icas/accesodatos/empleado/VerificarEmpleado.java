/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.empleado;

import icas.accesodatos.BuscarT;
import icas.accesodatos.Conexion;
import icas.accesodatos.Encriptar;
import icas.dominio.Empleado;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Gabriel
 */
public class VerificarEmpleado extends BuscarT<Empleado> {

    private PreparedStatement preparedStatement;
    private Encriptar encriptar;
    private Empleado empleado;

    public VerificarEmpleado(Conexion conexion, Empleado empleado) {
        this.conexion = conexion;
        this.empleado = empleado;
    }

    @Override
    public Empleado buscar() {
        byte[] claveEncriptada = null;
        try {
            encriptar = new Encriptar();
            claveEncriptada = encriptar.encriptar(empleado.getClave());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(VerificarEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }

        if (this.conexion.abrirConexion()) {
            try {
                String consulta = "SELECT e.nombre, e.apellido, r.nombre AS rol "
                        + "FROM empleado e, rol r WHERE e.rut = ? "
                        + "AND e.clave = ? "
                        + "AND e.idRol = r.idRol "
                        + "AND (SELECT ve.vigente FROM vigenciaEmpleado ve "
                        + "WHERE ve.rutEmpleado = ? ORDER BY ve.fecha DESC LIMIT 1) = true";

                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setString(1, empleado.getRut());
                preparedStatement.setBytes(2, claveEncriptada);
                preparedStatement.setString(3, empleado.getRut());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        empleado.setNombre(resultSet.getString("nombre"));
                        empleado.setApellido(resultSet.getString("apellido"));
                        empleado.setRol(resultSet.getString("rol"));
                    } else {
                        empleado = null;
                    }
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage());
            } finally {
                this.conexion.cerrarConexion();
            }
        }
        return empleado;
    }

}
