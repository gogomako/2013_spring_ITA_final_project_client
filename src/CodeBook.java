
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileOutputStream;
import java.util.*;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ashley
 */
public class CodeBook implements java.io.Serializable{

    private HashMap<String, String> codebook; //store codeword im memery


    CodeBook() {
        codebook = new HashMap<String, String>();        
    }
    
    public void put(String key,String value){
        codebook.put(key, value);
    }
    
    public String get(String key){
        return codebook.get(key);
    }
    
    public int size(){
        return codebook.size();
    }
    
    public Set<Map.Entry<String,String>> getEntrySet(){
        return codebook.entrySet();
    }
    
    public void setContent(HashMap<String,String> map){
        for (Map.Entry<String, String> entry : map.entrySet()) {
            codebook.put(entry.getKey(), entry.getValue());
        }
    }   
    
    public HashMap<String,String> getMap(){
        return codebook;
    }
}