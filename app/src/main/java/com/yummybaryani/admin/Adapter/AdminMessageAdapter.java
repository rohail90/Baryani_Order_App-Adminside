package com.yummybaryani.admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yummybaryani.admin.Common.Common;
import com.yummybaryani.admin.Model.Chat;
import com.yummybaryani.admin.R;

import java.util.List;


public class AdminMessageAdapter extends RecyclerView.Adapter<AdminMessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChat;
    private String Admin= Common.ADMIN;
    public AdminMessageAdapter(Context mContext, List<Chat> mChat){
        this.mContext = mContext;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public AdminMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);

            return new AdminMessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);

            return new AdminMessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull AdminMessageAdapter.ViewHolder holder, final int position) {

        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message= itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(mChat.get(position).getSender().equals(Admin)){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}