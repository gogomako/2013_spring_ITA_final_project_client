/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ashley
 */
public class PrivateMessage extends Message{
    //String sendNickName; //sender's nick name    
    String recNickName; //receiver's nick Name
    PrivateMessage(){super.setType(2);} //0=> system msg, 1=>public msg, 2=>private msg
    PrivateMessage(String sn,String rn,byte[] m){
        super.setType(2);
        super.setSenderNickName(sn);
        recNickName=rn;
        super.setMsg(m);
    }
    
    public void setReciverNickName(String n){
        recNickName=n;
    }    
    
    public String getReceiver(){
        return recNickName;
    }
    
}