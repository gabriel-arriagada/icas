/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.accesodatos.cliente;

import icas.accesodatos.Conexion;
import icas.accesodatos.Encriptar;
import icas.accesodatos.Operacion;
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
public class CambiarClaveCliente extends Operacion{

    private PreparedStatement preparedStatement;        
    private final String rut;
    private final String clave;        
    private Encriptar encriptar;

    public CambiarClaveCliente(Conexion conexion, String rut, String clave) {
        this.conexion = conexion;
        this.rut = rut;
        this.clave = clave;
    }

    @Override
    public boolean ejecutar() {
        boolean retorno = false;
        preparedStatement = null;
        
        byte[] claveEncriptada = null;
        try {
            encriptar = new Encriptar();
            claveEncriptada = encriptar.encriptar(clave);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(VerificarCliente.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
        
        String consulta = "UPDATE cliente SET clave = ? WHERE rut = ?";                

        if (this.conexion.abrirConexion()) {
            try {
                preparedStatement = this.conexion.getConnection().prepareStatement(consulta);                
                preparedStatement.setBytes(1, claveEncriptada);                
                preparedStatement.setString(2, rut);                                
                int filasAfectadas = preparedStatement.executeUpdate();
                if (filasAfectadas > 0) {
                    retorno = true;
                }

            } catch (SQLException ex) {
                try {
                    this.conexion.getConnection().rollback();
                    Logger.getLogger(CambiarClaveCliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(CambiarClaveCliente.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new RuntimeException(ex.getMessage());
                }
            } finally {
                try {
                    this.preparedStatement.close();                    
                    this.conexion.cerrarConexion();
                } catch (SQLException ex) {
                    Logger.getLogger(CambiarClaveCliente.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        return retorno;
    }

}
