
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
public class ClientListener extends Thread {

    ServerSocket listenSocket;
    Socket clientReceiveSocket;
    ObjectInputStream ois;
    Message msg;
    HuffmanDecoder decoder;
    HuffmanEncoder encoder;
    ClientList clientList;
    AutoReplyer replyer;
    int port;

    ClientListener() {
        decoder = new HuffmanDecoder();
    }

    ClientListener(int _port) {
        port = _port;
        decoder = new HuffmanDecoder();
    }

    public void run() {
        try {
            System.out.println("Client listening to server on port " + port + "\n");
            listenSocket = new ServerSocket(port);
            while (true) {
                clientReceiveSocket = listenSocket.accept();
                System.out.println("getMsg");
                ois = new ObjectInputStream(clientReceiveSocket.getInputStream());
                msg = (Message) ois.readObject();
                this.checkMsgFromServer(msg);
                ois.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkMsgFromServer(Message msg) {
        int type = msg.getType();
        String text = msg.getMsg();
        String sender = msg.getNickName();
        int mode=ClientScreen.mode;        
        switch (type) {
            case 0:
                SystemMessage sm = (SystemMessage) msg;
                text = sm.getSystemMsg();
                System.out.println("get a system msg:" + text);
                if (sender.equals("System Msg:")) {
                    ClientScreen.updateMsgScreen("<font color=blue size=4>===System Anoncement:" + text + "===</font><br>");
                } else if (sender.equals("Client List:")) {
                    System.out.println("get client list");
                    System.out.println(text);
                    String[] list = text.split(",");
                    clientList = new ClientList(list);
                    System.out.println(clientList.getListSize());
                    if (loginScreen.login) {
                        ClientScreen.setClientList();
                    }
                }
                break;
            case 1:
                //text=decoder.decode(text);  
                String sendingTo=ClientScreen.getReceiver();
                ClientScreen.updateMsgScreen("[" + sender + "]: " + text + "<br>");
                if(mode==2&&(!sender.equals(loginScreen.userName))){
                    if(sendingTo.equals("Not Selected"))this.sendAutoReply(mode);
                    else this.sendAutoReply(mode, sendingTo);
                }
                break;
            case 2:
                //text=decoder.decode(text);
                PrivateMessage pm = (PrivateMessage) msg;
                ClientScreen.updateMsgScreen("<font color=red size=4>[" + sender + "]: " + text + "</font><br>");
//                String msgderiect="";
//                if(sender.equals(loginScreen.userName))msgderiect="To ["+pm.getReceiver()+"]: ";
//                else msgderiect="From ["+pm.getNickName()+"]: ";
//                ClientScreen.updateMsgScreen("Private Msg "+msgderiect+text+"\n");
                break;
            case 3:
                CodebookMessage cm = (CodebookMessage) msg;
                CodeBookManager cbm = new CodeBookManager();
                CodeBook cb = new CodeBook();
                cb.setEncodeMap(cm.getCodebook().getEncodeMap());
                cb.setDecodeMap(cm.getCodebook().getDecodeMap());
                cbm.setCodeBook(cb);
                System.out.println("Get Codebook");
                ClientScreen.updateCodebookArea();
                ClientScreen.updateRateLabel();
                System.out.println(cbm.getCodeword("a"));
                break;
        }
    }

    public void sendAutoReply(int mode) {
        replyer = new AutoReplyer();
        if (mode == 2) {
            String msg = replyer.getMode2Reply();
            MsgSender sender = new MsgSender();
            sender.send(1, loginScreen.userName, msg);
        }
    }

    public void sendAutoReply(int mode, String receiver) {
        replyer = new AutoReplyer();
        if (mode == 2) {
            encoder=new HuffmanEncoder();
            String msg = replyer.getMode2Reply();
            MsgSender sender = new MsgSender();
            PrivateMessage pm=new PrivateMessage(loginScreen.userName,receiver,encoder.encode(msg));
            sender.send(pm);
        }
    }
}
