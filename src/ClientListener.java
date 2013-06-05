
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ashley
 */
public class ClientListener extends Thread{
    ServerSocket listenSocket;
    Socket clientReceiveSocket;
    ObjectInputStream ois;
    Message msg;
    
    int port;
    ClientListener(){}
    ClientListener(int _port){port=_port;}
    

    public void run(){
        try {
            System.out.println("Client listening to server on port "+port+"\n");
            listenSocket=new ServerSocket(port);
            while(true){
                clientReceiveSocket=listenSocket.accept();
                 System.out.println("getMsg");
                ois=new ObjectInputStream(clientReceiveSocket.getInputStream());
                msg=(Message)ois.readObject();
                this.checkMsgFromServer(msg);               
                ois.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void checkMsgFromServer(Message msg){
        int type=msg.getType();
        String text=msg.getMsg();
        String sender=msg.getNickName();      
        switch(type){
            case 0:
                if(sender.equals("System Msg:")){
                    ClientScreen.updateMsgScreen("========System Anoncement:"+text+"========\n");
                }else if(sender.equals("Client List:")){
                    System.out.println("get client list");
                    System.out.println(text);
                    String[] list=text.split(",");
                    ClientScreen.clientList.setList(list);  
                    ClientScreen.setClientList();
                }                
                break;
            case 1:
                ClientScreen.updateMsgScreen("["+sender+"]: "+text+"\n");
                break;
            case 2:
                PrivateMessage pm=(PrivateMessage)msg;
                ClientScreen.updateMsgScreen("Private Msg From["+sender+"]:"+msg);
                break;
            case 3:
                CodebookMessage cm=(CodebookMessage)msg;
                CodeBookManager cbm=new CodeBookManager();
                CodeBook cb=new CodeBook();
                cb.setContent(cm.getCodebook().getMap());
                cbm.setCodeBook(cb);                
                System.out.println("Get Codebook");
                ClientScreen.updateCodebookArea();
                System.out.println(cbm.getCodeword("a"));
                break;                        
        } 
    }
}
