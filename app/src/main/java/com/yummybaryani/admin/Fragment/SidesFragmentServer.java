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
import com.yummybaryani.admin.Model.Sides;
import com.yummybaryani.admin.R;
import com.yummybaryani.admin.Server.AddSidesActivity;
import com.yummybaryani.admin.Server.ViewHolders.FoodViewHolderServer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SidesFragmentServer extends Fragment {

    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference sides;

    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerViewDrinks;

    private Button saveBtn;
    com.yummybaryani.admin.CustomFont.NativelyCustomEditText sidesPrice,sidesName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drinks_screen, container, false);
        //init firebase
        context=container.getContext();
        //init Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        sides = firebaseDatabase.getReference("Sides");



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

        loadSides();

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
            updateDrink(adapter.getRef(item.getOrder()).getKey(), (Sides) adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteDrink(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }


    //Helper Method
    private void loadSides() {
        FirebaseRecyclerOptions<Sides> options = new FirebaseRecyclerOptions.Builder<Sides>().
                setQuery(sides, Sides.class).build();

        adapter = new FirebaseRecyclerAdapter<Sides, FoodViewHolderServer>(options) {
            @NonNull
            @Override
            public FoodViewHolderServer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sides_item, parent, false);
                return new FoodViewHolderServer(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolderServer holder, int position, @NonNull final Sides model) {

                com.yummybaryani.admin.CustomFont.NativelyCustomTextView pepsiHalfLtrPrice,drinkName;

                drinkName = holder.itemView.findViewById(R.id.sideName);
                pepsiHalfLtrPrice = holder.itemView.findViewById(R.id.sidePrice);

                drinkName.setText(model.getName());
                pepsiHalfLtrPrice.setText("Rs "+model.getPrice());





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
        Intent intent=new Intent(context, AddSidesActivity.class);

        startActivity(intent);

    }



    private void updateDrink(final String key, final Sides item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Update Side");
        alertDialog.setMessage("Please update information about Side");
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.add_side_item, null);
        alertDialog.setView(view);

        sidesName =view.findViewById(R.id.sideName);
        sidesPrice =view.findViewById(R.id.sidePrice);

        saveBtn = view.findViewById(R.id.saveBtn);

        sidesName.setText(item.getName());
        sidesPrice.setText(item.getPrice());


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setName(sidesName.getText().toString());
                item.setPrice(sidesPrice.getText().toString());
                sides.child(key).setValue(item);
                Toast.makeText(context, "Side Updated", Toast.LENGTH_SHORT).show();

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
        sides.child(key).removeValue();
    }


}