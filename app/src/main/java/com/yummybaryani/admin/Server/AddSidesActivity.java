package com.yummybaryani.admin.Server;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yummybaryani.admin.Model.Sides;
import com.yummybaryani.admin.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddSidesActivity extends Activity implements View.OnClickListener{

    com.yummybaryani.admin.CustomFont.NativelyCustomEditText  sidesPrice, sidesName;
    Button saveBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference sides;
    com.yummybaryani.admin.CustomFont.NativelyCustomTextView sidesTv;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_side_item);
            saveBtn=findViewById(R.id.saveBtn);
            saveBtn.setOnClickListener(this);
            sidesTv =findViewById(R.id.sidesTv);
            sidesTv.setVisibility(View.VISIBLE);
            firebaseDatabase = FirebaseDatabase.getInstance();
            sides = firebaseDatabase.getReference("Sides");

            sidesName =findViewById(R.id.sideName);
            sidesPrice =findViewById(R.id.sidePrice);


        }

        @Override
        public void onClick(View view) {
            if (view==saveBtn){
                if (!sidesName.getText().toString().equals("") && !sidesPrice.getText().toString().equals("")){
                    Sides sides_ = new Sides(sidesName.getText().toString(), sidesPrice.getText().toString());
                    this.sides.push().setValue(sides_);
                    finish();
                    Snackbar.make(sidesName, "New Side added successfully", Snackbar.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Please provide all details", Toast.LENGTH_SHORT).show();

                }
            }

        }

}
