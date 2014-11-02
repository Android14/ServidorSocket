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
import com.google.gson.Gson;
import java.util.*;
import com.dao.Puntos;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class Cliente {
    private static Gson gson=new Gson();
    public static void main(String[] args)
    {   
        
        Socket cliente = null;
        try
        {
            cliente = new Socket("127.0.0.1", 2050);
        }        
        catch(UnknownHostException uhe)
        {
           
        }
        catch(IOException ioe)
        {
            
        }
        
        if(cliente == null)
            System.exit(-1);
        
        DataInputStream in = null;
        DataOutputStream out = null;
        
        try
        {
            in = new DataInputStream(cliente.getInputStream());
            out = new DataOutputStream(cliente.getOutputStream());
            out.writeUTF("getPuntos");
            out.flush();
            String jsonListaPuntos=in.readUTF();
            Type type = new TypeToken<List<Puntos>>(){}.getType();
            List<Puntos> lp = gson.fromJson(jsonListaPuntos, type);
            for (Iterator<Puntos> it = lp.iterator(); it.hasNext();) {
                Puntos puntos = it.next();
                System.out.println(puntos.getNombre());
            }
        }
        catch(Exception ioe)
        {
           System.out.println(ioe); 
        }
        finally
        {
            try
            {
                out.close();
                in.close();
                cliente.close();
            }
            catch(Exception e)
            {
               
            }                
        }    
    }
}
