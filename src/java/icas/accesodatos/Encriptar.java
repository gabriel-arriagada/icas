/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.accesodatos;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Gabriel
 */
public class Encriptar {
    
    private final String llave;
    private final SecretKeySpec key;
    private final Cipher cipher;
    
    public Encriptar() throws NoSuchAlgorithmException, NoSuchPaddingException{
        this.llave = "qwerty|?*,.34_@?";//un caracter = 8 bits, so 16 * 8 = 128
        key = new SecretKeySpec(llave.getBytes(), "AES");
        cipher = Cipher.getInstance("AES");
    }
    
    public byte[] encriptar(String clave) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encriptado = cipher.doFinal(clave.getBytes());   
        return encriptado;
    }
  
}
