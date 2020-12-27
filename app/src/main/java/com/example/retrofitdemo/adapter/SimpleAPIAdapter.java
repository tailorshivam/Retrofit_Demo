package com.example.retrofitdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofitdemo.R;
import com.example.retrofitdemo.databinding.ItemSimpleBinding;
import com.example.retrofitdemo.model.Todos;

import java.util.List;

public class SimpleAPIAdapter extends RecyclerView.Adapter<SimpleAPIAdapter.ViewHolder> {

    Context context;
    List<Todos> todosList;

    public SimpleAPIAdapter(Context context, List<Todos> todosList) {
        this.context = context;
        this.todosList = todosList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_simple, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todos todos = todosList.get(position);
        holder.itemSimpleBinding.txtId.setText(String.format("%d", todos.getId()));
        holder.itemSimpleBinding.txtTitle.setText(todos.getTitle());
        if (todos.getCompleted())
            holder.itemSimpleBinding.txtCompleted.setText("true");
        else
            holder.itemSimpleBinding.txtCompleted.setText("false");
    }

    @Override
    public int getItemCount() {
        return todosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemSimpleBinding itemSimpleBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemSimpleBinding = DataBindingUtil.bind(itemView);
        }
    }
}
