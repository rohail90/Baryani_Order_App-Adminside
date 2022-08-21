package com.yummybaryani.admin.Model;

import java.io.Serializable;

public class Sides implements Serializable {
    private String name;
    private String price;

    public Sides(){}

    public Sides(String name, String price) {
        this.name = name;
        this.price = price;
    }
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
