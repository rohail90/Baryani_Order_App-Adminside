package com.yummybaryani.admin.Server;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yummybaryani.admin.Model.OurOffers;
import com.yummybaryani.admin.R;
import com.yummybaryani.admin.ViewHolders.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import static android.widget.Toast.LENGTH_SHORT;

public class SpecialMealsActivity extends AppCompatActivity  {
    RelativeLayout relativeLayout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference specialMeal;
    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerView;
    private MaterialEditText mealName,mealPrice,mealDescription;
    private Button btnUpload;
    private Button btnSelect;
    private static final int IMAGE_REQUEST_CODE = 100;
    private Uri saveUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    Toolbar toolbar;
    com.yummybaryani.admin.CustomFont.NativelyCustomEditText foodName,foodShortDescription,foodDiscount,foodHalfPrice,foodFullPrice,
            foodMediumPrice,foodDeliveryTime,chattniPrice,saladPrice,shamiPrice;
    TextView productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_meal_activity);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Special Meal");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        specialMeal = firebaseDatabase.getReference("specialMeal");

        recyclerView =findViewById(R.id.recycler_order_status);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMealDialog();
            }
        });
        showOurOfferss();
    }
    private void showOurOfferss() {
        FirebaseRecyclerOptions<OurOffers> options = new FirebaseRecyclerOptions.Builder<OurOffers>().setQuery(
                specialMeal.orderByChild("OurOffers"), OurOffers.class).build();

        adapter = new FirebaseRecyclerAdapter<OurOffers, OrderViewHolder>(options) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_offer_admin, parent, false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final OurOffers model) {

                final String mealID=adapter.getRef(position).getKey();
                TextView itemName = holder.itemView.findViewById(R.id.itemName);
                itemName.setText(model.getName());

                TextView itemDescription = holder.itemView.findViewById(R.id.itemDescription);
                itemDescription.setText(model.getDescription());

                TextView itemPrice = holder.itemView.findViewById(R.id.itemPrice);
                itemPrice.setText("Rs "+model.getDiscount());

                final TextView toggleButtonOn = holder.itemView.findViewById(R.id.tbOn);
                final TextView toggleButtonOff = holder.itemView.findViewById(R.id.tbOff);
                if (model.getEnabled()){
                    Log.d("TAG", "if Enabled: "+model.getEnabled());
                    toggleButtonOn.setBackground(getResources().getDrawable(R.drawable.border_radius_toggle_orange));
                    toggleButtonOn.setTextColor(getResources().getColor(R.color.white));
                    toggleButtonOff.setBackground(getResources().getDrawable(R.drawable.border_radius_toggle));
                    toggleButtonOff.setTextColor(getResources().getColor(R.color.Black));
                }else {
                    Log.d("TAG", "else Enabled: "+model.getEnabled());

                    toggleButtonOn.setBackground(getResources().getDrawable(R.drawable.border_radius_toggle));
                    toggleButtonOn.setTextColor(getResources().getColor(R.color.Black));
                    toggleButtonOff.setBackground(getResources().getDrawable(R.drawable.border_radius_toggle_orange));
                    toggleButtonOff.setTextColor(getResources().getColor(R.color.white));
                }
                de.hdodenhof.circleimageview.CircleImageView itemImage=holder.itemView.findViewById(R.id.prod_image);
                Picasso.get().load(model.getImage()).into(itemImage);
               toggleButtonOn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       toggleButtonOn.setBackground(getResources().getDrawable(R.drawable.border_radius_toggle_orange));
                       toggleButtonOn.setTextColor(getResources().getColor(R.color.white));
                       toggleButtonOff.setBackground(getResources().getDrawable(R.drawable.border_radius_toggle));
                       toggleButtonOff.setTextColor(getResources().getColor(R.color.Black));

                       specialMeal.child(mealID).child("enabled").setValue("true");


                   }
               });
                toggleButtonOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        toggleButtonOn.setBackground(getResources().getDrawable(R.drawable.border_radius_toggle));
                        toggleButtonOn.setTextColor(getResources().getColor(R.color.Black));
                        toggleButtonOff.setBackground(getResources().getDrawable(R.drawable.border_radius_toggle_orange));
                        toggleButtonOff.setTextColor(getResources().getColor(R.color.white));
                        specialMeal.child(mealID).child("enabled").setValue("false");


                    }
                });
                // totalAmount.setText(model.getTotal());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TAG", "onClick: ");
                        editOrDeleteOfferDialog(mealID,model);
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
    private void addMealDialog() {
        Intent intent=new Intent(this,AddSpecialMealItemActivity.class);
        startActivity(intent);

        /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Add new Special Meal");
        alertDialog.setMessage("Please provide information about Special Meal");
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        View view = getLayoutInflater().inflate(R.layout.custom_add_speacial_meal_dialog, null);
        alertDialog.setView(view);

        mealName = view.findViewById(R.id.mealName);
        mealPrice = view.findViewById(R.id.mealPrice);
        mealDescription = view.findViewById(R.id.mealDescription);

        btnSelect = view.findViewById(R.id.btnSelect);
        btnUpload = view.findViewById(R.id.btnUpload);

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
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }
   /* private void uploadImage() {
        if(saveUri != null && !mealName.getText().toString().equals(""))
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
                            OurOffers OurOffers = new OurOffers(mealName.getText().toString(),mealDescription.getText().toString(),mealPrice.getText().toString(),true, uri.toString());
                            OurOfferssActivity.this.OurOffers.push().setValue(OurOffers);
                            Snackbar.make(recyclerView, "Special Meal added successfully", Snackbar.LENGTH_SHORT).show();
                            saveUri = null;

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Snackbar.make(recyclerView, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Please provide name and image both", Toast.LENGTH_SHORT).show();
        }
    }*/
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
    private void editOrDeleteOfferDialog(final String mealID, final OurOffers model) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
      /**//*  alertDialog.setTitle("Add new Special Meal");
        alertDialog.setMessage("Please provide information about Special Meal");
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);*/
        alertDialog.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.top_offer_dialog, null);
        alertDialog.setView(view);

        ImageView itemImage=view.findViewById(R.id.prod_image);
        Picasso.get().load(model.getImage()).into(itemImage);
       TextView editTv = view.findViewById(R.id.edit_item);
        TextView deleteTv = view.findViewById(R.id.delete_item);
        TextView productName = view.findViewById(R.id.product_name);
        TextView productDetail = view.findViewById(R.id.product_detail);
        productName.setText(model.getName());
        productDetail.setText(model.getDescription());
        editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMealDialog(mealID,model,alertDialog);
            }
        });
        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specialMeal.child(mealID).removeValue();
                alertDialog.dismiss();
            }
        });

        /*alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });*/

        alertDialog.show();

    }
    private void updateMealDialog(final String mealId, final OurOffers item,android.app.AlertDialog editOrDelDialog) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
        alertDialog.setTitle("Update Offer");
        alertDialog.setMessage("Please provide information to update Offer");
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        View view = getLayoutInflater().inflate(R.layout.add_item_category_activity, null);
        alertDialog.setView(view);
        alertDialog.setCancelable(true);
        editOrDelDialog.dismiss();

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

        chattniPrice.setText(item.getSides().get(0).getPrice());
        saladPrice.setText(item.getSides().get(1).getPrice());
        shamiPrice.setText(item.getSides().get(2).getPrice());
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
                updateImage(mealId, item,alertDialog);
            }
        });

       /* alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                saveUri = null;
            }
        });*/
        alertDialog.show();
    }
    private void updateImage(final String mealID, final OurOffers item, final android.app.AlertDialog editOrDelDialog) {
        if(saveUri != null && !foodName.getText().toString().equals("") && !foodShortDescription.getText().toString().equals("")&&
                !foodFullPrice.getText().toString().equals("") && !foodMediumPrice.getText().toString().equals("") && !foodHalfPrice.getText().toString().equals("")
                && !foodDiscount.getText().toString().equals("")&& !foodDeliveryTime.getText().toString().equals("")&& !chattniPrice.getText().toString().equals("")&& !saladPrice.getText().toString().equals("")&& !shamiPrice.getText().toString().equals("") )
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

                            item.getSides().get(0).setPrice(chattniPrice.getText().toString());
                            item.getSides().get(1).setPrice(saladPrice.getText().toString());
                            item.getSides().get(2).setPrice(shamiPrice.getText().toString());

                            item.setDiscount(foodDiscount.getText().toString());
                            item.setDeliveryTime(foodDeliveryTime.getText().toString());
                            item.setImage(uri.toString());

                            specialMeal.child(mealID).setValue(item);
                            Toast.makeText(SpecialMealsActivity.this, "Special Meal updated successfully", LENGTH_SHORT).show();
                            saveUri = null;
                            editOrDelDialog.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Snackbar.make(foodName, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
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
}