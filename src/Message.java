/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ashley
 */
public class Message implements java.io.Serializable{

    int type; //0=> system msg, 1=>public msg, 2=>private msg, 3=>codebook Msg
    String sendNickName; //senderNickName   
    byte[] msg;
    HuffmanDecoder decoder;

    Message() {
    } 

    Message(int t, String n, byte[] m) {
        type = t;
        sendNickName = n;
        msg = m;
    }

    public void setType(int t) {
        type = t;
    }

    public void setMsg(byte[] s) {
        msg = s;
    }

    public void setSenderNickName(String n) {
        sendNickName = n;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        if(msg==null)return null;
        decoder=new HuffmanDecoder();        
        return decoder.decode(msg);
    }

    public String getNickName() {
        return sendNickName;
    }
}
