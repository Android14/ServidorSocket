/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socket;

import com.dao.Puntos;
import com.dao.PuntosDAO;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author uagrm
 */
public class Controlador implements ReceiveListener {
    private Servidor server;
    private Gson gson=new Gson();
    public Controlador(Servidor server) {
        this.server = server;
    }
    
    @Override
    public void OnReceive(ReceiveEvent e) {
        try {
            if (e.Dato.getDato().startsWith("Sumar")){
                String[] Param = e.Dato.getDato().split(",");
                int a = Integer.parseInt(Param[1]);
                int b = Integer.parseInt(Param[2]);
                int r = a+b+a+a+b+a;
                server.enviar(e.Dato.getIP(),String.valueOf(r));
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
