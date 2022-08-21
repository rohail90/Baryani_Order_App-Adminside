package com.yummybaryani.admin.Firebase;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.yummybaryani.admin.Database.PrefManager;
import com.yummybaryani.admin.Model.Token;
import com.yummybaryani.admin.R;
import com.yummybaryani.admin.Server.HomeActivityServer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Intent intent;
    PrefManager prefManager;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        prefManager=new PrefManager(getApplicationContext());
        Log.d("TAGG", " MyFirebaseMessagingService   Token is:" + s);
        if (s==null || s.equals("")){
            Log.d("TOKEN", " MyFirebaseMessagingService   Token is:" + s);


        }else {
            PrefManager.setToken(s);
            updateToken(s);
            Log.d("TOKEN", " MyFirebaseMessagingService   Token is:" + prefManager.getToken());
        }



    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("TAG", remoteMessage.getNotification().getTitle());
        Log.d("TAG", remoteMessage.getNotification().getBody());
        Log.d("TAG", remoteMessage.getData().toString());
        Log.i("DATA", remoteMessage.getData().toString());
        // get a list of running processes and iterate through them

        intent = new Intent("com.example.myfoodorderapp");
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        //NotificationData notificationData = new NotificationData();
        if (remoteMessage.getData() != null) {
            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject dataObject = new JSONObject(params);
                JSONObject object = new JSONObject(dataObject.getString("data"));

               /* intent.putExtra("notificationObject",object.toString());
                sendBroadcast(intent);*/

                Log.i("TAG", "onMessageReceived: " + object.getString("notificationType"));
                Log.i("TAG", "onMessageReceived: data is:..."+object.toString());



                //rest of the code
            } catch (Exception e) {

                Log.d("EXC", "Exception :onMessageReceived, exception is :" + e.toString());
            }
        } else {
            Log.i("TAG", "onMessageReceived: data is null");
        }

    }
      public void showNotification(String title, String message) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Start without a delay
// Each element then alternates between vibrate, sleep, vibrate, sleep...
        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};

// The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
        v.vibrate(pattern, -1);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final Intent intent = new Intent(this, HomeActivityServer.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotification")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(contentIntent)
                .setContentText(message);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, builder.build());
    }
    private void updateToken(String token) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token, false);
        // false because token send from client app

//        if (Common.currentUser.getPhone()!=null)
//        tokens.child(Common.currentUser.getPhone()).setValue(data);
    }
  /*  public void startAlert(){
        int i =60*60;
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);
        Log.i("onMessage", "Alarm set in " + i + " seconds");
       // Toast.makeText(this, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();
    }*/
}
