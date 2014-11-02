package com.socket;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author uagrm
 */

import java.net.*;
import java.io.*;

class Conector extends Thread
    {
        Socket clienteSocket;        
        boolean Continue = true;
        Servidor Owner;
        
        Conector(Servidor owner,Socket s)
        {
            clienteSocket = s;
            Owner = owner;
        }
       
        public void run()
        {            
            DataInputStream in = null; 
            DataOutputStream out = null;
 
            try
            {                                
                in = new DataInputStream(clienteSocket.getInputStream());
                out = new DataOutputStream(clienteSocket.getOutputStream());
                
                while(Continue)
                {                    
                    String clienteCommando = in.readUTF();
                    Owner.DispatchOnReceive(new ReceiveEvent(this,new Paquete(String.valueOf(clienteSocket.getLocalPort()),clienteCommando)));
                 
                    System.out.println(clienteSocket.getLocalPort());
                    System.out.println(clienteCommando);
                    if(clienteCommando.equalsIgnoreCase("Salir"))
                    {
                         Continue = false;   
                     }
                    else
                    {
                       
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            finally
            {
                
                try
                {                    
                    in.close();
                    out.close();
                    clienteSocket.close();
                    System.out.println("Terminado");
                }
                catch(IOException ioe)
                {
                    
                }
            }
        }

    } 
