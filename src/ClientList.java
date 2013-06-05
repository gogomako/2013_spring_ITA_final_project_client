
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ashley
 */
public class ClientList {
    String[] users;
    ClientList(){}
    public void setList(String[] list){
        users=list;
        System.out.println("client List been set");
    }
    
    public boolean checkIsNameInList(String name){
        System.out.println("check name:"+name);                
        for(int i=0;i<users.length;i++){            
            if(users[i].equals(name)) return true;           
        }      
        return false;
    }
    
    public String[] getList(){
        return users;
    }
    
    public int getListSize(){
        return users.length;
    }
}
