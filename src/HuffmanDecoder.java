
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mako
 */
public class HuffmanDecoder {

    CodeBookManager CBManager;
    HuffmanDecoder() {        
        CBManager = new CodeBookManager();

    }
    public String decode(String inputString) {        
        String tmp = "";
        String rlt = "";
        int index = 0;
        while (index < inputString.length()) {
            Character c = inputString.charAt(index);
            String word = c.toString();
            tmp += word;            
            if (CBManager.containsWord(tmp)) {  
                System.out.println(tmp+" contain to be "+CBManager.getWord(tmp));
                rlt += CBManager.getWord(tmp);                
                tmp="";
            }
            index++;
        }
        return rlt;
    }
}
