
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
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
public class MsgSender{

    Socket clientSocket;
    ObjectOutputStream oos;
    static String ip;
    static int port;
    HuffmanEncoder encoder;
    

    MsgSender() {
    }

    MsgSender(String _ip, int _port) {
        ip=_ip;
        port = _port;
    }

    public void send(Message msg) {        
        try {
            clientSocket = new Socket(ip, port);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(msg);
            oos.close();
            System.out.println("Send a msg"+msg.getMsg()+" to server port "+port+"\n");
        } catch (Exception ex) {
            Logger.getLogger(MsgSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void send(int type,String sender,String s){
        encoder=new HuffmanEncoder();
        byte[] m=encoder.encode(s);
        System.out.println("send"+s+" to"+m.length);
        Message msg=new Message(type,sender,m);
        this.send(msg);
    }
    
    public void sendSystemMsg(String sender,String msg){
        SystemMessage sm=new SystemMessage(sender,msg);
        this.send(sm);
    }
}
