package com.socket;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author uagrm
 */
import java.io.*;
import java.net.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

public final class Servidor extends Thread
{        
    private ServerSocket ServidorSocket=null;
    private EventListenerList ListenerList=new EventListenerList();
    private HashMap<String, Socket> clientes;
    private Controlador controler=null;
    
    public Servidor(int Puerto) 
    {
        try
        {
            ServidorSocket = new ServerSocket(Puerto);
            clientes = new HashMap<String, Socket>();
            controler = new Controlador(this);
            this.addReceiveListener(controler);
        }
        catch(IOException ioe)
        {
            System.out.println("No se pudo abrir el socket");
            System.exit(-1);
        }
        
        System.out.println("Esperando clientes...");
                
        
    }
    
    void addReceiveListener(ReceiveListener l) {
        ListenerList.add(ReceiveListener.class, l);
    }

    void removeReceiveListener(ReceiveListener l) {
         ListenerList.remove(ReceiveListener.class, l);
    }

    protected void DispatchOnReceive(ReceiveEvent e) {
     ReceiveListener[] ls = ListenerList.getListeners(ReceiveListener.class);
     for (ReceiveListener l : ls) {
      l.OnReceive(e);
      
     }

    }
    
    public void enviar(String cliente, String dato) throws IOException{
        if(clientes.containsKey(cliente)){
            Socket socket = clientes.get(cliente);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            try {
                out.writeUTF(dato);
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void enviarTodos(String dato) throws IOException{
        Collection<Socket> values = clientes.values();
        Iterator<Socket> iterator = values.iterator();
        
        while(iterator.hasNext()){
            Socket socket = iterator.next();
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            try {
                out.writeUTF(dato);
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void run() {
       while(true)
        {                        
            try
            {
                Socket clienteSocket = ServidorSocket.accept();
                Conector hilo = new Conector(this,clienteSocket);
                hilo.start();
                System.out.println("Se conecto");
                clientes.put(String.valueOf(clienteSocket.getLocalPort()),clienteSocket);
            }
            catch(IOException ioe)
            {
                
            }
        } 
    }
    public static void main(String args[]){
        Servidor server = new Servidor(2050);
        server.start();
        
    }   
   
}