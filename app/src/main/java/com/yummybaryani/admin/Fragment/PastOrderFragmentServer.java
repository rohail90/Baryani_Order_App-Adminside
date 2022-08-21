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
import com.yummybaryani.admin.Model.Request;
import com.yummybaryani.admin.R;
import com.yummybaryani.admin.Server.CompletedOrderDetailActivity;
import com.yummybaryani.admin.UIUtils;
import com.yummybaryani.admin.ViewHolders.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PastOrderFragmentServer extends Fragment {

    RecyclerView recyclerView;
    TextView textViewName;
    TextView textViewStatus;
    TextView textViewPhone;
    TextView textViewAddress;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    FirebaseRecyclerAdapter adapter;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ongoing_fragment_customer, container, false);
        //init firebase
        context = container.getContext();
        //init Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");

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
                request.orderByChild("status").equalTo("4"), Request.class).build();
        /* FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(
                request, Request.class).build();*/ //to get all items of a collection

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_order_list_admin, parent, false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Request model) {
                final String orderID = adapter.getRef(position).getKey();
                TextView textViewId = holder.itemView.findViewById(R.id.order_id);
                textViewId.setText("Order Id:" + adapter.getRef(position).getKey());

                TextView textViewTotal = holder.itemView.findViewById(R.id.order_total);
                textViewTotal.setText("Total:" + model.getTotal());
                TextView customerName = holder.itemView.findViewById(R.id.customerName);
                customerName.setText(model.getName());

                TextView orderDateTime = holder.itemView.findViewById(R.id.order_dateTime);
                //textViewStatus.setText(Common.getStatus(model.getStatus()));
                TextView viewDetails = holder.itemView.findViewById(R.id.viewDetailsBtn);
                viewDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CompletedOrderDetailActivity.class);
                        intent.putExtra("selectedOrder", model);
                        Log.d("ORDER", "OrderId : " + orderID);
                        intent.putExtra("orderID", orderID);
                        startActivity(intent);
                    }
                });

                ListView listView = holder.itemView.findViewById(R.id.quantity_info);
                ProductsAdapter productsAdapter = new ProductsAdapter(getContext(), model.getOrders());
                listView.setAdapter(productsAdapter);
                UIUtils.setListViewHeightBasedOnItems(listView);


                if (model.getOrders().get(0) != null) {
                    Log.d("ORDERS", "onBindViewHolder: oreder products q  " + model.getOrders().size());

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