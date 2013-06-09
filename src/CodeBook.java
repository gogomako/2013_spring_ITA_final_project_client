
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
import java.util.Map.Entry;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ashley,Mako
 */
public class CodeBook implements java.io.Serializable {

    private HashMap<String, String> encodeCodebook; //store codeword im memery
    //ex.a->01010
    private HashMap<String, String> decodeCodebook; //ex.01010->a

    CodeBook() {
        encodeCodebook = new HashMap<String, String>();
        decodeCodebook = new HashMap<String, String>();
    }

    public void put(String key, String value) {
        encodeCodebook.put(key, value);
        decodeCodebook.put(value, key);
    }

    public void cleanCoodbook() {
        encodeCodebook = new HashMap<String, String>();
        decodeCodebook = new HashMap<String, String>();
    }

    public String getCode(String key) {
        return encodeCodebook.get(key);
    }

    public String getWord(String key) {
        return decodeCodebook.get(key);
    }

    public boolean encodeCodebookContainsKey(String key) {
        if (encodeCodebook.containsKey(key)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean decodeCodebookContainsKey(String key) {
        if (decodeCodebook.containsKey(key)) {
            return true;
        } else {
            return false;
        }
    }

    public int size() {
        return encodeCodebook.size();
    }

    public Set<Entry<String, String>> getEntrySet() {
        return encodeCodebook.entrySet();
    }

    public void setEncodeMap(HashMap<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            encodeCodebook.put(entry.getKey(), entry.getValue());
        }
    }

    public void setDecodeMap(HashMap<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            decodeCodebook.put(entry.getKey(), entry.getValue());
        }
    }

    public HashMap<String, String> getEncodeMap() {
        return encodeCodebook;
    }

    public HashMap<String, String> getDecodeMap() {
        return decodeCodebook;
    }
}
