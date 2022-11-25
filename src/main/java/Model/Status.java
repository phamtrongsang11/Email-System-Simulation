/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author phamt
 */
public class Status implements Serializable {
    private static final long serialVersionUID = 20210811010L;
    private int id;
    private String name;
    
    public Status(){
        
    }

    public Status(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Status(int id) {
        this.id = id;
    }
    
    public Status(String name) {
        this.name = name;
    }
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
