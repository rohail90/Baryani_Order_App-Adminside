package com.yummybaryani.admin.Server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yummybaryani.admin.Common.Common;
import com.yummybaryani.admin.Model.Food;
import com.yummybaryani.admin.Model.Price_;
import com.yummybaryani.admin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import static android.widget.Toast.LENGTH_SHORT;

public class AddItemCategoryActivity extends Activity implements View.OnClickListener {

    private static final int IMAGE_REQUEST_CODE = 100;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference foods;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private Uri saveUri;
    String categoryId;
    private Intent intent = new Intent();
    TextView productImage;
    ArrayList<Price_> foodPricesList=new ArrayList<>();
    com.yummybaryani.admin.CustomFont.NativelyCustomEditText foodName,foodShortDescription,foodDiscount,foodHalfPrice,foodFullPrice,
            foodMediumPrice,foodDeliveryTime;
    Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_category_activity);

        firebaseDatabase = FirebaseDatabase.getInstance();
        foods = firebaseDatabase.getReference("Food");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        productImage=findViewById(R.id.upload_image);
        saveBtn=findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);
        productImage.setOnClickListener(this);

        if (getIntent().getExtras()!=null){
            categoryId = getIntent().getStringExtra("categoryId");
        }

        foodName = findViewById(R.id.food_name);
        foodShortDescription = findViewById(R.id.food_description);
        foodFullPrice = findViewById(R.id.full_biryaniPrice);
        foodMediumPrice = findViewById(R.id.medium_biryaniPrice);
        foodDeliveryTime=findViewById(R.id.food_deliveryTime);
        foodHalfPrice = findViewById(R.id.half_biryaniPrice);
        foodDiscount = findViewById(R.id.food_discount);


    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    public void onClick(View view) {

        if (view==saveBtn){
            uploadImage();
        }else if (view==productImage){
            selectImage();
        }
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
    private void uploadImage() {
        if(saveUri != null && !foodName.getText().toString().equals("") && !foodShortDescription.getText().toString().equals("")&&
                !foodFullPrice.getText().toString().equals("") && !foodMediumPrice.getText().toString().equals("") && !foodHalfPrice.getText().toString().equals("")
                && !foodDiscount.getText().toString().equals("")&& !foodDeliveryTime.getText().toString().equals("")&& categoryId != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();



            Price_ halfPrice=new Price_(Common.HalfBiryani,foodHalfPrice.getText().toString());
            Price_ mediumPrice=new Price_(Common.MediumBiryani,foodMediumPrice.getText().toString());
            Price_ fullPrice=new Price_(Common.FullBiryani,foodFullPrice.getText().toString());

            foodPricesList.clear();
            foodPricesList.add(halfPrice);
            foodPricesList.add(mediumPrice);
            foodPricesList.add(fullPrice);

            final String imageName = UUID.randomUUID().toString();
            final StorageReference imageStorage = storageReference.child("images/" + imageName);
            imageStorage.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            Food newFood = new Food(foodName.getText().toString(),
                                    foodShortDescription.getText().toString(),
                                    foodPricesList,
                                    foodDiscount.getText().toString(),
                                    categoryId,
                                    uri.toString(),foodDeliveryTime.getText().toString());
                            foods.push().setValue(newFood);
                            Snackbar.make(foodName, "Food added successfully", Snackbar.LENGTH_SHORT).show();
                            finish();
                            saveUri = null;
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


    private void selectImage(){
       intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

}
