package com.yummybaryani.admin.Server;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yummybaryani.admin.Common.Common;
import com.yummybaryani.admin.Interface.ItemClickListener;
import com.yummybaryani.admin.Model.Food;
import com.yummybaryani.admin.R;
import com.yummybaryani.admin.Server.ViewHolders.FoodViewHolderServer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.widget.Toast.LENGTH_SHORT;

public class FoodActivityServer extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 100;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference foods;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    FirebaseRecyclerAdapter adapter;
    FirebaseRecyclerAdapter searchAdapter;
    RecyclerView recyclerViewFood;
    SwipeRefreshLayout swipeRefreshLayout;
    String categoryId;

    List<String> suggested;
    MaterialSearchBar materialSearchBar;
    private Uri saveUri;
    com.yummybaryani.admin.CustomFont.NativelyCustomEditText foodName,foodShortDescription,foodDiscount,foodHalfPrice,foodFullPrice,
            foodMediumPrice,foodDeliveryTime;
    private Button btnUpload;
    private Button btnSelect;
    TextView productImage;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_server);

        coordinatorLayout = findViewById(R.id.parent);

        //init firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        foods = firebaseDatabase.getReference("Food");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        suggested = new ArrayList<>();
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setCardViewElevation(10);

        swipeRefreshLayout = findViewById(R.id.refreshFood);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFoodDialog();
            }
        });

        //get categoryId from intent passed from HomeActivity
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");

        recyclerViewFood = findViewById(R.id.recycler_food);
        recyclerViewFood.setHasFixedSize(true);
        recyclerViewFood.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        loadFoods();
        loadSuggestion();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(materialSearchBar.getText().equals(""))
                {
                    adapter.stopListening();
                    loadFoods();
                    loadSuggestion();
                    adapter.startListening();
                    swipeRefreshLayout.setRefreshing(false);
                }
                else
                {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        //Search bar logic
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> search = new ArrayList<>();
                for(String newSearch : suggested)
                {
                    if(newSearch.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                    {
                        search.add(newSearch);
                    }
                }
                materialSearchBar.setLastSuggestions(search);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new SimpleOnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recyclerViewFood.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                if(text.length() > 0)
                    filterResult(text);
                else {
                    Toast.makeText(FoodActivityServer.this, "Oops, you forgot to enter food", Toast.LENGTH_SHORT).show();
                    materialSearchBar.disableSearch();
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                super.onButtonClicked(buttonCode);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        if(searchAdapter != null)
            searchAdapter.stopListening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE)
        {
            if(data != null && data.getData() != null)
            {
                saveUri = data.getData();
                Toast.makeText(this, "Image selected", LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            updateFood(adapter.getRef(item.getOrder()).getKey(), (Food) adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteFood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }


    //Helper Method
    private void loadFoods() {
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().
                setQuery(foods.orderByChild("menuId").equalTo(categoryId), Food.class).build();

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolderServer>(options) {
            @NonNull
            @Override
            public FoodViewHolderServer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_server, parent, false);
                return new FoodViewHolderServer(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolderServer holder, int position, @NonNull final Food model) {
                TextView textViewName = holder.itemView.findViewById(R.id.food_name);
                ImageView imageView = holder.itemView.findViewById(R.id.food_image);

                textViewName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(imageView);

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

        recyclerViewFood.setAdapter(adapter);
    }


    private void loadSuggestion() {
        foods.orderByChild("menuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot suggestions : dataSnapshot.getChildren())
                {
                    Food item = suggestions.getValue(Food.class);
                    assert item != null;
                    suggested.add(item.getName());
                }
                materialSearchBar.setLastSuggestions(suggested);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void filterResult(CharSequence text) {
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().
                setQuery(foods.orderByChild("name").equalTo(text.toString()), Food.class).build();

        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolderServer>(options) {
            @NonNull
            @Override
            public FoodViewHolderServer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
                return new FoodViewHolderServer(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolderServer holder, int position, @NonNull final Food model) {
                TextView textViewName = holder.itemView.findViewById(R.id.food_name);
                ImageView imageView = holder.itemView.findViewById(R.id.food_image);

                textViewName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(imageView);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                    }
                });
            }
        };

        recyclerViewFood.setAdapter(searchAdapter);
        searchAdapter.startListening();
    }


    private void addFoodDialog() {
        Intent intent=new Intent(this,AddItemCategoryActivity.class);
        intent.putExtra("categoryId",categoryId);
        startActivity(intent);
       /* AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Add new Food");
        alertDialog.setMessage("Please provide information about food");
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.custom_add_food_dialog, null);
        alertDialog.setView(view);

        etName = view.findViewById(R.id.food_name);
        etDescription = view.findViewById(R.id.food_description);
        etPrice = view.findViewById(R.id.food_price);
        etDiscount = view.findViewById(R.id.food_discount);
        btnUpload = view.findViewById(R.id.btnUpload);
        btnSelect = view.findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });


        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                saveUri = null;
            }
        });

        alertDialog.show();*/
    }


    /*private void uploadImage() {
        if(saveUri != null && !etName.getText().toString().equals("") && !etDescription.getText().toString().equals("") &&
                !etPrice.getText().toString().equals("") && !etDiscount.getText().toString().equals("") && categoryId != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final String imageName = UUID.randomUUID().toString();
            final StorageReference imageStorage = storageReference.child("images/" + imageName);
            imageStorage.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            Food newFood = new Food(etName.getText().toString(),
                                    etDescription.getText().toString(),
                                    etPrice.getText().toString(),
                                    etDiscount.getText().toString(),
                                    categoryId,
                                    uri.toString());
                            foods.push().setValue(newFood);
                            Snackbar.make(coordinatorLayout, "Food added successfully", Snackbar.LENGTH_SHORT).show();
                            saveUri = null;
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = (int) (100.0 * (taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploading : " + progress);
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please provide all details", Toast.LENGTH_SHORT).show();
        }
    }*/


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }


    private void updateFood(final String key, final Food item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Update Food");
        alertDialog.setMessage("Please update information about food");
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.add_item_category_activity, null);
        alertDialog.setView(view);

        foodName = view.findViewById(R.id.food_name);
        foodShortDescription = view.findViewById(R.id.food_description);
        foodFullPrice = view.findViewById(R.id.full_biryaniPrice);
        foodMediumPrice = view.findViewById(R.id.medium_biryaniPrice);
        foodDeliveryTime=view.findViewById(R.id.food_deliveryTime);
        foodHalfPrice = view.findViewById(R.id.half_biryaniPrice);
        foodDiscount = view.findViewById(R.id.food_discount);
        btnUpload=view.findViewById(R.id.saveBtn);
        productImage=view.findViewById(R.id.upload_image);
        foodName.setText(item.getName());
        foodDiscount.setText(item.getDiscount());
        foodHalfPrice.setText(item.getPrice().get(0).getValue());
        foodMediumPrice.setText(item.getPrice().get(1).getValue());
        foodFullPrice.setText(item.getPrice().get(2).getValue());

        foodDeliveryTime.setText(item.getDeliveryTime());
        foodShortDescription.setText(item.getDescription());

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImage(key, item);
            }
        });


        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                saveUri = null;
            }
        });

        alertDialog.show();
    }


    private void updateImage(final String key, final Food item) {
        if(saveUri != null && !foodName.getText().toString().equals("") && !foodShortDescription.getText().toString().equals("") &&
                !foodFullPrice.getText().toString().equals("") && !foodMediumPrice.getText().toString().equals("") && !foodHalfPrice.getText().toString().equals("")
                && !foodDiscount.getText().toString().equals("")&& !foodDeliveryTime.getText().toString().equals("")&&  categoryId != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final String imageName = UUID.randomUUID().toString();
            final StorageReference imageStorage = storageReference.child("images/" + imageName);
            imageStorage.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            item.setName(foodName.getText().toString());
                            item.setDescription(foodShortDescription.getText().toString());

                            item.getPrice().get(0).setValue(foodHalfPrice.getText().toString());
                            item.getPrice().get(1).setValue(foodMediumPrice.getText().toString());
                            item.getPrice().get(2).setValue(foodFullPrice.getText().toString());


                            item.setDiscount(foodDiscount.getText().toString());
                            item.setDeliveryTime(foodDeliveryTime.getText().toString());
                            item.setImage(uri.toString());

                            foods.child(key).setValue(item);
                            Toast.makeText(FoodActivityServer.this, "Food updated successfully", LENGTH_SHORT).show();
                            saveUri = null;
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = (int) (100.0 * (taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploading : " + progress);
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please provide all details", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteFood(String key) {
        foods.child(key).removeValue();
    }


}
