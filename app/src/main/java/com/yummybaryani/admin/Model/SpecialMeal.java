package com.yummybaryani.admin.Model;

public class SpecialMeal {
    private String name;
    private String image;
    private String description;
    private String price;
    private Boolean isEnabled;


    public SpecialMeal(){}

    public SpecialMeal(String name, String description, String price, Boolean isEnabled, String image) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.isEnabled = isEnabled;
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

    public String getPrice() {
        return price;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public void setImage(String Image) {
        this.image = Image;
    }

    public void setDescription(String Description) {
        this.description = Description;
    }

    public void setPrice(String Price) {
        this.price = Price;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

}
