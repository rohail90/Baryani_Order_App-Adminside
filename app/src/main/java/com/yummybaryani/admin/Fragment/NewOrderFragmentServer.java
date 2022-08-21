package com.yummybaryani.admin.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yummybaryani.admin.Adapter.ProductsAdapter;
import com.yummybaryani.admin.Async.SendNotificationTask;
import com.yummybaryani.admin.Common.Common;
import com.yummybaryani.admin.Database.PrefManager;
import com.yummybaryani.admin.Model.Request;
import com.yummybaryani.admin.Model.Token;
import com.yummybaryani.admin.R;
import com.yummybaryani.admin.Server.NewOrdersDetailsActivity;
import com.yummybaryani.admin.UIUtils;
import com.yummybaryani.admin.ViewHolders.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewOrderFragmentServer extends Fragment {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference request,tokens;
    FirebaseRecyclerAdapter adapter;
    Context context;
    PrefManager prefManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ongoing_fragment_customer, container, false);
        context = container.getContext();
        prefManager=new PrefManager(context);
        //init Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");
        tokens = firebaseDatabase.getReference("Tokens");

        recyclerView = view.findViewById(R.id.recycler_order_status);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        showOrders("0");

        return view;
    }


    private void showOrders(String statusCode) {
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(
                request.orderByChild("status").equalTo(statusCode), Request.class).build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_orders_list_item_server, parent, false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Request model) {

                final String orderID = adapter.getRef(position).getKey();
                TextView textViewId = holder.itemView.findViewById(R.id.order_id);
                textViewId.setText(adapter.getRef(position).getKey());

                TextView customerName = holder.itemView.findViewById(R.id.customerName);
                customerName.setText(model.getName());

                TextView dateTime = holder.itemView.findViewById(R.id.order_dateTime);
                dateTime.setText(model.getPhone());

                TextView totalAmount = holder.itemView.findViewById(R.id.order_total);
                totalAmount.setText(model.getTotal());

                TextView cancelOrder = holder.itemView.findViewById(R.id.cancelOrderBtn);
                cancelOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        request.child(orderID).child("status").setValue("3");
                        request.child(orderID).child("cancelledBy").setValue(Common.CANCELLED_BY_ADMIN);


                        tokens.child(model.getPhone()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Token token = dataSnapshot.getValue(Token.class);
                                Log.d("TOKEN", "onDataChange: token is :  "+token.getToken());
                                if (token.getToken()!=null){
                                    SendNotificationTask sendNotificationTask=new SendNotificationTask(context,token.getToken(), Common.CANCEL_TITLE, Common.CANCEL_MSG);
                                    sendNotificationTask.execute();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Log.d("TOKEN", "onClick: token from order :  "+model.getUserToken());


                    }
                });
                TextView acceptOrder = holder.itemView.findViewById(R.id.acceptOrderBtn);
                acceptOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        request.child(orderID).child("status").setValue("1");
                        tokens.child(model.getPhone()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Token token = dataSnapshot.getValue(Token.class);
                                Log.d("TOKEN", "onDataChange: token is :  "+token.getToken());
                                if (token.getToken()!=null){
                                    SendNotificationTask sendNotificationTask=new SendNotificationTask(context,token.getToken(), Common.ACCEPT_TITLE, Common.ACCEPTD_MSG);
                                    sendNotificationTask.execute();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Log.d("TOKEN", "onClick: token from order :  "+model.getUserToken());

                    }
                });
                TextView callCustomer = holder.itemView.findViewById(R.id.callCustomerBtn);
                callCustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                TextView viewDetails = holder.itemView.findViewById(R.id.viewDetailsBtn);
                viewDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, NewOrdersDetailsActivity.class);
                        intent.putExtra("selectedOrder", model);
                        Log.d("ORDER", "OrderId : " + orderID);
                        intent.putExtra("orderID", orderID);
                        startActivity(intent);
                    }
                });
                // totalAmount.setText(Common.getStatus(model.getTotal()));

                ListView listView = holder.itemView.findViewById(R.id.quantity_info);
                ProductsAdapter productsAdapter = new ProductsAdapter(getContext(), model.getOrders());
                listView.setAdapter(productsAdapter);
                UIUtils.setListViewHeightBasedOnItems(listView);


                if (model.getOrders().get(0) != null) {
                    Log.d("ORDERS", "onBindViewHolder: new orders products quantity  " + model.getOrders().size());

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
}