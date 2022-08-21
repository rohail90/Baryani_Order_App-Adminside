package com.yummybaryani.admin.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Food implements Serializable {
    private String name;
    private String image;
    private String description;
    private ArrayList<Price_> price;
    private String discount;
    private String menuId;
    private String deliveryTime;


    public Food(){}


    public Food(String name, String description, ArrayList<Price_> price, String discount, String menuId, String image, String deliveryTime) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.menuId = menuId;
        this.deliveryTime = deliveryTime;
    }

    // Getter Methods
    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }
    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Price_> getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public String getMenuId() {
        return menuId;
    }

    // Setter Methods

    public void setName(String Name) {
        this.name = Name;
    }

    public void setImage(String Image) {
        this.image = Image;
    }

    public void setDescription(String Description) {
        this.description = Description;
    }

    public void setPrice(ArrayList<Price_> Price) {
        this.price = Price;
    }

    public void setDiscount(String Discount) {
        this.discount = Discount;
    }

    public void setMenuId(String MenuId) {
        this.menuId = MenuId;
    }
}
