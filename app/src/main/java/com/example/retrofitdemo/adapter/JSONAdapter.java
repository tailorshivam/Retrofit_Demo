package com.example.retrofitdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofitdemo.R;
import com.example.retrofitdemo.databinding.ItemJsonBinding;
import com.example.retrofitdemo.model.User;

import java.util.List;

public class JSONAdapter extends RecyclerView.Adapter<JSONAdapter.ViewHolder> {

    Context context;
    List<User> userList;

    public JSONAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_json, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.itemJsonBinding.txtUserName.setText(user.getUsername());
        holder.itemJsonBinding.txtCity.setText(user.getAddress().getCity());
        holder.itemJsonBinding.txtCompanyName.setText(user.getCompany().getName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemJsonBinding itemJsonBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemJsonBinding = DataBindingUtil.bind(itemView);
        }
    }
}
