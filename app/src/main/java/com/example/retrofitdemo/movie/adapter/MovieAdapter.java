package com.example.retrofitdemo.movie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.retrofitdemo.R;
import com.example.retrofitdemo.databinding.ItemMovieListBinding;
import com.example.retrofitdemo.movie.Interface.ItemClickListener;
import com.example.retrofitdemo.movie.model.Result;

import java.util.List;

import static com.example.retrofitdemo.movie.utils.Constants.IMAGES_PATH;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Result> resultList;
    ItemClickListener clickListener;

    public MovieAdapter(Context context, List<Result> resultList, ItemClickListener clickListener) {
        this.context = context;
        this.resultList = resultList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(IMAGES_PATH + resultList.get(position).getPosterPath()).into(holder.movieListBinding.ivPoster);
        holder.movieListBinding.txtTitle.setText(resultList.get(position).getTitle());
        holder.movieListBinding.txtReleaseDate.setText(resultList.get(position).getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemMovieListBinding movieListBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieListBinding = DataBindingUtil.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
