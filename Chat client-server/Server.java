/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.server.client.pkg05;


import java.io.*; 
import java.util.*;
import java.net.*; 


/**
 *
 * @author Leonardo Messeri
 */
public class Server{

//lc = lista client
//vettore per inserire i vari client
    static Vector<ClientHandler> lc = new Vector<>();
//contatore dei client
    public static int i = 0;
    
    
  
    public void main() throws UnknownHostException, IOException{
//il server ascolta sulla porta 2104
        ServerSocket ss = new ServerSocket(3300);
//while infinito per ricevere ogni richiesta client
        while (true){
            
//accetta la richiesta
            Socket s = ss.accept(); 
            System.out.println("Ricevuta nuova richiesta del client " + s);
//ottiene i dati input e output stream
            BufferedReader dis = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
//creazione di ClientHandler per la gestione del server
            ClientHandler ch = new ClientHandler(s,"Client" + i, dis, dos);
            System.out.println("Client " + i + " è entrato nella chat");
            if(i == 0){
                dos.writeBytes("Sei il Client 0 e sei il primo nella chat" + '\n');
            }
            else if(i > 0){
                dos.writeBytes("Sei il Client " + i + '\n');
                int x = i;
                do {
                   dos.writeBytes("Nella chat è presente: Client " + x-- + '\n');
                }while(x != 0);
            }
            Thread t = new Thread(ch);
            System.out.println("Elaborazione...");
            System.out.println("Il client è ora nella lista");
            System.out.println("");
//Aggiunta del client alla lista
            lc.add(ch);
//parte il thread 
            t.start();
//incrementa il numero del client
            i++;
        } 
    } 
} 







class ClientHandler implements Runnable{ 
    Scanner scn = new Scanner(System.in); 
    public String name; 
    public final BufferedReader dis; 
    public final DataOutputStream dos; 
    public Socket s; 
    public boolean login;
      

//costruttore
    public ClientHandler(Socket s, String name, BufferedReader dis, DataOutputStream dos){ 
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.login=true; 
    }
    
//metodo run    
    @Override
    public void run(){
        String ricevuta; 
        
        while (true){
            try{ 
//riceve la stringa 
                ricevuta = dis.readLine(); 
//mostra la stringa
                System.out.println(ricevuta); 
//se la stringa è uguale a logout allora termina la chat  
                if(ricevuta.equals("Logout")){ 
                    this.login=false; 
                    this.s.close();
                    break; 
                } 
                
//divide la stringa in messaggio e destinatario tramite tokenizer
                
                    StringTokenizer st = new StringTokenizer(ricevuta, "#");
                    String MsgToSend = st.nextToken(); 
                    String recipient = st.nextToken();
                
                
                
                
//cerca il destinatario nella lista dei client
                for (ClientHandler mc : Server.lc){ 
//se trova il destinatario invia il messaggio tramite l'output
                    if (mc.name.equals(recipient) && mc.login==true){ 
                        mc.dos.writeUTF(this.name+ " : " +MsgToSend); 
                        break;
                    } else if(mc.login==false){
                        System.out.println("Il client destinatario è uscito dalla chat"); 
                    } else if(Server.i <= 1){
                        System.out.println("Sei l'unico partecipante alla chat"); 
                    } else if(!mc.name.equals(recipient)){
                        mc.dos.writeUTF(MsgToSend); 
                    }
                    
                }
            } catch (IOException e){ 
                e.printStackTrace(); 
            }
        } 
        try{ 
//chiude le risorse
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
}
