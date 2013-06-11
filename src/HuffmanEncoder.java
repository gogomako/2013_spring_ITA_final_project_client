/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CBB
 */
public class HuffmanEncoder {
    CodeBookManager CBManager;
    
    HuffmanEncoder(){
        //default constructor
        CBManager = new CodeBookManager();
        //CBManager.initCodebook();        
    }
    
    public byte[] encode(String inputString){        
        return toBinary(getEncodeString(inputString));
    }    
    
    public String getEncodeString(String inputString){
        String outputString = "";       
        for(int i=0 ; i<inputString.length() ; i++){ 
            Character c=new Character(inputString.charAt(i));
            String word=c.toString();          
            System.out.println("word="+word);
            if(CBManager.containsCodeword(word)){      
                System.out.println("yes");
                outputString = outputString + CBManager.getCodeword(word);//encode
            }else{
                //TODO error handling
            }
        }
        System.out.println("return "+outputString);
        return outputString+CBManager.getCodeword("end");
    }
    
    public byte[] toBinary(String s) {           
        int sLen = s.length();                         //get bits number
        byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];  //compute byte number
        char c;
        for (int i = 0; i < sLen; i++) {
            if ((c = s.charAt(i)) == '1') {
                // byte[i/8] OR 10000000 >>> i%8
                toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));  
            } else if (c != '0') {
                throw new IllegalArgumentException();
            }
        }
        return toReturn;
    }
}
