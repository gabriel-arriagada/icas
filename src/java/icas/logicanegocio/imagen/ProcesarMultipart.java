/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.imagen;

import icas.dominio.UrlImagen;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;

/**
 *
 * @author Gabriel
 */
public class ProcesarMultipart {

    public String getTexto(Part part) throws UnsupportedEncodingException, IOException {
        String valor;
        BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[10240];//10kilobytes
        for (int length = 0; (length = reader.read(buffer)) > 0;) {
            stringBuilder.append(buffer, 0, length);
        }
        valor = stringBuilder.toString();
        return valor;
    }

    public String getFormato(Part part) {
        String formato = null;
        switch (part.getHeader("content-type")) {
            case "image/png":
                formato = ".png";
                break;
            case "image/jpg":
                formato = ".jpg";
                break;
            case "image/jpeg":
                formato = ".jpg";
                break;
        }
        return formato;
    }

    public boolean guardarImagen(Part part, String idProducto, String formato) {
        boolean guardada = false;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file;
        try {
            inputStream = (InputStream) part.getInputStream();
            file = new File(UrlImagen.URL_GUARDAR_IMAGENES + idProducto + formato);
            fileOutputStream = new FileOutputStream(file);
            int dato = inputStream.read();
            while (dato != -1) {
                fileOutputStream.write(dato);
                dato = inputStream.read();
            }
            guardada = true;
        } catch (IOException ex) {
            Logger.getLogger(ProcesarMultipart.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(fileOutputStream != null && inputStream != null){
                    fileOutputStream.close();
                    inputStream.close();
                }                
            } catch (IOException ex) {
                Logger.getLogger(ProcesarMultipart.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return guardada;
    }
}
