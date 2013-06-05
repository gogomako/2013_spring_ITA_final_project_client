/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ashley
 */
public class Message implements java.io.Serializable{

    int type; //0=>system msg,1=>public msg, 2=>private msg, 3=>codebook Msg
    String sendNickName; //senderNickName   
    String msg;

    Message() {
    } 

    Message(int t, String n, String m) {
        type = t;
        sendNickName = n;
        msg = m;
    }

    public void setType(int t) {
        type = t;
    }

    public void setMsg(String s) {
        msg = s;
    }

    public void setSenderNickName(String n) {
        sendNickName = n;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public String getNickName() {
        return sendNickName;
    }
}
