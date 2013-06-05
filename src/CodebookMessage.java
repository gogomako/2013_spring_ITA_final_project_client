/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ashley
 */
public class CodebookMessage extends Message{
   
    CodeBook book;
    CodebookMessage(){super.setType(3);} //0=> system msg, 1=>public msg, 2=>private msg, 3=>codebook Msg
    CodebookMessage(CodeBook b){
        super.setType(3);
        book=b;
    }
    
    public void setCodebook(CodeBook b){
        book=b;
    }
    
    public CodeBook getCodebook(){
        return book;
    }
}
