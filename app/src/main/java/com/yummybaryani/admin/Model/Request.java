package com.yummybaryani.admin.Model;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    private String Name;
    private String Phone;
    private String Address;
    private String Total;
    private String Status;
    private String latLng;
    private String userToken;
    private List<Order> orders;
    private String paymentMethod;
    private String cancelledBy;

    public Request(){}

    public void setUserToken(String userTokens) {
        this.userToken = userTokens;
    }

    public String getUserToken() {
        return userToken;
    }

    public Request(String name, String phone, String address, String total, String paymentMethod, String cancelledBy, String latLng, String token, List<Order> orders) {
        Name = name;
        Phone = phone;
        Address = address;
        Total = total;
        Status = "0";//0 is default, 1 is shipping, 2 is shipped
        this.paymentMethod = paymentMethod;
        this.latLng = latLng;
        this.orders = orders;
        this.cancelledBy=cancelledBy;
        this.userToken = token;

    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getLatLng() {
        return latLng;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
