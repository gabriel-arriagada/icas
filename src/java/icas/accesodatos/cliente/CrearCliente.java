/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.cliente;

import icas.accesodatos.Conexion;
import icas.accesodatos.Encriptar;
import icas.accesodatos.Operacion;
import icas.dominio.Cliente;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
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
public class CrearCliente extends Operacion {

    private PreparedStatement preparedStatement;
    private Encriptar encriptar;
    private final Cliente cliente;

    public CrearCliente(Conexion conexion, Cliente cliente) {
        this.conexion = conexion;
        this.cliente = cliente;
    }

    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        byte[] claveEncriptada = null;
        preparedStatement = null;

        try {
            encriptar = new Encriptar();
            claveEncriptada = encriptar.encriptar(cliente.getClave());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(CrearCliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        String consulta = "INSERT INTO cliente"
                + " (rut, clave, nombre, apellido, correo) "
                + " VALUES(?, ?, ?, ?, ?)";

        if (this.conexion.abrirConexion()) {
            try {
                this.conexion.getConnection().setAutoCommit(false);
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setString(1, cliente.getRut());
                preparedStatement.setBytes(2, claveEncriptada);
                preparedStatement.setString(3, cliente.getNombre());
                preparedStatement.setString(4, cliente.getApellido());
                preparedStatement.setString(5, cliente.getCorreo());
                int filasAfectadas = preparedStatement.executeUpdate();
                if (filasAfectadas > 0) {
                    AgregarVigenciaCliente av = new AgregarVigenciaCliente();
                    if (av.agregarVigencia(conexion, preparedStatement, cliente.getRut())) {
                        CrearCuenta cc = new CrearCuenta();
                        if (cc.crearCuenta(conexion, preparedStatement, cliente.getRut())) {
                            this.conexion.getConnection().commit();
                            retorno = true;
                        } else {
                            this.conexion.getConnection().rollback();
                        }                        
                    } else {
                        this.conexion.getConnection().rollback();
                    }
                } else {
                    this.conexion.getConnection().rollback();
                }

            } catch (SQLException ex) {
                try {
                    this.conexion.getConnection().rollback();
                    Logger.getLogger(CrearCliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(CrearCliente.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new RuntimeException(ex.getMessage());
                }
            } finally {
                try {
                    if (this.preparedStatement != null) {
                        this.preparedStatement.close();
                    }
                    this.conexion.getConnection().setAutoCommit(true);
                    this.conexion.cerrarConexion();
                } catch (SQLException ex) {
                    Logger.getLogger(CrearCliente.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        return retorno;
    }

}
