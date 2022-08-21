package com.yummybaryani.admin.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yummybaryani.admin.Common.Common;
import com.yummybaryani.admin.Interface.ItemClickListener;
import com.yummybaryani.admin.Model.ColdDrink;
import com.yummybaryani.admin.R;
import com.yummybaryani.admin.Server.AddColdrinksActivity;
import com.yummybaryani.admin.Server.ViewHolders.FoodViewHolderServer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ColdDrinksFragmentServer extends Fragment {

    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference coldDrinks;

    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerViewDrinks;

    private Button saveBtn;
    com.yummybaryani.admin.CustomFont.NativelyCustomEditText pepsiHalfLtrPrice,pepsi1_5LtrPrice,pepsi2_15LtrPrice,drinkName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drinks_screen, container, false);
        //init firebase
        context=container.getContext();
        //init Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        coldDrinks = firebaseDatabase.getReference("ColdDrinks");



        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDrink();
            }
        });


        recyclerViewDrinks = view.findViewById(R.id.recycler_drink);
        recyclerViewDrinks.setHasFixedSize(true);
        recyclerViewDrinks.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        loadDrinks();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            updateDrink(adapter.getRef(item.getOrder()).getKey(), (ColdDrink) adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteDrink(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }


    //Helper Method
    private void loadDrinks() {
        FirebaseRecyclerOptions<ColdDrink> options = new FirebaseRecyclerOptions.Builder<ColdDrink>().
                setQuery(coldDrinks, ColdDrink.class).build();

        adapter = new FirebaseRecyclerAdapter<ColdDrink, FoodViewHolderServer>(options) {
            @NonNull
            @Override
            public FoodViewHolderServer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_item, parent, false);
                return new FoodViewHolderServer(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolderServer holder, int position, @NonNull final ColdDrink model) {

                com.yummybaryani.admin.CustomFont.NativelyCustomTextView pepsiHalfLtrPrice,pepsi1_5LtrPrice,pepsi2_15LtrPrice,drinkName;

                drinkName = holder.itemView.findViewById(R.id.coldDrinkName);
                pepsiHalfLtrPrice = holder.itemView.findViewById(R.id.colddrinkHalfLtrPrice);
                pepsi1_5LtrPrice = holder.itemView.findViewById(R.id.colddrink_1_5LtrPrice);
                pepsi2_15LtrPrice = holder.itemView.findViewById(R.id.colddrink_2_15LtrPrice);


                drinkName.setText(model.getName());
                pepsiHalfLtrPrice.setText("Rs "+model.getHalfLtrPrice());
                pepsi1_5LtrPrice.setText("Rs "+model.getColdDrink_1_5LtrPrice());
                pepsi2_15LtrPrice.setText("Rs "+model.getColdDrink_2_15LtrPrice());




                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //Sending food_id to FoodDetailActivity
//                        Intent intent = new Intent(FoodActivityServer.this, FoodDetailActivity.class);
//                        intent.putExtra("foodId", adapter.getRef(position).getKey());
//                        startActivity(intent);
                    }
                });
            }
        };

        recyclerViewDrinks.setAdapter(adapter);
    }


    private void addDrink() {
        Intent intent=new Intent(context, AddColdrinksActivity.class);

        startActivity(intent);

    }



    private void updateDrink(final String key, final ColdDrink item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Update Drink");
        alertDialog.setMessage("Please update information about drink");
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.cold_drink_item, null);
        alertDialog.setView(view);

        drinkName = view.findViewById(R.id.coldDrinkName);
        pepsiHalfLtrPrice = view.findViewById(R.id.colddrinkHalfLtrPrice);
        pepsi1_5LtrPrice =view.findViewById(R.id.colddrink_1_5LtrPrice);
        pepsi2_15LtrPrice = view.findViewById(R.id.colddrink_2_15LtrPrice);
        saveBtn = view.findViewById(R.id.saveBtn);

        drinkName.setText(item.getName());
        pepsiHalfLtrPrice.setText(item.getHalfLtrPrice());
        pepsi1_5LtrPrice.setText(item.getColdDrink_1_5LtrPrice());
        pepsi2_15LtrPrice.setText(item.getColdDrink_2_15LtrPrice());


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setName(drinkName.getText().toString());
                item.setHalfLtrPrice(pepsiHalfLtrPrice.getText().toString());
                item.setColdDrink_1_5LtrPrice(pepsi1_5LtrPrice.getText().toString());
                item.setColdDrink_2_15LtrPrice(pepsi2_15LtrPrice.getText().toString());
                coldDrinks.child(key).setValue(item);
                Toast.makeText(context, "Drink Updated", Toast.LENGTH_SHORT).show();

            }
        });
        //

        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }


    private void deleteDrink(String key) {
        coldDrinks.child(key).removeValue();
    }


}