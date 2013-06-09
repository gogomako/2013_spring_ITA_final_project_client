
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
public class CodeBookManager {

    static CodeBook codebook;

    CodeBookManager() {
        codebook = new CodeBook();
    }

    public void initCodebook(String sender, MsgSender ms) { //request codebook
        System.out.println("init codebook sender:"+sender);
        Message m = new Message(0, sender, "request for codebook");
        ms.send(m);
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
}
