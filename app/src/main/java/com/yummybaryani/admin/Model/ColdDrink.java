package com.yummybaryani.admin.Model;

import java.io.Serializable;

public class ColdDrink implements Serializable {
    private String name;
    private String  halfLtrPrice;
    private String  coldDrink_1_5LtrPrice;
    private String  coldDrink_2_15LtrPrice;

    public ColdDrink(){}

    public ColdDrink(String name, String  halfLtrPrice, String  coldDrink_1_5LtrPrice, String  coldDrink_2_15LtrPrice) {
        this.name = name;
        this.halfLtrPrice=halfLtrPrice;
        this.coldDrink_1_5LtrPrice=coldDrink_1_5LtrPrice;
        this.coldDrink_2_15LtrPrice=coldDrink_2_15LtrPrice;

    }
    public String getHalfLtrPrice() {
        return halfLtrPrice;
    }

    public void setHalfLtrPrice(String halfLtrPrice) {
        this.halfLtrPrice = halfLtrPrice;
    }

    public void setColdDrink_1_5LtrPrice(String coldDrink_1_5LtrPrice) {
        this.coldDrink_1_5LtrPrice = coldDrink_1_5LtrPrice;
    }

    public void setColdDrink_2_15LtrPrice(String coldDrink_2_15LtrPrice) {
        this.coldDrink_2_15LtrPrice = coldDrink_2_15LtrPrice;
    }

    public String getColdDrink_1_5LtrPrice() {
        return coldDrink_1_5LtrPrice;
    }

    public String getColdDrink_2_15LtrPrice() {
        return coldDrink_2_15LtrPrice;
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



}
