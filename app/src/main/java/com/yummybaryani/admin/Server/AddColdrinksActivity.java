package com.yummybaryani.admin.Server;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yummybaryani.admin.Model.ColdDrink;
import com.yummybaryani.admin.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddColdrinksActivity extends Activity implements View.OnClickListener{

    com.yummybaryani.admin.CustomFont.NativelyCustomEditText pepsiHalfLtrPrice,pepsi1_5LtrPrice,pepsi2_15LtrPrice,drinkName;
    Button saveBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference coldDrinks;
    com.yummybaryani.admin.CustomFont.NativelyCustomTextView drinksTv;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.cold_drink_item);
            saveBtn=findViewById(R.id.saveBtn);
            saveBtn.setOnClickListener(this);
            drinksTv=findViewById(R.id.drinksTv);
            drinksTv.setVisibility(View.VISIBLE);
            firebaseDatabase = FirebaseDatabase.getInstance();
            coldDrinks = firebaseDatabase.getReference("ColdDrinks");

            drinkName=findViewById(R.id.coldDrinkName);

            pepsiHalfLtrPrice=findViewById(R.id.colddrinkHalfLtrPrice);
            pepsi1_5LtrPrice=findViewById(R.id.colddrink_1_5LtrPrice);
            pepsi2_15LtrPrice=findViewById(R.id.colddrink_2_15LtrPrice);
        }

        @Override
        public void onClick(View view) {
            if (view==saveBtn){
                if (!pepsiHalfLtrPrice.getText().toString().equals("") && !pepsi1_5LtrPrice.getText().toString().equals("") && !drinkName.getText().toString().equals("") && !pepsi2_15LtrPrice.getText().toString().equals("")){
                    ColdDrink coldDrink = new ColdDrink(drinkName.getText().toString(),pepsiHalfLtrPrice.getText().toString(),pepsi1_5LtrPrice.getText().toString(),pepsi2_15LtrPrice.getText().toString());
                    coldDrinks.push().setValue(coldDrink);
                    finish();
                    Snackbar.make(drinkName, "Drink added successfully", Snackbar.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Please provide all details", Toast.LENGTH_SHORT).show();

                }
            }

        }

}
