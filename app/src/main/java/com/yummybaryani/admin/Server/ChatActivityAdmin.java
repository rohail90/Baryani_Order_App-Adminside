package com.yummybaryani.admin.Server;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yummybaryani.admin.Adapter.AdminMessageAdapter;
import com.yummybaryani.admin.Common.Common;
import com.yummybaryani.admin.Database.PrefManager;
import com.yummybaryani.admin.Model.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.yummybaryani.admin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivityAdmin extends AppCompatActivity implements View.OnClickListener {

    CircleImageView profile_image;
    TextView user_name;

    DatabaseReference reference;

    Intent intent;

    ImageButton button_send;
    EditText text_send;

    AdminMessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;
    Context context;
    ImageView cancelBtn;
    TextView senderName;
    PrefManager session;
    String userPhone="",userName="",order_ID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        senderName=findViewById(R.id.senderName);
        context=this;
        session=new PrefManager(context);
        if (getIntent() != null) {

            userPhone = getIntent().getStringExtra("userPhone_");
            userName = getIntent().getStringExtra("userName_");
            order_ID = getIntent().getStringExtra("order_Id");
        }

        try {
          getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        }catch (Exception e){
            Log.d("EXC", "onCreateView: of ExpertChatFragment");
        }

        cancelBtn=findViewById(R.id.cancel);
        if (userName!=null){
            senderName.setText(userName);
        }else{
            senderName.setText("User");
        }
        if (!Common.isInternetAvailable(context)){
            Toast.makeText(context, "Internet Not Available!", Toast.LENGTH_SHORT).show();
        }

        cancelBtn.setOnClickListener(this);
        if (userPhone!=null && userPhone!=null){

            reference = FirebaseDatabase.getInstance().getReference("chats/"+userPhone+order_ID+Common.ADMIN);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    readMessage(userPhone,Common.ADMIN,"");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            readMessageeeeee(userPhone,Common.ADMIN,"");
        }

        button_send = (ImageButton) findViewById(R.id.btn_send_message);
        text_send = (EditText) findViewById(R.id.et_sendMessage);
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(text_send, 0);
        }catch (Exception e){
            Log.d("EXC", " exception onCreateView: of ExpertChat "+e.toString());
        }


        text_send.setScroller(new Scroller(context));
        text_send.setMaxLines(2);
        text_send.setVerticalScrollBarEnabled(true);
        text_send.setMovementMethod(new ScrollingMovementMethod());


        // intent = context.getIntent();
        //final String userId = intent.getStringExtra("userid");

        Log.i("USER", "onClick: user id :"+userPhone);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(userPhone,Common.ADMIN, msg);
                } else {
                    Toast.makeText(context, "You Can't Send Empty Message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        recyclerView = findViewById(R.id.recycler_view_msg);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onClick(View v) {
        if (v==cancelBtn){


        }


    }
    public static void hideKeyboard(Context mContext) {
        try {
            InputMethodManager imm = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(((Activity) mContext).getWindow()
                    .getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            Log.d("Cancel","Exception :"+e.getMessage());
        }

    }
    private void sendMessage(String userPhone_, String admin_, String message) {
        Log.d("USER","Receiver buyer :"+admin_+ " sender :  "+userPhone_ +"Job    " );
        if (Common.isInternetAvailable(context)){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", admin_);
            hashMap.put("receiver", userPhone_);
            hashMap.put("message", message);
            if (reference!=null){
                reference.push().setValue(hashMap);
            }else {
                Toast.makeText(context, "Message not sent!", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(context, "Internet Not Available!", Toast.LENGTH_SHORT).show();
        }

    }

    private void readMessage(final String admin_, final String userPhone_, final String imageurl) {
        mchat = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //  int count=0;
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    Log.i("TAG", "onDataChange: " +userPhone_ +" "+admin_+" "+chat.getReceiver()+" "+chat.getSender()+" "+chat.getMessage());
                    if ((chat.getReceiver().equals(admin_) && chat.getSender().equals(userPhone_)) || (chat.getReceiver().equals(userPhone_) && chat.getSender().equals(admin_))) {
                        mchat.add(chat);
                    }
                    /*if ((chat.getReceiver().equals(expertID) && chat.getSender().equals(buyerID))) {
                        count+=1;

                    }*/

                    messageAdapter = new AdminMessageAdapter(context, mchat);
                    recyclerView.setAdapter(messageAdapter);
                }
                // Log.d("COUNT", "onDataChange: "+count);
                // session.setMessageCount(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void readMessageeeeee(final String userPhone_, final String admin_, final String imageurl) {
        mchat = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int count=0;
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    Log.i("TAG", "onDataChange: " +admin_ +" "+userPhone_+" "+chat.getReceiver()+" "+chat.getSender()+" "+chat.getMessage());
                    if ((chat.getReceiver().equals(userPhone_) && chat.getSender().equals(admin_)) || (chat.getReceiver().equals(admin_) && chat.getSender().equals(userPhone_))) {
                        mchat.add(chat);
                    }
                    if ((chat.getReceiver().equals(userPhone_) && chat.getSender().equals(admin_))) {
                        count+=1;

                    }

                    messageAdapter = new AdminMessageAdapter(context, mchat);
                    recyclerView.setAdapter(messageAdapter);
                }
                Log.d("COUNT", "onDataChange: "+count);
                session.setMessageCount(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}