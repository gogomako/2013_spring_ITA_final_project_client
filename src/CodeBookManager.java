
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
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
public class CodeBookManager implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    static CodeBook codebook;

    CodeBookManager() {
        //codebook = new CodeBook();
    }

    CodeBookManager(CodeBook c) {
        codebook = c;
    }

    public void initCodebook(String sender, MsgSender ms) { //request codebook
        System.out.println("init codebook sender:" + sender);
        ms.sendSystemMsg(sender, "request for codebook");
    }

    public void setCodeBook(CodeBook book) {
        codebook = book;        
    }

    public String getCodeword(String s) {  //get codeword
        String keyValue = s;
        return codebook.getCode(keyValue);
    }

    public CodeBook getContent() { //get codebook hashmap
        return codebook;
    }

    public boolean containsCodeword(String word) {
        String keyValues = word;
        if (codebook.encodeCodebookContainsKey(keyValues)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean containsWord(String codeword) {
        if (codebook.decodeCodebookContainsKey(codeword)) {
            return true;
        } else {
            return false;
        }
    }

    public String getWord(String s) {
        return codebook.getWord(s);
    }
    
    public double getCompressRate(){        
        double original= Byte.SIZE*codebook.getEncodeMap().size()*2;
        double newSize=0;        
        for (Map.Entry<String, String> entry : codebook.getEncodeMap().entrySet()) {
            newSize+=entry.getValue().length();
        }        
        System.out.println("ori="+original+" new="+newSize);
        return newSize/original;
    }
    
    public double getCompressRate(String s,String code){        
        double original= Byte.SIZE*s.length()*2;
        double newSize=code.length();   
        System.out.println("ori="+original+" new="+newSize);
        return newSize/original;
    }
}
