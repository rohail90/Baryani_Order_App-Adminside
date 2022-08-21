package com.yummybaryani.admin.Model;

import java.io.Serializable;

public class Price_ implements Serializable {
    private String size;
    private String value;

    public Price_(){}

    public Price_(String size,String value) {
        this.size = size;
        this.value = value;
    }
    public String getSize() {
        return size;
    }

    public String getValue() {
        return value;
    }


    public void setSize(String size) {
        this.size = size;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
