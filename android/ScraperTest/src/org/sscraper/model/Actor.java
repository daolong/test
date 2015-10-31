package org.sscraper.model;

import java.io.Serializable;


public class Actor implements Serializable {
    
   private String name;
   private String role;
   
   public Actor(String name, String role) {
       this.name = name;
       this.role = role;
   }
   
   public String getName() {
       return this.name;
   }
   
   public String getRole() {
       return this.role;
   }
   
   public String toString() {
       return "Actor : name(" + this.name + "), role(" + this.role + ")";
   }
}
