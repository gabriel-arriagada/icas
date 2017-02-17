/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.vigenciacliente;

import icas.accesodatos.Conexion;
import icas.accesodatos.Operacion;
import icas.dominio.VigenciaCliente;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gabriel
 */
public class CrearVigenciaCliente extends Operacion {

    private PreparedStatement preparedStatement;
    private final VigenciaCliente vigenciaCliente;

    public CrearVigenciaCliente(Conexion conexion, VigenciaCliente vigenciaCliente) {
        this.conexion = conexion;
        this.vigenciaCliente = vigenciaCliente;
    }

    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        preparedStatement = null;

        String consulta = "INSERT INTO vigenciaCliente(rutCliente, fecha, idRazon, vigente)"
                + " VALUES(?,?,?,?)";

        if (this.conexion.abrirConexion()) {
            try {
                this.conexion.getConnection().setAutoCommit(false);
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setString(1, vigenciaCliente.getRutCliente());
                preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                preparedStatement.setInt(3, vigenciaCliente.getIdRazon());
                preparedStatement.setBoolean(4, vigenciaCliente.isVigente());
                int filasAfectadas = preparedStatement.executeUpdate();

                if (filasAfectadas > 0) {
                    this.conexion.getConnection().commit();
                    retorno = true;
                } else {
                    this.conexion.getConnection().rollback();
                }

            } catch (SQLException ex) {
                try {
                    this.conexion.getConnection().rollback();
                    Logger.getLogger(CrearVigenciaCliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(CrearVigenciaCliente.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new RuntimeException(ex.getMessage());
                }
            } finally {
                try {
                    this.preparedStatement.close();
                    this.conexion.getConnection().setAutoCommit(true);
                    this.conexion.cerrarConexion();
                } catch (SQLException ex) {
                    Logger.getLogger(CrearVigenciaCliente.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        return retorno;
    }

}
