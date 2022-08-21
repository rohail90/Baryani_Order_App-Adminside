package com.yummybaryani.admin.Server;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yummybaryani.admin.Adapter.ProductsAdapter;
import com.yummybaryani.admin.Model.Request;
import com.yummybaryani.admin.R;
import com.yummybaryani.admin.UIUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CancelledOrdersDetailActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout relativeLayout;
    Request model;
    TextView userName, orderDateTime, userPhoneNo, userEmail, userAddress, orderId, userMessage, subTotal, deliveryFee,
            discount, total, printInvoice, callBtn, emailBtn, navigateBtn;
    ListView listView;
    ProductsAdapter productsAdapter;
    String orderID="";
    int subTotalValue=0;
    DatabaseReference request;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_orders_detail);
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");
        if (getIntent()!=null && getIntent().getExtras()!=null){
            model=(Request) getIntent().getSerializableExtra("selectedOrder");
            orderID=getIntent().getStringExtra("orderID");
        }
        userName = findViewById(R.id.user_name);
        orderDateTime=findViewById(R.id.order_dateTime); //
        userPhoneNo=findViewById(R.id.user_phonenumber);
        userEmail=findViewById(R.id.userEmail);//
        userAddress=findViewById(R.id.userAddress);
        orderId=findViewById(R.id.orderId);//
        userMessage=findViewById(R.id.userMessage);//
        subTotal =findViewById(R.id.orderSubTotal);//
        deliveryFee=findViewById(R.id.orderDeliveryFee);//
        discount=findViewById(R.id.orderDiscount);//
        total=findViewById(R.id.orderTotal);

        orderId.setText("Order Id"+orderID);
        printInvoice=findViewById(R.id.printInvoiceBtn);
        callBtn=findViewById(R.id.callBtn);
        emailBtn=findViewById(R.id.emailBtn);
        navigateBtn=findViewById(R.id.navigateBtn);


        listView=findViewById(R.id.product_items);

        printInvoice.setOnClickListener(this);
        callBtn.setOnClickListener(this);
        emailBtn.setOnClickListener(this);
        navigateBtn.setOnClickListener(this);



        if (model!=null){
            if (model.getName()!=null){
                userName.setText(model.getName());
            }
            if (model.getPhone()!=null){
                userPhoneNo.setText(model.getPhone());
            }
            if (model.getAddress()!=null){
                userAddress.setText(model.getAddress());
            }
            if (model.getTotal()!=null){
                total.setText(model.getTotal());
            }
            if (model.getOrders()!=null){
                productsAdapter=new ProductsAdapter(this,model.getOrders());
                listView.setAdapter(productsAdapter);
                UIUtils.setListViewHeightBasedOnItems(listView);
                for (int i=0;i<model.getOrders().size();i++){
                    Log.d("Price", "Price: === "+ model.getOrders().get(i).getPrice());

                    if (model.getOrders().get(i).getPrice()!=null);
                    Log.d("Price", "Price: "+ model.getOrders().get(i).getPrice()+"  "+ Integer.parseInt(model.getOrders().get(i).getPrice()));
                    subTotalValue=subTotalValue+ Integer.parseInt(model.getOrders().get(i).getPrice());
                }
                subTotal.setText("Rs "+subTotalValue);
            }
        }

    }

    @Override
    public void onClick(View v) {
        //checking internet firstly
        /*if (Common.isInternetAvailable(this)) {

        } else {
            Snackbar.make(relativeLayout, "No Internet Connection!", Snackbar.LENGTH_LONG).show();
        }*/
        if (v==callBtn){
            if (model!=null && model.getPhone()!=null){
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:" + model.getPhone();
                Log.d("PHONE", "Customer Phone no: "+p);
                i.setData(Uri.parse(p));
                startActivity(i);
            }

        }else if (v==emailBtn){
            sendEmail();
        }
    }
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"abc@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Email", "Finished sending email...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CancelledOrdersDetailActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}