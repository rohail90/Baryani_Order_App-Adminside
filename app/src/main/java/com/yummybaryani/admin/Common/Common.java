package com.yummybaryani.admin.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yummybaryani.admin.Model.Request;
import com.yummybaryani.admin.Model.User;

import java.util.Calendar;
import java.util.Locale;

public class Common {
    public static User currentUser;
    public static Request currentRequest;
    public final static String UPDATE = "Update";
    public final static String DELETE = "Delete";
    public final static String USER_PHONE = "UserPhone";
    public final static String USER_ADDRESS = "UserAddress";
    public final static String USER_PASSWORD = "UserPassword";
    public final static String USER_NAME = "UserName";
    public final static String CLIENT = "client";
    public final static String SERVER = "server";
    public final static String ADMIN = "ADMIN";
    public final static String ACCEPT_TITLE = "Order accepted";
    public final static String Complete_TITLE = "Order Delivered";
    public final static String Complete_MSG = "Your Order has been delivered.";
    public final static String CANCEL_TITLE = "Order canceled";
    public final static String CANCEL_MSG = "Sorry,Your Order has been cancelled.";
    public final static String ACCEPTD_MSG = "Your order has been accepted , and now in shipping state you will get it soon.Thank you to use our service.";
    public final static String CANCELLED_BY_ADMIN = "Cancelled  By Admin";

    public final static String FullBiryani = "Half Baryani";
    public final static String HalfBiryani = "Full Baryani";
    public final static String MediumBiryani = "Medium Baryani";

    public final static String Chatni = "Chatni";
    public final static String Salad = "Salad";
    public final static String ShamiTikki = "Shami Tikki";







    public static String getStatus(String status) {
        switch (status) {
            case "0":
                return "Placed";
            case "1":
                return "Shipping";
            case "2":
                return "Shipped";
            case "3":
                return "Cancelled";
            default:
                return "Status Not Available";
        }
    }


    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isConnectedToInternet(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null){

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null){

                for(int i=0; i<info.length; i++){
                    if(info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
    public static String getDate(long time)
    {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date = new StringBuilder(android.text.format.DateFormat.format("dd-MM-yyyy HH:mm"
                , calendar).toString());
        return date.toString();
    }
}
