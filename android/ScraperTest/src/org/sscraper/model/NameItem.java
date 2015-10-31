/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.model;

public class NameItem {

    private String name;

    public NameItem(String name) {
        this.name = name;
    }

    public NameItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return name;
    }
}
