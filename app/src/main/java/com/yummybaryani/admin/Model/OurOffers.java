package com.yummybaryani.admin.Model;

import java.util.ArrayList;

public class OurOffers {
    private String name;
    private String image;
    private String description;
    private ArrayList<Price_> price;
    private String discount;
    private String deliveryTime;
    private ArrayList<Sides> sides;
    private Boolean isEnabled;


    public OurOffers(){}


    public OurOffers(String name, String description, ArrayList<Price_> price, String discount, String image, String deliveryTime, ArrayList<Sides> sides,Boolean isEnabled) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.deliveryTime = deliveryTime;
        this.sides = sides;
        this.isEnabled=isEnabled;
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


    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setSides(ArrayList<Sides> sides) {
        this.sides = sides;
    }

    public ArrayList<Sides> getSides() {
        return sides;
    }

}
