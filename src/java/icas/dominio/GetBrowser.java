/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.dominio;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author gabo
 */
public class GetBrowser {
            
    public boolean esChrome(HttpServletRequest request){
        boolean esChrome = false;
        String navegador = request.getHeader("User-Agent");
        if(navegador.contains("Chrome")){
            esChrome = true;            
        }
        return esChrome;
    }
}
