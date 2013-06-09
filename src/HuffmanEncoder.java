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
    CodeBook codebook;
    
    HuffmanEncoder(){
        //default constructor
        CBManager = new CodeBookManager();       

    }
    
    public String encode(String inputString){        
        String outputString = "";       
        for(int i=0 ; i<inputString.length() ; i++){ 
            Character c=new Character(inputString.charAt(i));
            String word=c.toString();          
            //System.out.println("word="+word);
            if(CBManager.containsCodeword(word)){      
                System.out.println("yes");
                outputString = outputString + CBManager.getCodeword(word);//encode
            }else{
                //TODO error handling
            }
        }
        return outputString;
    }    
}
