
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
    String ip;
    int port;

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
}
