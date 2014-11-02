package com.socket;


import java.util.EventObject;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author uagrm
 */
public class ReceiveEvent extends EventObject{
   Paquete Dato; 
   public ReceiveEvent(Object source,Paquete p) {
    super(source);
    Dato=p;
  } 
}


