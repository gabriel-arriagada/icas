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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author gabo
 */
public class VerificarClaveCliente extends Operacion {

    private PreparedStatement preparedStatement;
    private final Cliente cliente;
    private Encriptar encriptar;

    public VerificarClaveCliente(Conexion conexion, Cliente cliente) {
        this.conexion = conexion;
        this.cliente = cliente;
    }

    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        byte[] claveEncriptada = null;
        try {
            encriptar = new Encriptar();
            claveEncriptada = encriptar.encriptar(cliente.getClave());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(VerificarCliente.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }

        if (this.conexion.abrirConexion()) {
            try {
                String consulta = "SELECT e.nombre, e.apellido "
                        + "FROM cliente e WHERE e.rut = ? "
                        + "AND e.clave = ? "
                        + "AND (SELECT vc.vigente FROM vigenciaCliente vc "
                        + "WHERE vc.rutCliente = ? ORDER BY fecha DESC LIMIT 1) = true";

                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);
                preparedStatement.setString(1, cliente.getRut());
                preparedStatement.setBytes(2, claveEncriptada);
                preparedStatement.setString(3, cliente.getRut());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        retorno = true;
                    }
                    preparedStatement.close();                   
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage());
            } finally {                
                this.conexion.cerrarConexion();
            }
        }
        return retorno;
    }

}
