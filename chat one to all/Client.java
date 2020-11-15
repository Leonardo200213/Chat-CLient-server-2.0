/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.server.client.pkg07;

import java.io.*; 
import java.net.*; 
import java.util.Scanner;



/**
 *
 * @author Leonardo Messeri
 */
public class Client {
    final static int ServerPort = 3333; 
  
    public void main() throws UnknownHostException, IOException{ 
        Scanner scn = new Scanner(System.in); 
          
//ottiene l'ip di localhost
        InetAddress ip = InetAddress.getByName("localhost"); 
          
//crea la connessione tramite l'ip e il numero di porta
        Socket s = new Socket(ip, ServerPort); 
          
//aggiunge input e output stream
        DataInputStream dis = new DataInputStream(s.getInputStream()); 
        DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
        
        
        
        
//creazione del thread per l'invio del messaggio
        Thread sendMessage = new Thread(new Runnable(){ 
            @Override
            public void run(){ 
                while (true){
//legge il messaggio
                    String messaggio = scn.nextLine();
                    try { 
//scrive il messaggio sull'output stream
                        dos.writeUTF(messaggio); 
                    } catch (IOException e){ 
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
          
        
        
        
//creazione del thread per la lettura del messaggio
        Thread readMessage = new Thread(new Runnable(){ 
            @Override
            public void run(){
                while (true){ 
                    try { 
//legge il messaggio inviato a questo client
                        String messaggio = dis.readUTF(); 
                        System.out.println(messaggio); 
                    } catch (IOException e){
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
  
//entrambi i thread vengono eseguiti
        sendMessage.start(); 
        readMessage.start();
    }
}
