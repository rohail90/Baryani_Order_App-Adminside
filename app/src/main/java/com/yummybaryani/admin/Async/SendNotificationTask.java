package com.yummybaryani.admin.Async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.yummybaryani.admin.Database.PrefManager;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendNotificationTask extends AsyncTask<String, Void, String> {
    static String response;
    Context context;
    String deviceId,title,message;
    public final static String AUTH_KEY_FCM = "AAAA1sqiFk0:APA91bFQ7x5egL-3LvbKCYnS5MkWFEZ1N3a7bepsTS1h1AgPL-Q55RA2Hi5Sf10DW9qdTQddWNeam4hgpPKe0_llYLoJ3q7C9EIDW5SyZfotr_rtfEIDCtimESYbTscJmF6cEA9ll-aZ";

    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    public SendNotificationTask(Context context,String userDeviceIdKey, String title, String message) {
        this.context = context;
      this.deviceId=userDeviceIdKey;
        Log.d("TOKEN", "SendNotificationTask: "+userDeviceIdKey);
      this.title=title;
      this.message=message;
    }

    @Override
    protected void onPreExecute() {
        Log.d("TAG", "onPreExecute: ");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String address=null;
        try {
            String authKey = AUTH_KEY_FCM;   // You FCM AUTH key
            String FMCurl = API_URL_FCM;

            URL url;
            url = new URL(FMCurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + authKey);
            conn.setRequestProperty("Content-Type", "application/json");

            Log.d("TOKEN", "deviceId: "+deviceId+"   token:"+PrefManager.getToken());
            JSONObject json = new JSONObject();
            json.put("to", deviceId);
            JSONObject info = new JSONObject();
            info.put("title", title); // Notification title
            info.put("text", message); // Notification body
            json.put("notification", info);
            System.out.println(json.toString());

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();
        } catch (Exception e) {
            Log.d("TAG", "Exception in pushFCMNotification: " + e.getMessage());
        }

        return address;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s!=null){

        }else {
            Log.d("TTAG", " In   ..... onPostExecute: ");
        }
    }
}
