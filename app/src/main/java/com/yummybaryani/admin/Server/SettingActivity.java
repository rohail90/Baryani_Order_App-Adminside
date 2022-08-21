package com.yummybaryani.admin.Server;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.yummybaryani.admin.R;

import io.paperdb.Paper;

import static com.yummybaryani.admin.Common.Common.SERVER;
import static com.yummybaryani.admin.Common.Common.USER_NAME;
import static com.yummybaryani.admin.Common.Common.USER_PASSWORD;
import static com.yummybaryani.admin.Common.Common.USER_PHONE;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout changePassword, logoutLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        changePassword = findViewById(R.id.changePassword);
        changePassword.setOnClickListener(this);
        logoutLL = findViewById(R.id.logoutLL);
        logoutLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == changePassword) {
//            Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
//            startActivity(intent);
        } else if (view == logoutLL) {
            //clearing remember me data in PaperDb
            Paper.book(SERVER).delete(USER_PHONE);
            Paper.book(SERVER).delete(USER_PASSWORD);
            Paper.book(SERVER).delete(USER_NAME);

            Intent intent = new Intent(SettingActivity.this, MainActivityServer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}