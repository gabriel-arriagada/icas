/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.logicanegocio.websocket;

import icas.logicanegocio.sesion.VerificarSesion;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Gabriel
 */
@ServerEndpoint(value = "/comandas", configurator = ModificarHandShake.class)
public class ComandasWebSocket {

    private static Session SESION_RECEPTORA_DE_COMANDAS;
    private HttpSession httpSession;
    private VerificarSesion verificarSesion;

    @OnOpen
    public void open(Session currentSession, EndpointConfig config) throws IOException {
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (this.httpSession.getAttribute("usuario") != null) {
            this.verificarSesion = new VerificarSesion();
            boolean esReceptor = this.verificarSesion.esReceptorDeComandas(this.httpSession);
            if (esReceptor) {
                SESION_RECEPTORA_DE_COMANDAS = currentSession;
            }
        }       
    }

    @OnMessage
    public void message(String comanda) throws IOException {
        if (SESION_RECEPTORA_DE_COMANDAS != null) {
            SESION_RECEPTORA_DE_COMANDAS.getBasicRemote().sendText(comanda);
        }
    }

    @OnClose
    public void close(Session session) throws Exception {
        Logger.getLogger(ComandasWebSocket.class.getName())
                .log(Level.SEVERE, "Sesi\u00f3n websocket {0} cerrada", session.getId());
    }

    @OnError
    public void error(Throwable throwable) throws Exception {
        Logger.getLogger(ComandasWebSocket.class.getName()).log(Level.SEVERE, "Error web socket",throwable);
    }

}
