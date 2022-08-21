package com.yummybaryani.admin.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yummybaryani.admin.Adapter.ProductsAdapter;
import com.yummybaryani.admin.Async.SendNotificationTask;
import com.yummybaryani.admin.Common.Common;
import com.yummybaryani.admin.Model.Request;
import com.yummybaryani.admin.Model.Token;
import com.yummybaryani.admin.R;
import com.yummybaryani.admin.Server.OngoingOrderDetailActivityServer;
import com.yummybaryani.admin.UIUtils;
import com.yummybaryani.admin.ViewHolders.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OnGoingOrderFragmentServer extends Fragment {

    RecyclerView recyclerView;
    TextView textViewName;
    TextView textViewStatus;
    TextView textViewPhone;
    TextView textViewAddress;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference request,products,tokens;
    FirebaseRecyclerAdapter adapter;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ongoing_fragment_customer, container, false);
context=container.getContext();
        //init Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");
        products = firebaseDatabase.getReference("Food");
        tokens = firebaseDatabase.getReference("Tokens");

        textViewName = view.findViewById(R.id.order_id);
        textViewStatus = view.findViewById(R.id.order_status);
        textViewPhone = view.findViewById(R.id.order_phone);
        textViewAddress = view.findViewById(R.id.order_address);

        recyclerView = view.findViewById(R.id.recycler_order_status);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        showOrders("1");

        return view;
    }

    //Helper Method
    private void showOrders(String phone) {
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(
                request.orderByChild("status").equalTo("1"), Request.class).build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.on_going_order_list_item_server, parent, false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Request model) {
                final String orderID=adapter.getRef(position).getKey();
                TextView textViewId = holder.itemView.findViewById(R.id.order_id);
                textViewId.setText("Order Id:"+adapter.getRef(position).getKey());

              TextView textViewTotal = holder.itemView.findViewById(R.id.order_total);
                textViewTotal.setText("Total:"+model.getTotal());
                TextView customerName = holder.itemView.findViewById(R.id.customerName);
                customerName.setText(model.getName());

                TextView viewDetails = holder.itemView.findViewById(R.id.viewDetailsBtn);
                viewDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, OngoingOrderDetailActivityServer.class);
                        intent.putExtra("selectedOrder",model);
                        Log.d("ORDER", "OrderId : "+orderID);
                        intent.putExtra("orderID",orderID);
                        startActivity(intent);
                    }
                });
                Spinner statusSpinner = (Spinner) holder.itemView.findViewById(R.id.orderStatusSpinner);

                setSpinnerItems(statusSpinner,orderID,model);
                ListView listView = holder.itemView.findViewById(R.id.quantity_info);
                ProductsAdapter productsAdapter=new ProductsAdapter(getContext(),model.getOrders());
                listView.setAdapter(productsAdapter);
                UIUtils.setListViewHeightBasedOnItems(listView);


                if (model.getOrders().get(0)!=null ){
                    Log.d("ORDERS", "onBindViewHolder: oreder products q  "+model.getOrders().size());

                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
    private void setSpinnerItems(Spinner spinner, final String orderId, final Request model) {
        Log.d("onResume", "colorSpinnerImplementation");

       final ArrayList statusList = new ArrayList<>();
        statusList.add("Shipping");
        statusList.add("Shipped");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_items, R.id.statusTv, statusList);
        adapter.setDropDownViewResource(R.layout.spinner_items);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {
                    //Toast.makeText(getContext(),"Please Select Auto Color",Toast.LENGTH_SHORT).show();
                    tokens.child(model.getPhone()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Token token = dataSnapshot.getValue(Token.class);
                            Log.d("TOKEN", "onDataChange: token is :  "+token.getToken());
                            if (token.getToken()!=null){
                                SendNotificationTask sendNotificationTask=new SendNotificationTask(context,token.getToken(), Common.Complete_TITLE, Common.Complete_MSG);
                                sendNotificationTask.execute();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    request.child(orderId).child("status").setValue("4");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}//class ends