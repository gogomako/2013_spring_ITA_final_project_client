
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
public class HuffmanDecoder implements java.io.Serializable{

    CodeBookManager CBManager;
    CodeBook codebook;

    HuffmanDecoder() {
        //default constructor
        CBManager = new CodeBookManager();
    }

    public String decode(byte[] bytes) {   
        System.out.println(CBManager.getWord("100110"));
        String inputString=this.toBinary(bytes);
        System.out.println("decode " + inputString);
        String tmp = "";
        String rlt = "";
        int index = 0;
        while (index < inputString.length()) {
            Character c = inputString.charAt(index);
            String word = c.toString();
            tmp += word;
            System.out.println("index="+index+" tmp="+tmp);
            if (CBManager.containsWord(tmp)) {                
                System.out.println(tmp + " contain to be " + CBManager.getWord(tmp));
                String w=CBManager.getWord(tmp);
                System.out.println(tmp + " contain to be " + w);
                if(w.equals("end"))return rlt;
                rlt += CBManager.getWord(tmp);
                tmp = "";
            }
            index++;
        }
        System.out.println("decode to "+rlt);
        return rlt;
    }

    public String toBinary(byte[] bytes) {
        System.out.println(bytes.length);
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE); //equals to bits number
        for (int i = 0; i < Byte.SIZE * bytes.length; i++) {     //run bits number
            //make byte move left bit by bit to AND 10000000, if equals 0 then the first bit is 1
            sb.append((bytes[i / Byte.SIZE] << (i % Byte.SIZE) & 0x80) == 0 ? '0' : '1');
        }
        return sb.toString();
    }
}
