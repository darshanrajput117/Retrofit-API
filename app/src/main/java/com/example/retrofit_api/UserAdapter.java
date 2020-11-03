package com.example.retrofit_api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_api.Utility.Globals;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<com.example.retrofit_api.UserAdapter.Viewholder> {

    Globals globals;
    Context context;
    public ArrayList<User.Rows> Userlist;
    public UserAdapter(Context context,ArrayList<User.Rows> userlist) {
        this.Userlist = userlist;
        this.context=context;
        this.globals= (Globals) context.getApplicationContext();
    }

    @NonNull
    @Override
    public com.example.retrofit_api.UserAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewitem;
        if(globals.getGridValue()==0){
            viewitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userlist_gridview, parent, false);
        }else if(globals.getGridValue()==1){
            viewitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        }else{
            viewitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        }
        return new Viewholder(viewitem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull com.example.retrofit_api.UserAdapter.Viewholder holder, int position) {
        com.example.retrofit_api.User.Rows user = Userlist.get(position);
        holder.tv_name.setText(user.first_name + " "+ user.last_name);
        holder.tv_emailid.setText(user.email_id);
        holder.tv_mno.setText(user.mobile_no);
        holder.tv_address.setText(user.address);
    }

    @Override
    public int getItemCount() {
        return Userlist.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        public AppCompatTextView tv_name, tv_emailid, tv_mno, tv_address;

        public Viewholder(View view) {
            super(view);
            this.tv_name = view.findViewById(R.id.tv_name);
            this.tv_emailid = view.findViewById(R.id.tv_emailid);
            this.tv_mno = view.findViewById(R.id.tv_mno);
            this.tv_address = view.findViewById(R.id.tv_address);
        }
    }
}