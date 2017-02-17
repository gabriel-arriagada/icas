/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.icas.accesodatos.hashProducto;

import icas.accesodatos.ConexionPostgreSql;
import icas.accesodatos.Leer;
import icas.accesodatos.comprascliente.LeerComprasFrecuentes;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author gabo
 */
public class PUX_TestLeerComprasFrecuentes {

    @Test
    public void pruebaA() {
        Leer frecuentes = new LeerComprasFrecuentes(new ConexionPostgreSql(), "17334973-4");
        assertTrue(frecuentes.leer()!= null);
    }
   

}
