
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ashley
 */
public class CodeBookManager {
    static CodeBook codebook;
    
    CodeBookManager(){codebook=new CodeBook();}
    
    
    public void initCodebook(String sender,MsgSender ms) { //request codebook
        Message m=new Message(0,sender,"request for codebook");
        ms.send(m);
    }
    
    public void setCodeBook(CodeBook book){
        codebook=book;
    }
    
    public String getCodeword(String s){  //get codeword
        String keyValue=s;
        if(s.equals(" "))keyValue="space";
        return codebook.get(keyValue);
    }
    
    public CodeBook getContent(){ //get codebook hashmap
        return codebook;
    }          
    
}
