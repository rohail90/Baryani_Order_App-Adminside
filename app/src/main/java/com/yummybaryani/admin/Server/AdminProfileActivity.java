package com.yummybaryani.admin.Server;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yummybaryani.admin.Model.User;
import com.yummybaryani.admin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;

import io.paperdb.Paper;

import static com.yummybaryani.admin.Common.Common.SERVER;
import static com.yummybaryani.admin.Common.Common.USER_ADDRESS;
import static com.yummybaryani.admin.Common.Common.USER_NAME;
import static com.yummybaryani.admin.Common.Common.USER_PHONE;

public class AdminProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView userName, address, phone;
    Button updateUserNameBtn, updateAddressBtn;
    String phoneTxt, userNameTxt, addressTxt;
    de.hdodenhof.circleimageview.CircleImageView profilePhoto;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        firebaseDatabase = FirebaseDatabase.getInstance();
        table_user = firebaseDatabase.getReference("User");
        userName = findViewById(R.id.profile_name);
        address = findViewById(R.id.profile_address);
        phone = findViewById(R.id.profile_phone);
        updateUserNameBtn = findViewById(R.id.btn_updateUsername);
        updateAddressBtn = findViewById(R.id.btn_updateAddress);
        profilePhoto = findViewById(R.id.profile_picture);

        updateUserNameBtn.setOnClickListener(this);
        updateAddressBtn.setOnClickListener(this);

        phoneTxt = Paper.book(SERVER).read(USER_PHONE);
        userNameTxt = Paper.book(SERVER).read(USER_NAME);
        addressTxt = Paper.book(SERVER).read(USER_ADDRESS);

        Log.d("TAG", "admin profile: " + phone + "  " + userName + "  " + address);
        userName.setText(userNameTxt);
        phone.setText(phoneTxt);
        address.setText(addressTxt);


    }

    @Override
    public void onClick(View view) {
        if (view == updateAddressBtn) {
            updateAddressDialog();

        } else if (view == updateUserNameBtn) {
            updateUsernameDialog();
        }
    }

    public void updateUserName(final String userName) {
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Checking User avail
                if (dataSnapshot.child(phoneTxt).exists()) {
                    //Get User data

                    User user = dataSnapshot.child(phoneTxt).getValue(User.class);
                    Log.d("TAG", "onDataChange: User :" + user.getPassword() + "  " + user.getPhone());
                    assert user != null;
                    //if (user.getPassword().equals(oldPasswordEditText.getText().toString())) {
                    Paper.book(SERVER).write(USER_NAME, userName);
                    user.setName(userName);
                    table_user.child(phoneTxt).child("name").setValue(userName);
                    Log.d("TAG", "onDataChange: User name :" + userName);


                    Toast.makeText(AdminProfileActivity.this, "Username Changed!", Toast.LENGTH_SHORT).show();

                    finish();
                    /*} else {
                        Toast.makeText(ChangePasswordActivity.this, "You entered wrong password..", Toast.LENGTH_SHORT).show();
                    }*/
                } else {
                    Toast.makeText(AdminProfileActivity.this, "User does'nt exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void updateUserAddress(final String address) {
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Checking User avail
                if (dataSnapshot.child(phoneTxt).exists()) {
                    //Get User data

                    User user = dataSnapshot.child(phoneTxt).getValue(User.class);
                    Log.d("TAG", "onDataChange: User :" + user.getPassword() + "  " + user.getPhone());
                    assert user != null;
                    //if (user.getPassword().equals(oldPasswordEditText.getText().toString())) {
                    Paper.book(SERVER).write(USER_ADDRESS, address);
                    user.setAddress(address);
                    table_user.child(phoneTxt).child("address").setValue(address);
                    Log.d("TAG", "onDataChange: User address :" + address);


                    Toast.makeText(AdminProfileActivity.this, "Address Changed!", Toast.LENGTH_SHORT).show();

                    finish();
                    /*} else {
                        Toast.makeText(ChangePasswordActivity.this, "You entered wrong password..", Toast.LENGTH_SHORT).show();
                    }*/
                } else {
                    Toast.makeText(AdminProfileActivity.this, "User does'nt exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void updateUsernameDialog() {
        final View dialogView = View.inflate(AdminProfileActivity.this, R.layout.update_profile_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(true);
        alertDialog.setTitle("Update Username");
        alertDialog.setMessage("Update user username");
        final MaterialEditText userNAme = dialogView.findViewById(R.id.userName);
        android.widget.Button updateBtn = dialogView.findViewById(R.id.btnUpdate);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserName(userNAme.getText().toString());
                alertDialog.dismiss();

            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    private void updateAddressDialog() {
        final View dialogView = View.inflate(AdminProfileActivity.this, R.layout.update_profile_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(true);
        alertDialog.setTitle("Update Address");
        alertDialog.setMessage("Update user address");
        final MaterialEditText userNAme = dialogView.findViewById(R.id.userName);
        android.widget.Button updateBtn = dialogView.findViewById(R.id.btnUpdate);
        userNAme.setHint("Address");
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserAddress(userNAme.getText().toString());
                alertDialog.dismiss();

            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }
}