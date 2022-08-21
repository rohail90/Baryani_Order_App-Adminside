//package com.example.myfoodorderapp.Async;
//
//import android.app.Activity;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.AsyncTask;
//import android.util.Log;
//
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class GetAddressTask extends AsyncTask<String, Void, String> {
//    static String response;
//    Activity context;
//    double lat,lng;
//   int whichAddress;
//    public GetAddressTask(Activity context, double lat, double lng) {
//        this.context = context;
//        this.lat = lat;
//        this.lng=lng;
//        this.whichAddress=whichAddress;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        Log.i("Address", "ADDRESS: ");
//        ChatActivityUser.progressDialog.show();
//        super.onPreExecute();
//    }
//
//    @Override
//    protected String doInBackground(String... strings) {
//        String address=null;
//        try{
//            Geocoder geocoder;
//            List<Address> addresses;
//            geocoder = new Geocoder(context, Locale.getDefault());
//
//            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//
//            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            Log.d("ADDRESS", "ADDRESS: "+address);
//
//
//            // ((FinalIncidentReport)context).stopLocationUpdates();
//        }catch (IOException e){
//
//        }
//        return address;
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        ChatActivityUser.progressDialog.dismiss();
//
//        if (s!=null){
//            ChatActivityUser.userAddressUsingCurrentLocation=s;
//        }else {
//            Log.i("TTAG", " In get AddressTask ..... onPostExecute: ");
//        }
//    }
//}
